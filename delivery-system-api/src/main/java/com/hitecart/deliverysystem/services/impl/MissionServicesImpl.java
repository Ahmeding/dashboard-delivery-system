package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.*;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.*;
import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.services.*;
import com.hitecart.deliverysystem.specification.MissionSpecification;
import com.hitecart.deliverysystem.utils.AssignmentResult;
import com.hitecart.deliverysystem.model.DriverStats;
import com.hitecart.deliverysystem.utils.TimeSlot;
import com.hitecart.deliverysystem.validator.MissionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.View;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
@Slf4j
public class MissionServicesImpl implements IMissionService  {

    private final DriverStatsRepository driverStatsRepository;
    private final IDriverStatsService driverStatsService;
    private final AvailabilityManager availabilityManager;
    private final IChauffeurVehicleService chauffeurVehicleService;
    private final IMissionVehicleService missionVehicleService;
    private final IChauffeurVehicleRepository chauffeurVehicleRepository;
    private final MissionVehicleRepository missionVehicleRepository;
    private final MissionRepository missionRepository;
    private final EtapeRepository etapeRepository;
    private final INotificationService notificationService;
    private final IChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final View error;
    private final UtilisateurService utilisateurServiceImpl;
    private final IDeliveryDestinationService deliveryDestinationService;
    private final DeliveryPointRepository deliveryPointRepository;

    private LocalDateTime calculateNextAvailableTime(LocalDateTime missionEnd, double distance) {
        if (distance > 800) {
            return missionEnd.plusHours(24);// 24h rest for long trips
        } else if (distance > 200) {
            return missionEnd.plusHours(6);// 6h rest for medium trips
        } else {
            return missionEnd.plusHours(2);// 2h rest for short trips
        }
    }
    // Helper method to find expert driver (EXPERT level with least missions)
    private Chauffeur findExpertDriver(List<Chauffeur> availableDrivers,
                                       Map<Chauffeur, Integer> weeklyAssignmentCounts) {
        return availableDrivers.stream()
                .filter(d -> d.getExperienceLevel() == DriverExperience.EXPERT)
                .min(Comparator.comparingInt(d ->
                        weeklyAssignmentCounts
                                .getOrDefault(d,0)))
                .orElse(null);
    }

    // Helper method to find helper driver (non-EXPERT with least missions)
    private Chauffeur findHelperDriver(List<Chauffeur> availableDrivers,
                                       Map<Chauffeur, Integer> weeklyAssignmentCounts) {
        return availableDrivers.stream()
                .filter(d -> d.getExperienceLevel() != DriverExperience.EXPERT)
                .min(Comparator.comparingInt(d ->
                                weeklyAssignmentCounts.getOrDefault(d, 0))
                                )
                .orElse(null);
    }

    // Helper method to find single best driver (least missions overall)
    private List<Chauffeur> findSingleDriver(
            List<Chauffeur> availableDrivers) {
        return availableDrivers.stream()
                .filter(d -> d.getExperienceLevel() != DriverExperience.EXPERT)
                .min(Comparator.comparingInt(d -> d.getStats().getTotalMissions()))
                .map(List::of)
                .orElse(Collections.emptyList());
    }


    // Helper method to update stats and collect drivers
    private void updateDriverStats(
            List<Chauffeur> drivers,
            Mission mission,
            Set<Chauffeur> updatedDrivers
    ) {


        double distance = mission.getDeliveryDestination()
                .getServiceDelivery().getDistance();

        drivers.forEach(driver -> {
            driver.ensureStatsInitialized();  // Critical safety check
            // Update next available time
            driver.setNextAvailableTime(
                    calculateNextAvailableTime(mission.getDateFin(), distance)
            );
            // Update stats
            DriverStats stats = driver.getStats();
            try{
                if (mission.getDifficulty() == RouteDifficulty.DIFFICULT) {
                    stats.incrementLongTrips();
                } else {
                    stats.incrementStandardTrips();
                }
            }catch (Exception e){
                log.error("Failed to update stats for driver {}: {}",
                        driver.getId(), e.getMessage());
                // Create emergency stats record
                DriverStats emergencyStats = new DriverStats();
                emergencyStats.setChauffeur(driver);
                driver.setStats(emergencyStats);
                driver.getStats().incrementStandardTrips();
            }

            updatedDrivers.add(driver); // Add to Set (duplicates auto-removed)
        });
    }



    // =============================
    //
    // =============================
    @Transactional
    public void generateWeeklyAssignments() {
        Map<Chauffeur, Integer> weeklyAssignmentCounts = new HashMap<>(); // Track weekly assignments per driver

        Map<Vehicule, Integer> weeklyVehicleAssignmentCounts = new HashMap<>(); // Track weekly assignments per vehicle
        /* -------------------------------------------
         * 1. Define Weekly Timeframe
         * ------------------------------------------- */
        // Get current week: Sunday 00:00 to Saturday 23:59
        LocalDateTime weekStart = LocalDateTime.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                .truncatedTo(ChronoUnit.DAYS);

        LocalDateTime weekEnd = weekStart.plusDays(6)
                .with(LocalTime.MAX);
        // Initialize a Set to track updated drivers
        Set<Chauffeur> updatedDrivers = new HashSet<>();
        /* -------------------------------------------
         * 2. Fetch & Prepare Missions
         * ------------------------------------------- */
        // Get missions occurring this week
        List<Mission> missions = missionRepository.findByDateRange(weekStart, weekEnd);
        List<Mission> failedMissions = new ArrayList<>();
        long expiredCount = missions.stream()
                .filter(m -> m.getDateDebut().isBefore(LocalDateTime.now()))
                .count();

        log.info("Filtered {} expired missions", expiredCount);

        missions.removeIf(mission ->
                mission.getDateFin().isAfter(weekEnd) ||
                        mission.getDateDebut().isBefore(weekStart) ||
                        mission.getDateDebut().isBefore(LocalDateTime.now())
        );
        // Auto-calculate difficulty for each mission
        missions.forEach(mission -> {
            double distance = mission.getDeliveryDestination()
                    .getServiceDelivery()
                    .getDistance();

            LocalDateTime start = mission.getDateDebut();
            LocalDateTime end = mission.getDateFin();

            if (!isValidMissionDuration(distance, start, end)) {
                log.warn("Mission {} has invalid duration for {}km distance",
                        mission.getId(), distance);
                failedMissions.add(mission);
            }
        });
        missions.removeAll(failedMissions);


        // Sort missions: Difficult first, then longest duration
        missions.sort(Comparator
                .comparing(Mission::getDifficulty).reversed()
                .thenComparing(m->m.getDateDebut())
                .thenComparing(m->m.getDeliveryDestination()
                        .getServiceDelivery().getDistance())
        );

        /* -------------------------------------------
         * 3. Prepare Resources
         * ------------------------------------------- */
        chauffeurRepository.findAll()
                .forEach(driverStatsService::createStatsForDriver);

        List<Chauffeur> allDrivers = chauffeurRepository.findAll();
        List<Vehicule> allVehicles = vehiculeRepository.findAll();

        // Weekly rotation shuffle for fairness
        long weekNumber = ChronoUnit.WEEKS.between(LocalDate.of(
                2025, 1, 1), LocalDate.now());
        Collections.shuffle(allDrivers, new Random(weekNumber));

        // Initialize tracking systems

        Map<Vehicule, List<TimeSlot>> vehicleAvailability =
                availabilityManager.initializeVehicleAvailability(
                        allVehicles,
                        weekStart, weekEnd);

        availabilityManager.initializeDriverAvailability(
                allDrivers, weekStart, weekEnd);
        log.debug("Initialized vehicle availability:");
        vehicleAvailability.forEach((v, slots) ->
                log.trace("Vehicle {}: {} slots", v.getId(), slots.size())
        );
        /* -------------------------------------------
         * 4. Assignment Process
         * ------------------------------------------- */

        List<AssignmentResult> successfulAssignments = new ArrayList<>();
        logAssignmentRequirements(missions);

        for (Mission mission : missions) {

            TimeSlot missionSlot = new TimeSlot(
                    mission.getDateDebut(),
                    mission.getDateFin()
            );

            // --------------------------
            // Vehicle Selection
            // --------------------------
            Vehicule vehicle = findSuitableVehicle(mission,vehicleAvailability, weeklyVehicleAssignmentCounts);
            if (vehicle == null) {
                log.warn("No vehicle with merged availability for mission {}", mission.getId());
                failedMissions.add(mission);
                continue;
            }

            // --------------------------
            // Driver Selection
            // --------------------------


            List<DriverExperience> requiredLevels = mission.getDifficulty() == RouteDifficulty.DIFFICULT ?
                    List.of(DriverExperience.EXPERT, DriverExperience.INTERMEDIATE, DriverExperience.JUNIOR) :
                    List.of(DriverExperience.INTERMEDIATE, DriverExperience.JUNIOR);

            List<Chauffeur> availableDrivers =
    availabilityManager.getAvailableDrivers(missionSlot, requiredLevels).stream()
            .filter(driver->(driver.getNextAvailableTime()
                    == null ||
                    driver.getNextAvailableTime()
                            .isBefore(missionSlot.getStart()))&&
                    availabilityManager.isAvailable(driver,missionSlot))
                    .collect(Collectors.toList());


            List<Chauffeur> drivers = this.assignDrivers(
                    availableDrivers,
                    mission,
                    weeklyAssignmentCounts,
                    missions, // Pass all missions for difficulty check
                    availabilityManager,
                    missionSlot);
            // Update merged availability
            availabilityManager.blockResources(vehicle, drivers, missionSlot);
            successfulAssignments.add(new AssignmentResult(mission, vehicle, drivers));
            drivers.forEach(driver ->
                    weeklyAssignmentCounts.put(driver, weeklyAssignmentCounts.getOrDefault(driver, 0) + 1)
            );


            if (drivers.isEmpty()) {
                log.warn("Failed driver assignment despite merged vehicle slots");
                vehicleAvailability.get(vehicle).add(missionSlot);
                failedMissions.add(mission);
                continue;
            }
            // Update stats and add drivers to the Set
            // --------------------------
            // Update Resource States
            // --------------------------

            // Update driver availability
            updateDriverStats(drivers, mission, updatedDrivers);

            // Update vehicle availability
            if(vehicle!=null){
                updateVehicleAvailability(vehicle, missionSlot, vehicleAvailability);
            }
           // --------------------------
            // Record Assignment
            // --------------------------
            AssignmentResult result = new AssignmentResult(mission, vehicle, drivers);
            successfulAssignments.add(result);
        }

        /* -------------------------------------------
         * 6. Finalize & Save Results
         * ------------------------------------------- */

        // Before saving drivers
        updatedDrivers.forEach(driver -> {
            if (driver.getStats() == null) {
                log.warn("Emergency stats creation for driver {}", driver.getId());
                driver.ensureStatsInitialized();
            }
        });
        // Batch save all updated drivers in one query
        chauffeurRepository.saveAll(updatedDrivers);
        /* -------------------------------------------
         * 7. Finalize & Save Results
         * ------------------------------------------- */
        saveAssignments(successfulAssignments);
        logResults(successfulAssignments, failedMissions);
        updateDriverAssignmentStatus(successfulAssignments);

        logResourceUtilization(
                successfulAssignments.size(),
                failedMissions.size(),
                updatedDrivers,
                successfulAssignments,
                allDrivers.size(),
                allVehicles.size()
        );
    }

// ======================
// Helper Methods
// ======================

    private void updateVehicleAvailability(
            Vehicule vehicle,
            TimeSlot missionSlot,
            Map<Vehicule, List<TimeSlot>> availability) {

        availability.compute(vehicle, (v, slots) ->
                slots.stream()
                        .flatMap(slot -> {
                            // Split the slot into non-overlapping periods
                            List<TimeSlot> remaining = slot.subtract(missionSlot);
                            log.debug("Vehicle {}: Split {} into {} slots",
                                    v.getId(), slot, remaining.size());
                            return remaining.stream();

                        })
                        .filter(ts->ts.getStart().isBefore(ts.getEnd()))// Validate time slots
                        .collect(Collectors.toList())
        );
    }

    private void logResourceUtilization(
            int success,
            int failed,
            Set<Chauffeur> updatedDrivers,
            List<AssignmentResult> successfulAssignments,
            int totalDrivers,
            int totalVehicles) {
        log.info("Resource Utilization Report:");
        log.info("Successful assignments: {}", success);
        log.info("Failed assignments: {}", failed);
        log.info("Drivers used: {}/{}", updatedDrivers.size(), totalDrivers);
        log.info("Vehicles used: {}/{}",
                successfulAssignments.stream()
                        .map(a -> a.getVehicle().getId())
                        .distinct()
                        .count(),
                totalVehicles
        );
    }

private boolean isValidMissionDuration(double distance, LocalDateTime start, LocalDateTime end) {
    Duration duration = Duration.between(start, end);
    long hours = duration.toHours();
    if (distance > 1000) {
        // Allow 24-36 hours for long-haul missions (e.g., 1230km)
        return hours >= 24 && hours <= 36;  // Exactly 24 hours
    }
    else if (distance > 500) {
        // 12-24 hours for medium distances
        return hours >= 12 && hours <= 28;
    }
     // Up to 8 hours for short distances
    return hours <= 18;
}

    /**
     * Finds vehicle with sufficient capacity and availability
     */
    private Vehicule findSuitableVehicle(Mission mission, Map<Vehicule,
            List<TimeSlot>> availability, Map<Vehicule, Integer> weeklyVehicleAssignmentCounts) {

        TimeSlot missionSlot = new TimeSlot(mission.getDateDebut(), mission.getDateFin());
        log.debug("Finding vehicle for mission ID {} (Volume: {}, Time: {} to {})",
                mission.getId(), mission.getVolume(), missionSlot.getStart(), missionSlot.getEnd());

        return availability.entrySet().stream()
                .peek(entry -> {
                    Vehicule v = entry.getKey();
                    log.debug("Checking vehicle {} (Volume: {})",
                            entry.getKey().getId(), entry.getKey().getVolume());
                    log.debug("Available slots for vehicle {}: {}",
                            entry.getKey().getId(), entry.getValue());
                    log.debug("Vehicle {} availability slots: {}",
                            entry.getKey().getId(), entry.getValue());
                    log.debug("Inspecting vehicle {} (Vol: {}, Type: {}, Active: {})",
                            v.getId(), v.getVolume(), v.getVehicleType(), v.getIsActive());
                })
                // Enhanced filtering with failure reasons
                .filter(entry -> {
                    Vehicule v = entry.getKey();
                    // Check capacity
                    boolean hasCapacity =
                            v.getVolume()
                                    .compareTo(mission.getVolume()) >= 0;

                    log.debug("Vehicle {}: hasCapacity={} (Mission requires {}, Vehicle has {})",
                            entry.getKey().getId(), hasCapacity,
                            mission.getVolume(), entry.getKey().getVolume());

                    // Check availability
                    boolean availableSlot  = entry.getValue().stream()
                            .anyMatch(slot -> slot.covers(missionSlot));

                    if (!hasCapacity) {
                        log.trace("Reject vehicle {}: Insufficient capacity ({} < {})",
                                v.getId(), v.getVolume(), mission.getVolume());
                    }
                    if (!availableSlot) {
                        log.trace("Reject vehicle {}: No compatible time slots", v.getId());
                    }

                    log.debug("Vehicle {}: Capacity={}/{} Available={}",
                            entry.getKey().getId(),
                            entry.getKey().getVolume(),
                            mission.getVolume(),
                            availableSlot ? "Yes" : "No");
                    return hasCapacity && availableSlot;
                })
                // Better selection logic with prioritization
                .min(Comparator.comparingInt(
                        (Map.Entry<Vehicule, List<TimeSlot>> entry) ->
                                weeklyVehicleAssignmentCounts
                                        .getOrDefault(entry.getKey(),0)
                        ).thenComparing(entry->entry.getKey().getVolume())
                        .thenComparing(entry->entry.getValue().size(), // Secondary: Most available slots
                                Comparator.reverseOrder()
                 ))
                .map(entry -> {
                    Vehicule selectedVehicle = entry.getKey();
                    log.info("Selected vehicle {} for mission {} (Vol: {}, Slots remaining: {})",
                            selectedVehicle.getId(), mission.getId(),
                            selectedVehicle.getVolume(), entry.getValue().size());
// Update weekly assignment count
                    weeklyVehicleAssignmentCounts.merge(selectedVehicle, 1, Integer::sum);

                    return selectedVehicle;
                })
                .orElseGet(() -> {
                    log.warn("No vehicle found for mission {} (Req Vol: {}, Window: {}->{})",
                            mission.getId(), mission.getVolume(),
                            missionSlot.getStart(), missionSlot.getEnd());
                    return null;
                });
    }

    /**
     *
     * @param availableDrivers
     * @param mission
     * @param weeklyAssignmentCounts
     * @param allMissions
     * @param availabilityManager
     * @param missionSlot
     * @return
     */
    private List<Chauffeur> assignDrivers(
            List<Chauffeur> availableDrivers,
            Mission mission,
            Map<Chauffeur, Integer> weeklyAssignmentCounts,
            List<Mission> allMissions, // Pass the full mission list
            AvailabilityManager availabilityManager,
            TimeSlot missionSlot ) {

        List<Chauffeur> drivers = new ArrayList<>();

        log.debug("Starting driver assignment for mission {} (Difficulty: {})",
                mission.getId(), mission.getDifficulty());
    if (mission.getDifficulty() == RouteDifficulty.DIFFICULT) {
            log.debug("Processing difficult mission {}", mission.getId());


            // Log initial available drivers
            logAvailableDrivers(availableDrivers, "Initial available drivers for difficult mission");

            // Try expert + helper first
            Chauffeur expert = findExpertDriver(availableDrivers,weeklyAssignmentCounts);
            Chauffeur helper = findHelperDriver(availableDrivers,weeklyAssignmentCounts);

            if (expert != null && helper != null) {
                drivers.add(expert);
                drivers.add(helper);
                log.info("Assigned expert+helper pair: {} and {} to mission {}",
                        expert.getId(), helper.getId(), mission.getId());
            } else if (expert != null) {
                    // Expert + any available non-expert
                Chauffeur fallbackDriver = availableDrivers
                        .stream()
                                .filter(d->d !=expert)
                                        .findFirst()
                                                .orElse(null);
                if(fallbackDriver != null) {
                    drivers.add(fallbackDriver);
                    drivers.add(fallbackDriver);
                    log.info("Assigned expert + fallback pair : {} and {} to mission {} ",
                            expert.getId(), fallbackDriver.getId(), mission.getId());
                }

            } else {
                // New priority: Intermediate + Junior
                log.debug("No experts available, trying intermidiate/junior combination");

                List<Chauffeur> intermediates= availableDrivers.stream()
                        .filter(d->d.getExperienceLevel()
                        == DriverExperience.INTERMEDIATE)
                        .collect(Collectors.toList());

                List<Chauffeur> juniors = availableDrivers.stream()
                        .filter(d->d.getExperienceLevel()
                                == DriverExperience.JUNIOR)
                        .collect(Collectors.toList());

                log.debug("Available intermediates: {}", intermediates.size());
                log.debug("Available juniors: {}", juniors.size());

                // First try two intermediates
                if(intermediates.size()>=2){
                    drivers = intermediates.stream()
                            .sorted(Comparator.comparingInt(d->
                                    weeklyAssignmentCounts.getOrDefault(d,0)))
                            .limit(2)
                            .collect(Collectors.toList());
                    log.info("Assigned two intermediates: {} and {} to mission {}",
                            drivers.get(0).getId(),drivers.get(1).getId(), mission.getId());
                }
                // Then try intermediate + junior
                else if (!intermediates.isEmpty() && !juniors.isEmpty()) {
                    Chauffeur intermediate = intermediates.stream()
                            .min(Comparator.comparingInt(d ->
                                    weeklyAssignmentCounts.getOrDefault(d,0)))
                            .orElse(null);

                    Chauffeur junior = juniors.stream()
                            .min(Comparator.comparingInt(d ->
                                    weeklyAssignmentCounts.getOrDefault(d,0)))
                            .orElse(null);

                    if (intermediate != null && junior != null) {
                        drivers.add(intermediate);
                        drivers.add(junior);
                        log.info("Assigned intermediate+junior pair: {} and {} to mission {}",
                                intermediate.getId(), junior.getId(), mission.getId()); }
                }else{
                    // Fallback to any two available drivers
                    drivers = availableDrivers.stream()
                            .limit(2)
                            .collect(Collectors.toList());
                    if (!drivers.isEmpty()) {
                        log.warn("Using fallback drivers: {} and {} for mission {}",
                                drivers.size() > 0 ? drivers.get(0).getId() : "none",
                                drivers.size() > 1 ? drivers.get(1).getId() : "none",
                                mission.getId());
                    }
                }
            }
        }
        else {
            // For standard missions, prioritize least busy non-expert driver
            // First try non-experts
            List<Chauffeur> nonExperts = availableDrivers.stream()
                    .filter(d -> d.getExperienceLevel() != DriverExperience.EXPERT)
                    .collect(Collectors.toList());
            if (!nonExperts.isEmpty()) {
                drivers = nonExperts.stream()
                        .min(Comparator.comparingInt(d ->
                                weeklyAssignmentCounts.getOrDefault(d, 0)
                        ))
                        .map(List::of)
                        .orElse(Collections.emptyList());
            }else {
                // Check if all difficult missions are assigned
                boolean allDifficultCovered = allMissions.stream()
                        .filter(m -> m.getDifficulty() == RouteDifficulty.DIFFICULT)
                        .allMatch(m -> m.getAssignmentStatus() == AssignmentStatus.ASSIGNED);

                if (allDifficultCovered) {
                    // Get available experts for this slot
                    List<Chauffeur> experts = availabilityManager.getAvailableDrivers(
                                    missionSlot,
                                    List.of(DriverExperience.EXPERT)
                            ).stream()
                            .filter(d ->
                                    (d.getNextAvailableTime() == null ||
                                            d.getNextAvailableTime().isBefore(missionSlot.getStart())) &&
                                            availabilityManager.isAvailable(d, missionSlot)
                            )
                            .collect(Collectors.toList());
                    drivers = experts.stream()
                            .min(Comparator.comparingInt(d ->
                                            weeklyAssignmentCounts.getOrDefault(d, 0)
                                    ))  // Close min() first
                                    .map(List::of)  // Now working with Optional<Chauffeur>
                                    .orElse(Collections.emptyList());
                    if (!drivers.isEmpty()) {
                        log.info("Assigned expert driver {} to standard mission {} (fallback)",
                                drivers.get(0).getId(), mission.getId());
                    }
                }
            }
        }

        // Validate driver count for dificulte missions
        // Validation and cleanup
        if(mission.getDifficulty() == RouteDifficulty.DIFFICULT
        && drivers.size()!=2) {
            log.warn("Failed to assign 2 drivers for difficult mission {}. Found: {}/2",

                    mission.getId(), drivers.size());
            log.debug("Available drivers at failure: {}",
                    availableDrivers.stream()
                            .map(d->d.getId()+ ":" + d.getExperienceLevel())
                            .collect(Collectors.joining(", ")));
            return Collections.emptyList();
        }
        log.debug("Final driver assignment for mission {}: {}",
                mission.getId(), drivers.stream()
                        .map(d -> d.getId() + "(" + d.getExperienceLevel() + ")")
                        .collect(Collectors.joining(", ")));
        // Ok
        return drivers.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Helper method for logging
    private void logAvailableDrivers(List<Chauffeur> drivers, String context) {
        log.debug("{} ({} drivers): {}", context, drivers.size(), drivers.stream()
                .map(d -> d.getId() + "(" + d.getExperienceLevel() + ")")
                .collect(Collectors.joining(", ")));
    }

    /**
     *
     * @param assignments
     */
    private void saveAssignments(List<AssignmentResult> assignments) {
        List<MissionVehicle> missionVehicles = new ArrayList<>();
        List<ChauffeurVehicle> chauffeurVehicles = new ArrayList<>();

        for (AssignmentResult assignment : assignments) {
            // Create MissionVehicle
            MissionVehicle mv = MissionVehicle.builder()
                    .mission(assignment.getMission())
                    .vehicule(assignment.getVehicle())
                    .assignedAt(LocalDateTime.now())
                    .build();
            missionVehicles.add(mv);

            // Create ChauffeurVehicle links
            for (Chauffeur driver : assignment.getDrivers()) {
                ChauffeurVehicle cv = ChauffeurVehicle.builder()
                        .missionVehicle(mv)
                        .chauffeur(driver)
                        .assignedAt(LocalDateTime.now())
                        .build();
                chauffeurVehicles.add(cv);
            }
        }

        // Batch save to database
        missionVehicleRepository.saveAll(missionVehicles);
        chauffeurVehicleRepository.saveAll(chauffeurVehicles);
    }
    // =============================
    //
    // =============================

    private void logResults(List<AssignmentResult> assignments, List<Mission> unassignedMissions) {
        // Log success stats
        log.info("Successfully assigned {} missions:", assignments.size());
        assignments.forEach(assignment ->
                log.debug("Mission ID {}: Driver {}, Vehicle {}",
                        assignment.getMission().getId(),
                        assignment.getDrivers().size(),
                        assignment.getVehicle().getId())
        );

        // Log failures
        if (!unassignedMissions.isEmpty()) {
            log.warn("Could not assign {} missions:", unassignedMissions.size());
            unassignedMissions.forEach(mission ->
                    log.warn("Unassigned Mission ID {} ({} to {})",
                            mission.getId(),
                            mission.getDateDebut(),
                            mission.getDateFin())
            );
        }
    }

    private void updateDriverAssignmentStatus(List<AssignmentResult> assignments) {
        // Collect all unique driver IDs
        Set<Integer> driverIds = assignments.stream()
                .flatMap(a -> a.getDrivers().stream())
                .map(Chauffeur::getId)
                .collect(Collectors.toSet());

        // Bulk update isAssigned status
        if (!driverIds.isEmpty()) {
            chauffeurRepository.bulkUpdateAssignmentStatus(new ArrayList<>(driverIds), true);
        } else {
            log.info("No drivers to update assignment status.");
        }
        //chauffeurRepository.bulkUpdateAssignmentStatus(new ArrayList<>(driverIds), true);
    }

    private void logAssignmentRequirements(List<Mission> missions) {
        long difficultMissions = missions.stream()
                .filter(m -> m.getDifficulty() == RouteDifficulty.DIFFICULT)
                .count();

        log.info("Weekly Assignment Requirements:");
        log.info("Total missions: {}", missions.size());
        log.info("Difficult missions requiring 2 drivers: {}", difficultMissions);
        log.info("Standard missions requiring 1 driver: {}", missions.size() - difficultMissions);
        log.info("Total vehicles needed: {}", missions.size());
    }
    // =============================
    // GENERATE MISSIONS
    // =============================

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void generateMissions() {
        log.info("Created mission -generateMissions-" );

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        deliveryPointRepository.findAllByEnabled(true).forEach(point -> {

            RecurrencePattern pattern = point.getRecurrencePattern();
           if(pattern !=null){
               // Determine date range for generation
               LocalDate startDate = pattern.getStartDate().isAfter(currentDate)?
                       pattern.getStartDate() : currentDate;
               LocalDate endDate = pattern.getEndDate()!=null?
                       pattern.getEndDate() :
                       LocalDate.of(currentYear,12,31);
               // only generate if end date is in the future
               Set<DayOfWeek> patternDayOfWeek = point.getRecurrencePattern().getDaysOfWeek();
               if (!patternDayOfWeek.isEmpty() && endDate.isAfter(currentDate)) {
                   generateForPoint(point, startDate, endDate);
               }
           }
           });

    }

    private void generateForPoint(DeliveryPoint point, LocalDate start, LocalDate end) {
        log.error("Created mission -generateForPoint-" );
        LocalDate patternStart= point.getRecurrencePattern().getStartDate();
        int targetWeek = getWeekOfMonth(patternStart);

        start.datesUntil(end.plusDays(1)).forEach(date -> {
            if(matchesRecurrencePattern(point, date)){
                log.error("Created mission -matchesRecurrencePattern(point, date)-" );
                createMissionIfNeeded(point, date);
            }
        });
    }
    private boolean matchesRecurrencePattern(DeliveryPoint point, LocalDate date) {
        RecurrencePattern pattern = point.getRecurrencePattern();

        // Basic date range validation
        if(date.isBefore(pattern.getStartDate())||
                (pattern.getEndDate()!=null &&
                        date.isAfter(pattern.getEndDate()))){
            log.error("Created mission -return false-" );
            return false;
        }

        // Must match at least one day of week
        if (!pattern.getDaysOfWeek().contains(date.getDayOfWeek())) {
            return false;
        }

        return switch (pattern.getRecurrenceInterval()) {
            // DAILY: Every day matching daysOfWeek (e.g. Mon/Wed/Fri)
            case DAILY -> true;
            // WEEKLY: Every week on these days
            case WEEKLY ->
                    ChronoUnit.WEEKS.between(pattern.getStartDate(), date) % 1 == 0;
            // BIWEEKLY: Every 2 weeks on these days
            case BIWEEKLY ->
                    ChronoUnit.WEEKS.between(pattern.getStartDate(), date) % 2 == 0;
            // MONTHLY: Same week of month as start date
            case MONTHLY -> {
                int targetWeek = getWeekOfMonth(pattern.getStartDate());
                LocalDate nthDay = date.withDayOfMonth(1)
                        .with(TemporalAdjusters.dayOfWeekInMonth(targetWeek
                        ,date.getDayOfWeek()));
                yield nthDay.equals(date);
            }
        };

    }
    // Get the week of the month (1st, 2nd, 3rd, etc.)
    private int getWeekOfMonth(LocalDate date) {
        return ((date.getDayOfMonth() - 1) / 7) + 1;
    }

    // Get the first day of the target week in a given month
    private LocalDate getWeekStartInMonth(LocalDate baseDate, int targetWeek, DayOfWeek startDay) {
        return baseDate
                .withDayOfMonth(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(targetWeek, startDay));
    }

    private void createMissionIfNeeded(DeliveryPoint point, LocalDate date) {

        // Mission timing (18:00 start, next day 14:00 end)
        LocalDateTime missionStart = date.atTime(18, 0);
        LocalDateTime missionEnd = date.plusDays(1).atTime(14, 0);
        double distance = point.getDeliveryDestination().getServiceDelivery().getDistance();
        RouteDifficulty routeDifficulty = RouteDifficulty.SIMPLE;
        if(distance>1000){
            routeDifficulty =RouteDifficulty.DIFFICULT;
            // Mission timing (18:00 start, next day 18:00 end)
            missionStart = date.atTime(18, 0);
            missionEnd = date.plusDays(1).atTime(18, 0);
        }else if(distance<800 && distance>200){
            routeDifficulty =RouteDifficulty.MEDIUM;
            // Mission timing (22:00 start, next day 15:00 end)
            missionStart = date.atTime(22, 0);
            missionEnd = date.plusDays(1).atTime(13, 0);
        }else{
            routeDifficulty =RouteDifficulty.SIMPLE;
            // Mission timing (6:00 start, same day 15:00 end)
            missionStart = date.atTime(6, 0);
            missionEnd = date.plusDays(0).atTime(13, 0);
        }



        boolean exists = missionRepository.existsByDeliveryDestinationAndTimeRange(
                point.getDeliveryDestination(),
                missionStart,
                missionEnd);


        if (!exists) {
                        point.getDeliveryDestination().getServiceDelivery().getDistance();
            Mission newMission = Mission.builder()
                    .titre("Mission " + point.getAddress().getVille())
                    .deliveryDestination(point.getDeliveryDestination())
                    .dateDebut(missionStart)
                    .difficulty(routeDifficulty)
                    .dateFin(missionEnd)
                    .volume(new BigDecimal(10))
                    .etatMission(EtatMission.PLANNED)
                    .build();
            missionRepository.save(newMission);
            log.info("Created mission for {} on {}", point.getName(), date);
        }
    }


    private List<Integer> extractVehicleIds(MissionDto dto){
       if (dto.getMissionVehiclesDtos() == null) {
           return Collections.emptyList();
       }
       return dto.getMissionVehiclesDtos().stream()
               .map(MissionVehicleDto::getVehicleDto)
               .filter(Objects::nonNull) // Filter out null vehicle DTOs
               .map(VehicleDto::getId)
               .filter(Objects::nonNull)
               .distinct()
               .collect(Collectors.toList());
   }
    private List<Integer> extractDriverIds(MissionDto dto){
        return dto.getMissionVehiclesDtos().stream()
                .flatMap(mv->mv.getChauffeursVehicleDto().stream())
                .map(ChauffeurVehicleDto::getChauffeurDto)
                .map(ChauffeurDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
   }
    private void updateVehicleAndDriverStatus(MissionDto dto,boolean isAssigned){
        List<Integer> vehicleIds = extractVehicleIds(dto);
        List<Integer> driverIds = extractDriverIds(dto);

        vehiculeRepository.bulkUpdateAssignmentStatus(vehicleIds, isAssigned);
        chauffeurRepository.bulkUpdateAssignmentStatus(driverIds, isAssigned);
    }
    private void createMissionRelationships(MissionDto dto, Mission savedMission){
       List<MissionVehicle> missionVehicles = new ArrayList<>();
       List<ChauffeurVehicle> chauffeurVehicles  = new ArrayList<>();
       List<Etape> etapes = new ArrayList<>();


            dto.getEtapeDtos().forEach(etp -> {
                Etape etape = createEtape(etp, savedMission);
                etapes.add(etape);
            });


            dto.getMissionVehiclesDtos().parallelStream().forEach(mvDto -> {
                MissionVehicle missionVehicle = createMissionVehicle(mvDto, savedMission);
                missionVehicles.add(missionVehicle);

                if(!mvDto.getChauffeursVehicleDto().isEmpty() && mvDto.getChauffeursVehicleDto().size()<=2 ){
                    mvDto.getChauffeursVehicleDto().parallelStream().forEach(cvDto -> {
                        chauffeurVehicles.add(createChauffeurVehicle(cvDto, missionVehicle));
                    });
                }
            });
            missionVehicleRepository.saveAll(missionVehicles);
            List<ChauffeurVehicle> savedChauffeurVehicles =
                    chauffeurVehicleRepository.saveAll(chauffeurVehicles);
            etapeRepository.saveAll(etapes);
            sendNotificationsAsync(savedChauffeurVehicles, savedMission.getId());

    }
    private Etape createEtape(EtapeDto etp, Mission savedMission) {
       Etape etape = EtapeDto.toEntity(etp);
       etape.setMission(savedMission);
       return etape;
    }

    @Async
    protected void sendNotificationsAsync(List<ChauffeurVehicle> chauffeurVehicles, Integer missionId) {
        chauffeurVehicles.parallelStream().forEach(cv -> {
            notificationService.sendNotification(
                    cv.getChauffeur().getId(),
                    NotificationDto.builder()
                            .title("New Mission Assignment")
                            .userId(cv.getChauffeur().getId())
                            .message("You've been assigned to mission #" + missionId)
                            .status(NotificationStatus.SEND)
                            .build()
            );
        });
    }

    private ChauffeurVehicle createChauffeurVehicle(ChauffeurVehicleDto cvDto, MissionVehicle missionVehicle) {
        return ChauffeurVehicle.builder()
                .missionVehicle(missionVehicle)
                .chauffeur(chauffeurRepository.getReferenceById(cvDto.getChauffeurDto().getId()))
                .assignedAt(LocalDateTime.now())
                .build();
   }

    private MissionVehicle createMissionVehicle(MissionVehicleDto mvDto, Mission savedMission) {
        return MissionVehicle.builder()
                .mission(savedMission)
                .vehicule(vehiculeRepository.getReferenceById(mvDto.getVehicleDto().getId()))
                .assignedAt(LocalDateTime.now())
                .build();
    }

    private void checkMissionAccomplishedStatus(MissionDto dto) {
        if (dto.getId() != null && dto.isMissionAccomplished()) {
            throw new InvalidOperationException("Cannot modify an accomplished mission",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
    }

    private void validateMissionDto(MissionDto dto){
        if (dto == null) {
            throw new InvalidOperationException("Mission DTO cannot be null",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        if (CollectionUtils.isEmpty(dto.getEtapeDtos())) {
            throw new InvalidOperationException("At least one Delivery Point must be specified",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        if (CollectionUtils.isEmpty(dto.getMissionVehiclesDtos())) {
            throw new InvalidOperationException("At least one vehicle must be specified",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }

        dto.getMissionVehiclesDtos().forEach(mv -> {
            if (CollectionUtils.isEmpty(mv.getChauffeursVehicleDto())) {
                throw new InvalidOperationException("Each vehicle must have at least one driver",
                        ErrorCodes.MISSION_NOT_VALID_OPERATION);
            }
        });
    }

    private void checkOverlappingMissions(MissionDto dto) {
        List<Integer> vehicleIds = extractVehicleIds(dto);
        List<Integer> driverIds = extractDriverIds(dto);

        if (!missionRepository.findConflictingMissions(
                dto.getDateDebut(),
                dto.getDateFin(),
                vehicleIds,
                driverIds).isEmpty()) {
            throw new InvalidOperationException("Vehicles/drivers have overlapping missions",
                    ErrorCodes.MISSION_OVERLAP);
        }
    }

    private Mission createAndSaveMission(MissionDto dto) {
        DeliveryDestinationDto destination = deliveryDestinationService.
                findById(dto.getIdDeliveryDestination());

        Mission mission = MissionDto.toEntity(dto);
        mission.setMissionVehicules(new ArrayList<>());

        dto.getMissionVehiclesDtos().forEach(vehicleDto -> {
           if(vehicleDto==null){
               throw new InvalidOperationException("Null vehicle!!",ErrorCodes.MISSION_NOT_VALID_OPERATION);
           }
            MissionVehicle vehicle = MissionVehicleDto.toEntity(vehicleDto);
            mission.addMissionVehicle(vehicle);
        });
        mission.setEtatMission(EtatMission.PLANNED);
        mission.setDeliveryDestination(DeliveryDestinationDto.toEntity(destination));
        return missionRepository.save(mission);
    }

    @Transactional
    @Override
    public MissionDto save(MissionDto dto) {
        // Validate DTO Early
        validateMissionDto(dto);
        checkMissionAccomplishedStatus(dto);
        checkDto(dto, "Mission Not valid",
                "Mission Not Valide", ErrorCodes.MISSION_NOT_VALID_OPERATION);
        // Check availability (vehicles/drivers + dates)
        checkOverlappingMissions(dto);

        // Save mission
        Mission missionToSave = createAndSaveMission(dto);
        missionToSave.setEtatMission(EtatMission.PLANNED);
        // batch update vehicule/ drivers
        updateVehicleAndDriverStatus(dto,true);
        // Batch create relationships
        createMissionRelationships(dto, missionToSave);
       // save mission-vehicle history and chauffeur_vehicle

/*        if (!dto.getMissionVehiclesDtos().isEmpty()) {
            for (MissionVehicleDto mvehiculeDto : dto.getMissionVehiclesDtos()) {

                    MissionVehicle savedMissionVehicle = new MissionVehicle();
                    final VehicleDto vehiculeDto = checkVehicle(VehicleDto.toEntity(mvehiculeDto.getVehicleDto()));

                    vehiculeDto.setAssigned(true);
                    vehiculeRepository.save(VehicleDto.toEntity(vehiculeDto));
                    savedMissionVehicle.setVehicule(VehicleDto.toEntity(vehiculeDto));
                    savedMissionVehicle.setMission(savedMission);
                    savedMissionVehicle.setAssignedAt(LocalDateTime.now());
                    missionVehicleRepository.save(savedMissionVehicle);

                    if(!mvehiculeDto.getChauffeursVehicleDto().isEmpty()){
                        for(ChauffeurVehicleDto chauffeurByVehicleDto  : mvehiculeDto.getChauffeursVehicleDto()){

                            ChauffeurVehicle chauffeurVehicleToSave = new ChauffeurVehicle();
                            ChauffeurDto chauffeurDto = checkDriver(ChauffeurDto.toEntity(chauffeurByVehicleDto.getChauffeurDto()));
                            chauffeurDto.setAssigned(true);
                            chauffeurRepository.save(ChauffeurDto.toEntity(chauffeurDto));
                            chauffeurVehicleToSave.setChauffeur(ChauffeurDto.toEntity(chauffeurByVehicleDto.getChauffeurDto()));
                            chauffeurVehicleToSave.setMissionVehicle(savedMissionVehicle);
                            chauffeurVehicleToSave.setAssignedAt(LocalDateTime.now());
                            notificationService.sendNotification(
                                    chauffeurVehicleToSave.getChauffeur().getId(),
                                    Notification.builder()
                                            .title("new mission")
                                            .message("new mission message")
                                            .status(NotificationStatus.SEND)
                                            .build());
                            chauffeurVehicleRepository.save(chauffeurVehicleToSave);

                        }
                    }else{
                        throw new InvalidOperationException("At least one driver  must be specified when creating a mission!",
                                ErrorCodes.MISSION_NOT_VALID_OPERATION);
                    }
            }
        }else{
            throw new InvalidOperationException("At least one Vehicle with a driver(s) must be specified when creating a mission!",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }*/


        return MissionDto.fromEntity(missionToSave);

    }

    @Override
    public MissionDto update(Integer id,MissionDto dto) {
        return null;
    }

    private ChauffeurDto checkDriver(Chauffeur driver) {
    return ChauffeurDto.fromEntity( chauffeurRepository.findById(driver.getId()).orElseThrow(()->
            new EntityNotFoundException("Driver not found with id: " + driver.getId(),
                    ErrorCodes.CHAUFFEUR_NOT_FOUND)));
    }

    private VehicleDto checkVehicle(Vehicule vehicule) {
        return VehicleDto.fromEntity(vehiculeRepository.findById(vehicule.getId()).orElseThrow(()->
               new EntityNotFoundException("Vehicle not found with id: " + vehicule.getId(),
                       ErrorCodes.VEHICULE_NOT_FOUND)));

    }

    private void checkOverlapping(MissionDto dto) {
        checkOverlappingVehicles(dto);
        checkOverlappingDrivers(dto);
    }


    @Override
    public List<Integer> checkOverlappingVehicles(MissionDto dto) {
        List<Integer> vehiclesIds= dto.getMissionVehiclesDtos().stream()
                .map(dtoMV->dtoMV.getVehicleDto().getId())
                .collect(Collectors.toList());

        log.error(vehiclesIds.size()+" vehiclesIds");
        List<Integer> unavailableVehiclesIds = missionVehicleService.findUnavailableVehicles(vehiclesIds,dto.getDateDebut(),dto.getDateFin());
        log.error(unavailableVehiclesIds.size()+" unavailableVehiclesIds");

/*        if(!unavailableVehiclesIds.isEmpty()){
            List<String> errorsUnavailableVs = unavailableVehiclesIds.stream().map(String::valueOf).toList();

            throw new InvalidOperationException("Some vehicles are already assigned to another mission.",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION,errorsUnavailableVs);
        }*/
        return unavailableVehiclesIds;
    }
    @Override
    public List<Integer> checkOverlappingDrivers(MissionDto dto) {

        List<Integer> driverIds= dto.getMissionVehiclesDtos()
                .stream().flatMap(dtoMV->dtoMV.getChauffeursVehicleDto().stream())
                .map(cvDto->cvDto.getChauffeurDto().getId())
                .collect(Collectors.toList());
            log.error( driverIds.size()+" driverIds");
        List<Integer> unavailableDriverIds =
                chauffeurVehicleService.findUnavailableDrivers(driverIds,dto.getDateDebut(),dto.getDateFin());
            log.error( unavailableDriverIds.size()+" unavailableDriverIds");

       return unavailableDriverIds;
    }

    @Override
    public MissionDto updateKilometrage(String flag, Integer idMission, double kilometrage) {

        CheckParmasUtil.checkID(idMission,
                "Le parametre ID Mission est null",
                "Impossible de modifier une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        CheckParmasUtil.checkDouble(kilometrage,
                "Le parametre kilometrage de Debut de Mission est null",
                "Impossible de modifier une mission avec kilometrage null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);

        CheckParmasUtil.checkDoubleGTZero(kilometrage,
                "Le parametre kilometrage de Debut de Mission est egale zero",
                "Impossible de modifier une mission avec kilometrage egale à zero",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);


        Optional<Mission> missionOptional = missionRepository.findById(idMission);
        if (missionOptional == null) {
            new EntityNotFoundException("Aucune Mission avec l'id :"
                    + idMission + "n'a ete trouvé dans la base deonnée", ErrorCodes.MISSION_NOT_FOUND);
        }

        Mission mission = missionOptional.get();
        if (flag == "KM_DEBUT") {
            mission.setKmDebut(kilometrage);
        } else {
            mission.setKmFin(kilometrage);
        }
        missionRepository.save(mission);

        return MissionDto.fromEntity(mission);
    }

    @Override
    public MissionDto updateEtatMission(Integer idMission, EtatMission etatMission) {
        CheckParmasUtil.checkID(idMission,
                "Le parametre ID Mission est null",
                "Impossible de modifier une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        CheckParmasUtil.checkStringParma(etatMission.toString(),
                "Le parametre etatMission Mission est null",
                "Impossible de modifier une mission avec un etatMission null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);

        MissionDto missionDto = findById(idMission);
        if (missionDto.isMissionAccomplished()) {
            throw new InvalidOperationException(
                    "Impossible de modifier la commande lorsqu'elle est accompli",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        missionDto.setEtatMission(etatMission);

        return MissionDto.fromEntity(missionRepository.save(MissionDto.toEntity(missionDto)));
    }

    @Override
    public MissionDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Mission est null",
                "Impossible de trouver une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        Optional<Mission> missionOp = missionRepository.findById(id);

        MissionDto missionDto= Optional.of(MissionDto.fromEntity(missionOp.get()))
                .orElseThrow(
                        () -> new EntityNotFoundException("Aucune Mission avec l'id :"
                                + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.MISSION_NOT_FOUND)
                );

        setCreatedByUser(missionDto);
        return missionDto;
    }

    @Override
    public List<MissionDto> findAllByDate(Instant dateMission) {
        CheckParmasUtil.checkDate(dateMission,
                "ERREUR: Le parametre date_mission est null",
                "Impossible de trouver la liste des missions par date  avec une date null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);


        return missionRepository.findAllByDateDebut(dateMission).stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MissionResponse> findAll() {
        return missionRepository.findAll().stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedMissionResponse)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "l'ID Mission  à supprimer est null",
                "Impossible de supprimer une Mission  avec un ID null", ErrorCodes.MISSION_NOT_VALID_OPERATION);
        Mission mission = missionRepository.findById(id).get();
        if (id != null && MissionDto.fromEntity(mission).isMissionAccomplished()) {
            throw new InvalidOperationException("Impossible de Supprimer la mission lorsqu'elle est accompli",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        // First, delete related records in the mission_vehicle table

        etapeRepository.deleteByMissionId(id);

        List<MissionVehicleDto> missionVehicleDtos = new ArrayList<>();
        missionVehicleDtos= missionVehicleService.findByMissionId(id);
        for (MissionVehicleDto missionVehicleDto : missionVehicleDtos) {
                chauffeurVehicleService.deleteByMissionVehicleId(missionVehicleDto.getId());
        }
        missionVehicleRepository.deleteByMissionId(id);
        missionRepository.deleteById(id);
    }

    @Override
    public PageResponse<MissionDto> findAllMissions(int page, int size) {

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Mission> missions = missionRepository.findAllMissions(pageable);

        List<MissionDto> missionsDto = missions.stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                missionsDto,
                missions.getNumber(),
                missions.getSize(),
                missions.getTotalElements(),
                missions.getTotalPages(),
                missions.isFirst(),
                missions.isLast()
        );
    }

    @Override
    public PageResponse<MissionDto> findAllMissionsByDriverId(int page, int size, Authentication connectedUser) {
            Utilisateur user = ((Utilisateur) connectedUser.getPrincipal());
            Pageable pageable = PageRequest.of(page,size,Sort.by("createdAt").descending());
            Page<Mission> missions = missionRepository.findAll(MissionSpecification.withChauffeurId(user.getId()),pageable);

        List<MissionDto> missionsDto = missions.stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                missionsDto,
                missions.getNumber(),
                missions.getSize(),
                missions.getTotalElements(),
                missions.getTotalPages(),
                missions.isFirst(),
                missions.isLast()
        );
    }

    @Override
    public List<MissionResponse> findAllMissionsByDriver(Authentication connectedUser) {
        Utilisateur user = ((Utilisateur) connectedUser.getPrincipal());
        if(user==null){
            throw  new InvalidOperationException("Not found user :",ErrorCodes.CHAUFFEUR_NOT_FOUND);
        }
        List<Mission> missionByDriver= missionRepository.findAll(MissionSpecification.withChauffeurId(user.getId()));

        return missionByDriver.stream()
        .map(MissionResponse::fromEntity)
                .toList();
    }

    private void checkDto(MissionDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = MissionValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error(msgLog, dto);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }


    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(MissionDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }    private void setCreatedByUserResponse(MissionResponse response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private MissionDto updatedResponse(Mission mission) {
        MissionDto mDto=MissionDto.fromEntity(mission);
        setCreatedByUser(mDto);
        return mDto;
    }
    private MissionResponse updatedMissionResponse(Mission mission) {
        MissionResponse mDto=MissionResponse.fromEntity(mission);
        setCreatedByUserResponse(mDto);
        return mDto;
    }
}
