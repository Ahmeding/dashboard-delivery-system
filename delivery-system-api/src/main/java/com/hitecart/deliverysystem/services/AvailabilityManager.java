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
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityManager {
    private final IChauffeurVehicleRepository chauffeurVehicleRepository;
    private final MissionVehicleRepository missionVehicleRepository;

    private Map<Chauffeur, List<TimeSlot>> driverAvailability = new HashMap<>();



    public void
    initializeDriverAvailability(List<Chauffeur> drivers, LocalDateTime start, LocalDateTime end) {

            // Step 1: Fetch all assignments for all drivers in one query
            List<ChauffeurVehicle> allAssignments = chauffeurVehicleRepository
                    .findChauffeurVehiclesByDriversAndDateRange(drivers, start, end);

            // Step 2: Group assignments by driver (KEY FIX!)
            Map<Chauffeur, List<ChauffeurVehicle>> assignmentsByDriver = allAssignments.stream()
                    .collect(Collectors.groupingBy(ChauffeurVehicle::getChauffeur));

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


    public List<Chauffeur> getAvailableDrivers(TimeSlot slot,
                                               List<DriverExperience> requiredLevels) {
        return driverAvailability.entrySet().stream()
                .peek(entry -> {
                            System.out.println("Checking driver: " + entry.getKey().getId());
                            System.out.println("Experience: " + entry.getKey().getExperienceLevel());
                            System.out.println("Available slots: " + entry.getValue());
                        })
                .filter(entry ->
                        // Check experience level
                        requiredLevels.contains(entry.getKey().getExperienceLevel()) &&
                                // Check time slot availability
                                entry.getValue().stream().anyMatch(availableSlot ->{
                                        boolean covers = availableSlot.covers(slot);
                                            System.out.println("Slot covers? " + covers);
                                            return covers;
                                        }
                                        )
                )
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Update availability after assignment
    public void markDriverBusy(Chauffeur driver, TimeSlot slot) {
        driverAvailability.computeIfPresent(driver, (k, slots) ->
                slots.stream()
                        .flatMap(available -> available.subtract(slot).stream())
                        .collect(Collectors.toList())
        );
    }

private List<TimeSlot> subtractAssignments(
        TimeSlot fullSlot,
        List<? extends AbstractEntity> assignments) {
    List<TimeSlot> availableSlots = new ArrayList<>(Collections.singletonList(fullSlot));
    log.debug("Initial full slot: {}", fullSlot);
    for (AbstractEntity assignment : assignments) {
        TimeSlot occupied = extractOccupiedTimeSlot(assignment);
        log.debug("Subtracting occupied slot: {}", occupied);
        if (occupied == null) {
            log.warn("Skipping invalid assignment: {}", assignment.getId());
            continue;
        }

        availableSlots = availableSlots.stream()
                .flatMap(slot -> slot.subtract(occupied).stream())
                .collect(Collectors.toList());
    }
    log.debug("Final available slots: {}", availableSlots);
    return availableSlots;
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
            } else if (assignment instanceof MissionVehicle) {
                MissionVehicle mv = (MissionVehicle) assignment;
                if (mv.getMission() == null) return null;
                return new TimeSlot(mv.getMission().getDateDebut(), mv.getMission().getDateFin());
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

        boolean hasNoOverlap = driver.getAssignments().stream()
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
        return hasNoOverlap && pastCooldown;
    }


}
