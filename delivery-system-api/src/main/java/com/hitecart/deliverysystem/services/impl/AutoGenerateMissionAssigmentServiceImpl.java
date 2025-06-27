package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.services.AutoGenerateMissionAssigmentService;
import com.hitecart.deliverysystem.services.AvailabilityManager;
import com.hitecart.deliverysystem.utils.AssignmentResult;
import com.hitecart.deliverysystem.utils.TimeSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoGenerateMissionAssigmentServiceImpl implements AutoGenerateMissionAssigmentService {
    private final DeliveryPointRepository deliveryPointRepository;
    private final MissionRepository missionRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final MissionVehicleRepository missionVehicleRepository;
    private final DriverVehicleRepository driverVehicleRepository;

    /**
     *
     */
    @Transactional
    @Override
    public void generateWeeklyAssignments() {
        // 1. Define the week (Monday to Sunday)
        LocalDateTime weekStart = LocalDateTime.now().with(DayOfWeek.SUNDAY).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime weekEnd = weekStart.plusDays(6);

        // 2. Fetch missions for the week
        List<Mission> missions = missionRepository.findMissionsWithVehiclesAndDrivers(weekStart, weekEnd);
        if (missions.isEmpty()) {
            log.info("No missions this week!");
            return;
        }

        // 3. Sort missions by longest duration first
        missions.sort(Comparator.comparingLong(
                (Mission m) -> Duration.between( m.getStartDate(), m.getEndDate()).toMinutes()
        ).reversed());

        // 4. Fetch all drivers and vehicles
        List<Driver> drivers = driverRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();

        // 5. Initialize availability and mission counters
        AvailabilityManager availabilityManager = new AvailabilityManager(driverVehicleRepository, missionVehicleRepository);
        //Map<Driver, List<TimeSlot>> driverAvailability = availabilityManager.initializeDriverAvailability(drivers, weekStart, weekEnd);
        Map<Vehicle, List<TimeSlot>> vehicleAvailability = availabilityManager.initializeVehicleAvailability(vehicles, weekStart, weekEnd);

        // 6. Initialize mission counter for each driver (NEW)
        Map<Driver, Integer> chauffeurMissionCount = drivers.stream()
                .collect(Collectors.toMap(driver -> driver, driver -> 0));

        // 7. Shuffle drivers weekly for fairness
        long weekNumber = ChronoUnit.WEEKS.between(LocalDate.of(2023, 1, 1).atStartOfDay(), weekStart);
        Collections.shuffle(drivers, new Random(weekNumber));

        // 8. Prepare assignment tracking
        List<AssignmentResult> assignments = new ArrayList<>();
        List<Mission> unassignedMissions = new ArrayList<>();

        for (Mission mission : missions) {
            TimeSlot missionSlot = new TimeSlot(mission.getStartDate(),
                    mission.getEndDate());


            // 9. Find suitable vehicle
            Map<Vehicle, Integer> vehicleAssignmentCounts = new HashMap<>(); // Track weekly assignments per vehicle
            Vehicle vehicle = findSuitableVehicle(mission, vehicleAvailability,
                    vehicleAssignmentCounts);
            // 10. Find available driver with fewest missions (UPDATED)
            //Driver driver = findAvailableDriver(missionSlot, driverAvailability, chauffeurMissionCount);
      /*     if (vehicle == null || driver == null) {
                unassignedMissions.add(mission);
                continue;
            }*/
            // 11. Create and track assignment
            // AssignmentResult assignment = createAssignment(mission, vehicle, driver);
            //assignments.add(assignment);

            // 12. Update availability and mission count (NEW)
          /*  updateTimeSlots(vehicleAvailability, vehicle, missionSlot);
            updateTimeSlots(driverAvailability, driver, missionSlot);
            chauffeurMissionCount.put(driver, chauffeurMissionCount.get(driver) + 1);*/ // Increment count
        }


        // 13. Save and log results
        saveAssignmentsInBatch(assignments);
        logResults(assignments, unassignedMissions);
    }




    /**
     *
     * @param mission
     * @param vehicle
     * @param drivers
     * @return
     */
    private AssignmentResult createAssignment(Mission mission, Vehicle vehicle, List<Driver> drivers) {
        // Build a temporary "AssignmentResult" object to group mission, vehicle, and driver
        return new AssignmentResult(mission, vehicle, drivers);
    }

    /***
     *
     * @param assignments
     */
    private void saveAssignmentsInBatch(List<AssignmentResult> assignments) {
        // Convert AssignmentResult objects to database entities
        List<MissionVehicle> missionVehicles = new ArrayList<>();
        List<DriverVehicle> driverVehicles = new ArrayList<>();

        for (AssignmentResult assignment : assignments) {
            // Create MissionVehicle link (mission + vehicle)
            MissionVehicle mv = MissionVehicle.builder()
                    .mission(assignment.getMission())
                    .vehicle(assignment.getVehicle())
                    .assignedAt(LocalDateTime.now())
                    .build();
            missionVehicles.add(mv);

            // Create DriverVehicle link (driver + mission)
            DriverVehicle cv = DriverVehicle.builder()
                    .missionVehicle(mv)  // Link to the MissionVehicle
                    //.driver(assignment.getDrivers())
                    .assignedAt(LocalDateTime.now())
                    .build();
            driverVehicles.add(cv);
        }

        // Batch save to database
        missionVehicleRepository.saveAll(missionVehicles);
        driverVehicleRepository.saveAll(driverVehicles);
    }

    /**
     *
     * @param assignments
     * @param unassignedMissions
     */
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
                            mission.getStartDate(),
                            mission.getEndDate())
            );
        }
    }


    /**
     * Updated driver selection method
     * @param slot
     * @param availability
     * @param missionCounts
     * @return
     */
    private Driver findAvailableDriver(TimeSlot slot,
                                       Map<Driver, List<TimeSlot>> availability,
                                       Map<Driver, Integer> missionCounts) {
        return availability.entrySet().stream()
                .filter(entry -> entry.getValue().stream()
                        .anyMatch(available -> available.covers(slot))
                )
                .min(Comparator.comparingInt(
                        entry -> missionCounts.getOrDefault(entry.getKey(), 0)
                ))  // Prefer driver with least missions
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    //

    /**
     * Improved vehicle selection logic
     * @param mission
     * @param availability
     * @param assignmentCounts
     * @return
     */
    private Vehicle findSuitableVehicle(Mission mission,
                                        Map<Vehicle, List<TimeSlot>> availability,
                                        Map<Vehicle, Integer> assignmentCounts) {
        // Create a TimeSlot for the mission
        TimeSlot missionSlot = new TimeSlot(mission.getStartDate(), mission.getEndDate());
        return availability.entrySet().stream()
                .filter(entry -> {
                    boolean hasCapacity = entry.getKey().getVolumeCapacity().compareTo(mission.getVolume()) >= 0;
                    boolean isAvailable = entry.getValue().stream()
                            .anyMatch(slot -> slot.covers(missionSlot));
                    return hasCapacity && isAvailable;
                })
                .min(Comparator.comparing(
                        e -> e.getKey().getVolumeCapacity(),  // Prefer smallest vehicle
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    /**
     * Type-safe availability update
     * @param availability
     * @param resource
     * @param bookedSlot
     * @param <T>
     */
    private <T> void updateTimeSlots(Map<T, List<TimeSlot>> availability, T resource, TimeSlot bookedSlot) {
        availability.computeIfPresent(resource, (k, slots) ->
                slots.stream()
                        .flatMap(slot -> slot.subtract(bookedSlot).stream())
                        .collect(Collectors.toList())
        );
    }


    /**
     *
     * @param drivers
     * @param weekStart
     */

    private void rotateDrivers(List<Driver> drivers, LocalDateTime weekStart) {
        long weekNumber = ChronoUnit.WEEKS.between(LocalDateTime.of(2023, 1, 1, 0, 0), weekStart);
        Collections.rotate(drivers, (int) (weekNumber % drivers.size()));
    }

    /**
     *
     * @param mission
     * @param availability
     * @return
     */
    private Vehicle assignVehicle(Mission mission, Map<Vehicle, List<TimeSlot>> availability) {
        TimeSlot missionSlot = new TimeSlot(mission.getStartDate(), mission.getEndDate());
        return availability.entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(slot -> slot.covers(missionSlot)))
                .map(Map.Entry::getKey)
                .filter(v -> v.getVolumeCapacity().compareTo(mission.getVolume()) >= 0)
                .min(Comparator.comparing(Vehicle::getVolumeCapacity)) // Smallest suitable vehicle
                .orElse(null);
    }

    /**
     *
     * @param availability
     * @param missionCounts
     * @param missionSlot
     * @return
     */
    private Driver assignDriver(Map<Driver, List<TimeSlot>> availability,
                                Map<Driver, Integer> missionCounts,
                                TimeSlot missionSlot) {
        return availability.entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(slot -> slot.covers(missionSlot)))
                .map(Map.Entry::getKey)
                .min(Comparator.comparing(missionCounts::get)) // Driver with fewest missions
                .orElse(null);
    }


    /**
     *
     * @param mvs
     * @param cvs
     * @param isAssigned
     */
    private void updateAssignmentStatus(List<MissionVehicle> mvs, List<DriverVehicle> cvs, boolean isAssigned) {
        List<Integer> vehicleIds = mvs.stream().map(mv -> mv.getVehicle().getId()).collect(Collectors.toList());
        List<Integer> driverIds = cvs.stream().map(cv -> cv.getDriver().getId()).collect(Collectors.toList());
        vehicleRepository.bulkUpdateAssignmentStatus(vehicleIds, isAssigned);
        driverRepository.bulkUpdateAssignmentStatus(driverIds, isAssigned);
    }



}
