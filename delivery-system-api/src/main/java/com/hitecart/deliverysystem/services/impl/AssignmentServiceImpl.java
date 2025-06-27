package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.AssignmentStatus;
import com.hitecart.deliverysystem.model.enums.DriverExperience;
import com.hitecart.deliverysystem.model.enums.RouteDifficulty;
import com.hitecart.deliverysystem.services.*;
import com.hitecart.deliverysystem.utils.AssignmentResult;
import com.hitecart.deliverysystem.utils.TimeSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
@Slf4j
public class AssignmentServiceImpl {
/*

    private final IDriverStatsService driverStatsService;
    private final AvailabilityManager availabilityManager;
    private final DriverVehicleRepository chauffeurVehicleRepository;
    private final MissionVehicleRepository missionVehicleRepository;
    private final MissionRepository missionRepository;
    private final DriverRepository chauffeurRepository;
    private final VehicleRepository vehiculeRepository;

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
    private Driver findExpertDriver(List<Driver> availableDrivers,
                                    Map<Driver, Integer> weeklyAssignmentCounts) {
        return availableDrivers.stream()
                .filter(d -> d.getExperienceLevel() == DriverExperience.EXPERT)
                .min(Comparator.comparingInt(d ->
                        weeklyAssignmentCounts
                                .getOrDefault(d,0)))
                .orElse(null);
    }

    // Helper method to find helper driver (non-EXPERT with least missions)
    private Driver findHelperDriver(List<Driver> availableDrivers,
                                    Map<Driver, Integer> weeklyAssignmentCounts) {
        return availableDrivers.stream()
                .filter(d -> d.getExperienceLevel() != DriverExperience.EXPERT)
                .min(Comparator.comparingInt(d ->
                        weeklyAssignmentCounts.getOrDefault(d, 0))
                )
                .orElse(null);
    }

    // Helper method to find single best driver (least missions overall)
    private List<Driver> findSingleDriver(
            List<Driver> availableDrivers) {
        return availableDrivers.stream()
                .filter(d -> d.getExperienceLevel() != DriverExperience.EXPERT)
                .min(Comparator.comparingInt(d -> d.getStats().getTotalMissions()))
                .map(List::of)
                .orElse(Collections.emptyList());
    }


    // Helper method to update stats and collect drivers
    private void updateDriverStats(
            List<Driver> drivers,
            Mission mission,
            Set<Driver> updatedDrivers
    ) {


        double distance = mission.getDeliveryDestination()
                .getDeliveryService().getDistance();

        drivers.forEach(driver -> {
            driver.ensureStatsInitialized();  // Critical safety check
            // Update next available time
            driver.setNextAvailableTime(
                    calculateNextAvailableTime(mission.getStartDate(), distance)
            );
            // Update stats
            DriverStats stats = driver.getStats();
            try{
                if (mission.getDeliveryDestination().getDeliveryService().getDifficulty() == RouteDifficulty.HARD) {
                    stats.incrementLongTrips();
                } else {
                    stats.incrementStandardTrips();
                }
            }catch (Exception e){
                log.error("Failed to update stats for driver {}: {}",
                        driver.getId(), e.getMessage());
                // Create emergency stats record
                DriverStats emergencyStats = new DriverStats();
                emergencyStats.setDriver(driver);
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
        Map<Driver, Integer> weeklyAssignmentCounts = new HashMap<>(); // Track weekly assignments per driver

        Map<Vehicle, Integer> weeklyVehicleAssignmentCounts = new HashMap<>(); // Track weekly assignments per vehicle
        */
/* -------------------------------------------
         * 1. Define Weekly Timeframe
         * ------------------------------------------- *//*

        // Get current week: Sunday 00:00 to Saturday 23:59
        LocalDateTime weekStart = LocalDateTime.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                .truncatedTo(ChronoUnit.DAYS);

        LocalDateTime weekEnd = weekStart.plusDays(6)
                .with(LocalTime.MAX);
        // Initialize a Set to track updated drivers
        Set<Driver> updatedDrivers = new HashSet<>();
        */
/* -------------------------------------------
         * 2. Fetch & Prepare Missions
         * ------------------------------------------- *//*

        // Get missions occurring this week
        List<Mission> missions = missionRepository.findByDateRange(weekStart, weekEnd);
        List<Mission> failedMissions = new ArrayList<>();
        long expiredCount = missions.stream()
                .filter(m -> m.getStartDate().isBefore(LocalDateTime.now()))
                .count();

        log.info("Filtered {} expired missions", expiredCount);

        missions.removeIf(mission ->
                mission.getEndDate().isAfter(weekEnd) ||
                        mission.getStartDate().isBefore(weekStart) ||
                        mission.getStartDate().isBefore(LocalDateTime.now())
        );
        // Auto-calculate difficulty for each mission
        missions.forEach(mission -> {
            double distance = mission.getDeliveryDestination()
                    .getDeliveryService()
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

        */
/* -------------------------------------------
         * 3. Prepare Resources
         * ------------------------------------------- *//*

        chauffeurRepository.findAll()
                .forEach(driverStatsService::createStatsForDriver);

        List<Driver> allDrivers = chauffeurRepository.findAll();
        List<Vehicle> allVehicles = vehiculeRepository.findAll();

        // Weekly rotation shuffle for fairness
        long weekNumber = ChronoUnit.WEEKS.between(LocalDate.of(
                2025, 1, 1), LocalDate.now());
        Collections.shuffle(allDrivers, new Random(weekNumber));

        // Initialize tracking systems

        Map<Vehicle, List<TimeSlot>> vehicleAvailability =
                availabilityManager.initializeVehicleAvailability(
                        allVehicles,
                        weekStart, weekEnd);

        availabilityManager.initializeDriverAvailability(
                allDrivers, weekStart, weekEnd);

        */
/* -------------------------------------------
         * 4. Assignment Process
         * ------------------------------------------- *//*


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
            Vehicle vehicle = findSuitableVehicle(mission,vehicleAvailability, weeklyVehicleAssignmentCounts);
            if (vehicle == null) {
                failedMissions.add(mission);
                continue;
            }

            // --------------------------
            // Driver Selection
            // --------------------------


            List<DriverExperience> requiredLevels = mission.getDifficulty() == RouteDifficulty.HARD ?
                    List.of(DriverExperience.EXPERT, DriverExperience.INTERMEDIATE, DriverExperience.JUNIOR) :
                    List.of(DriverExperience.INTERMEDIATE, DriverExperience.JUNIOR);

            List<Driver> availableDrivers =
                    availabilityManager.getAvailableDrivers(
                                    missionSlot,
                                    requiredLevels).stream()
                            .filter(driver->(driver.getNextAvailableTime()
                                    == null ||
                                    driver.getNextAvailableTime()
                                            .isBefore(missionSlot.getStart()))&&
                                    availabilityManager.isAvailable(driver,missionSlot))
                            .collect(Collectors.toList());

*/
/*             availableDrivers = availableDrivers.stream()
                    .filter(driver ->
                            (driver.getNextAvailableTime() == null ||
                                    driver.getNextAvailableTime().isBefore(missionSlot.getStart())) &&
                                    availabilityManager.isAvailable(driver, missionSlot)
                    )
                    .collect(Collectors.toList());*//*




            List<Driver> drivers = new ArrayList<>();
            drivers = this.assignDrivers(
                    availableDrivers,
                    mission,
                    weeklyAssignmentCounts,
                    missions, // Pass all missions for difficulty check
                    availabilityManager,
                    missionSlot);


            drivers.forEach(driver ->
                    weeklyAssignmentCounts.put(driver, weeklyAssignmentCounts.getOrDefault(driver, 0) + 1)
            );
            if (drivers.isEmpty()) {
                failedMissions.add(mission);
                vehicleAvailability.get(vehicle).add(missionSlot); // Return slot
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

        */
/* -------------------------------------------
         * 6. Finalize & Save Results
         * ------------------------------------------- *//*


        // Before saving drivers
        updatedDrivers.forEach(driver -> {
            if (driver.getStats() == null) {
                log.warn("Emergency stats creation for driver {}", driver.getId());
                driver.ensureStatsInitialized();
            }
        });
        // Batch save all updated drivers in one query
        chauffeurRepository.saveAll(updatedDrivers);
        */
/* -------------------------------------------
         * 7. Finalize & Save Results
         * ------------------------------------------- *//*

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
            Vehicle vehicle,
            TimeSlot missionSlot,
            Map<Vehicle, List<TimeSlot>> availability) {

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
            Set<Driver> updatedDrivers,
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

    */
/**
     * Finds vehicle with sufficient capacity and availability
     *//*

    private Vehicle findSuitableVehicle(Mission mission, Map<Vehicle,
            List<TimeSlot>> availability, Map<Vehicle, Integer> weeklyVehicleAssignmentCounts) {

        TimeSlot missionSlot = new TimeSlot(mission.getDateDebut(), mission.getDateFin());
        log.debug("Finding vehicle for mission ID {} (Volume: {}, Time: {} to {})",
                mission.getId(), mission.getVolume(), missionSlot.getStart(), missionSlot.getEnd());

        return availability.entrySet().stream()
                .peek(entry -> {
                    Vehicle v = entry.getKey();
                    log.debug("Checking vehicle {} (Volume: {})",
                            entry.getKey().getId(), entry.getKey().getCapacity());
                    log.debug("Available slots for vehicle {}: {}",
                            entry.getKey().getId(), entry.getValue());
                    log.debug("Vehicle {} availability slots: {}",
                            entry.getKey().getId(), entry.getValue());
                    log.debug("Inspecting vehicle {} (Vol: {}, Type: {}, Active: {})",
                            v.getId(), v.getCapacity(), v.getVehicleType(), v.getIsActive());
                })
                // Enhanced filtering with failure reasons
                .filter(entry -> {
                    Vehicle v = entry.getKey();
                    // Check capacity
                    boolean hasCapacity =
                            v.getCapacity()
                                    .compareTo(mission.getVolume()) >= 0;

                    log.debug("Vehicle {}: hasCapacity={} (Mission requires {}, Vehicle has {})",
                            entry.getKey().getId(), hasCapacity,
                            mission.getVolume(), entry.getKey().getCapacity());

                    // Check availability
                    boolean availableSlot  = entry.getValue().stream()
                            .anyMatch(slot -> slot.covers(missionSlot));

                    if (!hasCapacity) {
                        log.trace("Reject vehicle {}: Insufficient capacity ({} < {})",
                                v.getId(), v.getCapacity(), mission.getVolume());
                    }
                    if (!availableSlot) {
                        log.trace("Reject vehicle {}: No compatible time slots", v.getId());
                    }

                    log.debug("Vehicle {}: Capacity={}/{} Available={}",
                            entry.getKey().getId(),
                            entry.getKey().getCapacity(),
                            mission.getVolume(),
                            availableSlot ? "Yes" : "No");
                    return hasCapacity && availableSlot;
                })
                // Better selection logic with prioritization
                .min(Comparator.comparingInt(
                                (Map.Entry<Vehicle, List<TimeSlot>> entry) ->
                                        weeklyVehicleAssignmentCounts
                                                .getOrDefault(entry.getKey(),0)
                        ).thenComparing(entry->entry.getKey().getCapacity())
                        .thenComparing(entry->entry.getValue().size(), // Secondary: Most available slots
                                Comparator.reverseOrder()
                        ))
                .map(entry -> {
                    Vehicle selectedVehicle = entry.getKey();
                    log.info("Selected vehicle {} for mission {} (Vol: {}, Slots remaining: {})",
                            selectedVehicle.getId(), mission.getId(),
                            selectedVehicle.getCapacity(), entry.getValue().size());
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

    */
/**
     *
     * @param availableDrivers
     * @param mission
     * @param weeklyAssignmentCounts
     * @param allMissions
     * @param availabilityManager
     * @param missionSlot
     * @return
     *//*

    private List<Driver> assignDrivers(
            List<Driver> availableDrivers,
            Mission mission,
            Map<Driver, Integer> weeklyAssignmentCounts,
            List<Mission> allMissions, // Pass the full mission list
            AvailabilityManager availabilityManager,
            TimeSlot missionSlot ) {

        List<Driver> drivers = new ArrayList<>();

        log.debug("Starting driver assignment for mission {} (Difficulty: {})",
                mission.getId(), mission.getDifficulty());
        if (mission.getDifficulty() == RouteDifficulty.HARD) {
            log.debug("Processing difficult mission {}", mission.getId());


            // Log initial available drivers
            logAvailableDrivers(availableDrivers, "Initial available drivers for difficult mission");

            // Try expert + helper first
            Driver expert = findExpertDriver(availableDrivers,weeklyAssignmentCounts);
            Driver helper = findHelperDriver(availableDrivers,weeklyAssignmentCounts);

            if (expert != null && helper != null) {
                drivers.add(expert);
                drivers.add(helper);
                log.info("Assigned expert+helper pair: {} and {} to mission {}",
                        expert.getId(), helper.getId(), mission.getId());
            } else if (expert != null) {
                // Expert + any available non-expert
                Driver fallbackDriver = availableDrivers
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

                List<Driver> intermediates= availableDrivers.stream()
                        .filter(d->d.getExperienceLevel()
                                == DriverExperience.INTERMEDIATE)
                        .collect(Collectors.toList());

                List<Driver> juniors = availableDrivers.stream()
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
                    Driver intermediate = intermediates.stream()
                            .min(Comparator.comparingInt(d ->
                                    weeklyAssignmentCounts.getOrDefault(d,0)))
                            .orElse(null);

                    Driver junior = juniors.stream()
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
            List<Driver> nonExperts = availableDrivers.stream()
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
                        .filter(m -> m.getDifficulty() == RouteDifficulty.HARD)
                        .allMatch(m -> m.getAssignmentStatus() == AssignmentStatus.ASSIGNED);

                if (allDifficultCovered) {
                    // Get available experts for this slot
                    List<Driver> experts = availabilityManager.getAvailableDrivers(
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
                            .map(List::of)  // Now working with Optional<Driver>
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
        if(mission.getDifficulty() == RouteDifficulty.HARD
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
    private void logAvailableDrivers(List<Driver> drivers, String context) {
        log.debug("{} ({} drivers): {}", context, drivers.size(), drivers.stream()
                .map(d -> d.getId() + "(" + d.getExperienceLevel() + ")")
                .collect(Collectors.joining(", ")));
    }

    */
/**
     *
     * @param assignments
     *//*

    private void saveAssignments(List<AssignmentResult> assignments) {
        List<MissionVehicle> missionVehicles = new ArrayList<>();
        List<DriverVehicle> driverVehicles = new ArrayList<>();

        for (AssignmentResult assignment : assignments) {
            // Create MissionVehicle
            MissionVehicle mv = MissionVehicle.builder()
                    .mission(assignment.getMission())
                    .vehicle(assignment.getVehicle())
                    .assignedAt(LocalDateTime.now())
                    .build();
            missionVehicles.add(mv);

            // Create DriverVehicle links
            for (Driver driver : assignment.getDrivers()) {
                DriverVehicle cv = DriverVehicle.builder()
                        .missionVehicle(mv)
                        .driver(driver)
                        .assignedAt(LocalDateTime.now())
                        .build();
                driverVehicles.add(cv);
            }
        }

        // Batch save to database
        missionVehicleRepository.saveAll(missionVehicles);
        chauffeurVehicleRepository.saveAll(driverVehicles);
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
                .map(Driver::getId)
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
                .filter(m -> m.getDifficulty() == RouteDifficulty.HARD)
                .count();

        log.info("Weekly Assignment Requirements:");
        log.info("Total missions: {}", missions.size());
        log.info("Difficult missions requiring 2 drivers: {}", difficultMissions);
        log.info("Standard missions requiring 1 driver: {}", missions.size() - difficultMissions);
        log.info("Total vehicles needed: {}", missions.size());
    }
*/

}
