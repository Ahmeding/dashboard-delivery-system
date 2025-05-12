package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.DriverExperience;
import com.hitecart.deliverysystem.dao.IChauffeurVehicleRepository;
import com.hitecart.deliverysystem.dao.MissionVehicleRepository;
import com.hitecart.deliverysystem.utils.TimeSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityManager {
    private final IChauffeurVehicleRepository chauffeurVehicleRepository;
    private final MissionVehicleRepository missionVehicleRepository;
    // Track vehicle time slots (REQUIRED)
    private final Map<Vehicule, List<TimeSlot>> vehicleAvailability = new ConcurrentHashMap<>();
    private Map<Chauffeur, List<TimeSlot>> driverAvailability = new HashMap<>();

    public void blockResources(Vehicule vehicle, List<Chauffeur> drivers, TimeSlot slot) {
        log.info("Blocking resources for mission {} to {}", slot.getStart(), slot.getEnd());

        // Block vehicle availability
        vehicleAvailability.compute(vehicle, (v, currentSlots) -> {
            if (currentSlots == null) return Collections.emptyList();
            return currentSlots.stream()
                    .flatMap(ts -> ts.subtract(slot).stream())
                    .collect(Collectors.toList());
        });
        log.debug("Updated vehicle {} availability", vehicle.getId());

        // Block driver availability
        drivers.forEach(driver ->
                driverAvailability.compute(driver, (d, currentSlots) -> {
                    if (currentSlots == null) return Collections.emptyList();
                    return currentSlots.stream()
                            .flatMap(ts -> ts.subtract(slot).stream())
                            .collect(Collectors.toList());
                })
        );
        log.debug("Updated availability for {} drivers", drivers.size());
    }

    public void
    initializeDriverAvailability(List<Chauffeur> drivers, LocalDateTime start, LocalDateTime end) {

            // Step 1: Fetch all assignments for all drivers in one query
            List<ChauffeurVehicle> allAssignments = chauffeurVehicleRepository
                    .findChauffeurVehiclesByDriversAndDateRange(drivers, start, end);

            // Step 2: Group assignments by driver (KEY FIX!)
            Map<Chauffeur, List<ChauffeurVehicle>>
                    assignmentsByDriver = allAssignments.stream()
                    .collect(Collectors
                            .groupingBy(ChauffeurVehicle::getChauffeur));

        drivers.forEach(driver -> {
                    List<TimeSlot> availability =
                            new ArrayList<>(List.of(new TimeSlot(start, end)));

            // Subtract existing assignments
            availability = subtractAssignments(
                    availability,
                    assignmentsByDriver.getOrDefault(driver,
                            Collections.emptyList())
            );
            // Subtract cooldown periods
            availability = subtractCooldownPeriods(driver, availability);
            driverAvailability.put(driver, availability);

                });
            // Step 3: Calculate availability for each driver
            this.driverAvailability = drivers.parallelStream()
                    .collect(Collectors.toMap(
                            driver -> driver,
                            driver -> subtractAssignments(
                                    new TimeSlot(start, end),
                                    assignmentsByDriver.getOrDefault(driver, Collections.emptyList())
                            )
                    ));
        }

    public List<Chauffeur> getAvailableDrivers(TimeSlot slot,
                                               List<DriverExperience> requiredLevels) {
        return driverAvailability.entrySet().stream()
                .filter(entry ->{
                    Chauffeur driver = entry.getKey();
                    boolean levelMatch = requiredLevels.contains(driver.getExperienceLevel());
                            boolean slotAvailable = entry.getValue().stream()
                                    .anyMatch(availableSlot -> availableSlot.covers(slot));
                            log.debug("Driver {}: Level={} SlotAvailable={}",
                                    driver.getId(), driver.getExperienceLevel(), slotAvailable);

                            return levelMatch && slotAvailable;
                        })

                        .sorted((a,b)->{
                // Prioritize experts first
            if(a.getKey().getExperienceLevel() == DriverExperience.EXPERT) return -1;
            if (b.getKey().getExperienceLevel() == DriverExperience.EXPERT) return 1;
            return 0;
        }).map(Map.Entry::getKey).collect(Collectors.toList());
    }
    // ========== NEW FUNCTIONALITY ========== //

    public void blockDriverTime(Chauffeur driver, TimeSlot missionSlot) {
        driverAvailability.computeIfPresent(driver, (k, currentSlots) ->
                currentSlots.stream()
                        .flatMap(slot -> slot.subtract(missionSlot).stream())
                        .collect(Collectors.toList())
        );
        log.debug("Blocked {} for {}", driver.getId(), missionSlot);
    }
    private List<TimeSlot> subtractCooldownPeriods(Chauffeur driver, List<TimeSlot> availability) {
        if (driver.getNextAvailableTime() != null) {
            TimeSlot cooldown = new TimeSlot(
                    driver.getNextAvailableTime().minusHours(2), // Cooldown buffer
                    driver.getNextAvailableTime()
            );
            return availability.stream()
                    .flatMap(slot -> slot.subtract(cooldown).stream())
                    .collect(Collectors.toList());
        }
        return availability;
    }


    // ========== IMPROVED SAFETY CHECKS ========== //

    private List<TimeSlot> subtractAssignments(List<TimeSlot> availability,
                                               List<ChauffeurVehicle> assignments) {
        for (ChauffeurVehicle assignment : assignments) {
            TimeSlot occupied = extractOccupiedTimeSlot(assignment);
            if (occupied == null) continue;

            availability = availability.stream()

                    .flatMap(slot -> {
                        List<TimeSlot> remaining = slot.subtract(occupied);
                        log.trace("Split {} into {} slots", slot, remaining.size());
                        return remaining.stream();
                    })
                    .filter(ts -> {
                        // Explicit null check first
                        if (ts == null || ts.getStart() == null || ts.getEnd() == null) {
                            log.warn("Invalid TimeSlot detected: {}", ts);
                            return false;
                        }
                        long minutes = ChronoUnit.MINUTES.between(ts.getStart(), ts.getEnd());
                        return minutes > 15;
                    }) // Ignore tiny fragments
                    .collect(Collectors.toList());
        }
        return availability;
    }

    private List<TimeSlot> subtractAssignments(
            TimeSlot fullSlot,
            List<? extends AbstractEntity> assignments) {

        List<TimeSlot> availableSlots = new ArrayList<>(
                Collections.singletonList(fullSlot));
        log.debug("Initial full slot: {}", fullSlot);

        for (AbstractEntity assignment : assignments) {
            TimeSlot occupied = extractOccupiedTimeSlot(assignment);
            if (occupied == null) {
                log.warn("Skipping invalid assignment: {}", assignment.getId());
                continue;
            }

            availableSlots = availableSlots.stream()
                    .flatMap(slot -> slot.subtract(occupied).stream())
                    .filter(ts->{
                        long minutes
                    = ChronoUnit.MINUTES.between(ts.getStart(), ts.getEnd());
                        return minutes>15;
                    })
                    .collect(Collectors.toList());
        }
        log.debug("Final available slots: {}", availableSlots);
        return availableSlots;
    }
    private List<TimeSlot> mergeAdjacentSlots(List<TimeSlot> slots) {
        List<TimeSlot> merged = new ArrayList<>();
        slots.sort(Comparator.comparing(TimeSlot::getStart));

        for (TimeSlot slot : slots) {
            if (merged.isEmpty()) {
                merged.add(slot);
            } else {
                TimeSlot last = merged.get(merged.size() - 1);
                if (last.getEnd().equals(slot.getStart())) {
                    merged.set(merged.size() - 1,
                            new TimeSlot(last.getStart(), slot.getEnd()));
                } else {
                    merged.add(slot);
                }
            }
        }
        return merged;
    }
    /**
     * Extracts the occupied TimeSlot from an assignment, or returns null if invalid.
     */
    private TimeSlot extractOccupiedTimeSlot(AbstractEntity assignment) {
        try {
            if (assignment instanceof ChauffeurVehicle) {
                ChauffeurVehicle cv = (ChauffeurVehicle) assignment;
                MissionVehicle mv = cv.getMissionVehicle();
                if (mv == null || mv.getMission() == null) return null;
                return new TimeSlot(mv.getMission().getDateDebut(), mv.getMission().getDateFin());
            }
            else if (assignment instanceof MissionVehicle) {
                MissionVehicle mv = (MissionVehicle) assignment;
                if (mv.getMission() == null) {
                    log.error("MissionVehicle {} has no mission", mv.getId());
                    return null;
                }
                return new TimeSlot(
                        mv.getMission().getDateDebut(),
                        mv.getMission().getDateFin());
            } else {
                throw new IllegalArgumentException("Unsupported assignment type: " + assignment.getClass());
            }
        } catch (NullPointerException e) {
            log.error("Failed to extract TimeSlot from assignment {}", assignment.getId(), e);
            return null;
        }
    }


    public boolean isAvailable(Chauffeur driver, TimeSlot missionSlot) {
        // Check 1: Driver's existing assignments don't overlap with mission slot
        // Combined check: availability slots + cooldown time

        boolean slotAvailable = driverAvailability.getOrDefault(driver, List.of()).stream()
                .anyMatch(slot -> slot.covers(missionSlot));

        boolean cooldownPassed = driver.getNextAvailableTime() == null ||
                driver.getNextAvailableTime().isBefore(missionSlot.getStart());

        log.debug("Driver {}: SlotAvailable={}, CooldownOK={}",
                driver.getId(), slotAvailable, cooldownPassed);

        return slotAvailable && cooldownPassed;

      /*  boolean hasNoOverlap = driver.getAssignments().stream()
                .map(ChauffeurVehicle::getMissionVehicle)
                .map(MissionVehicle::getMission)
                .noneMatch(mission ->
                        new TimeSlot(mission.getDateDebut(), mission.getDateFin())
                                .overlapsWith(missionSlot)
                );
        // Check 2: Driver's next available time (cooldown period)
        boolean pastCooldown = driver.getNextAvailableTime() == null ||
                driver.getNextAvailableTime().isBefore(missionSlot.getStart());
        log.debug("Driver {}: Overlap={}, Cooldown={}",
                driver.getId(), hasNoOverlap ? "No" : "Yes", pastCooldown ? "OK" : "Blocked");
        return hasNoOverlap && pastCooldown;*/
    }

    // Optimized vehicle availability initialization
    public Map<Vehicule, List<TimeSlot>> initializeVehicleAvailability(List<Vehicule> vehicles,
                                                                       LocalDateTime start,
                                                                       LocalDateTime end) {
        // Single query to fetch all assignments for all vehicles
        List<MissionVehicle> allAssignments = missionVehicleRepository.findByVehiculesAndTime(vehicles, start, end);

        // Group assignments by vehicle
        Map<Vehicule, List<MissionVehicle>> assignmentsByVehicle = allAssignments.stream()
                .collect(Collectors.groupingBy(MissionVehicle::getVehicule));

        // Process vehicles in parallel
        return vehicles.parallelStream()
                .peek(vehicle -> {
                    List<MissionVehicle> assignments = assignmentsByVehicle.getOrDefault(vehicle, Collections.emptyList());
                    log.debug("Vehicle {} has {} assignments", vehicle.getId(), assignments.size());
                })
                .collect(Collectors.toMap(
                        vehicle -> vehicle,
                        vehicle -> {

                            List<MissionVehicle> assignments = assignmentsByVehicle.getOrDefault(vehicle, Collections.emptyList());
                            List<TimeSlot> availability = subtractAssignments(new TimeSlot(start, end), assignments);
                            log.debug("Vehicle {} availability after subtraction: {}", vehicle.getId(), availability);
                            return availability;
                        }
                ));
    }
    // Update availability after assignment
    public void markDriverBusy(Chauffeur driver, TimeSlot slot) {
        driverAvailability.computeIfPresent(driver, (k, slots) ->
                slots.stream()
                        .flatMap(available -> available.subtract(slot).stream())
                        .collect(Collectors.toList())
        );
    }





}
