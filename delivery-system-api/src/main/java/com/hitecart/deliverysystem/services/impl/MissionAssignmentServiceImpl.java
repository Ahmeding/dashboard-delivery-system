package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.dto.driver.DriverResponse;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleRequest;
import com.hitecart.deliverysystem.dto.mission.MissionAssignmentRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.DriverMapper;
import com.hitecart.deliverysystem.mapper.NotificationMapper;
import com.hitecart.deliverysystem.mapper.VehicleMapper;
import com.hitecart.deliverysystem.mapper.mission.MissionMapper;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;
import com.hitecart.deliverysystem.services.DriverVehicleService;
import com.hitecart.deliverysystem.services.MissionAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service implementation for managing mission vehicle and driver assignments.
 * Handles creation, update, and removal of vehicle/driver assignments for missions.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MissionAssignmentServiceImpl implements MissionAssignmentService {

    // ============================== DEPENDENCIES ==============================
    private final MissionRepository missionRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final MissionVehicleRepository missionVehicleRepository;
    private final DriverVehicleRepository driverVehicleRepository;
    private final StopPointRepository stopPointRepository;

    private final DriverVehicleService driverVehicleService;
    private final MissionVehicleServiceImpl missionVehicleService;
    private final NotificationServiceImpl notificationService;

    private final MissionMapper missionMapper;
    private final VehicleMapper vehicleMapper;
    private final DriverMapper driverMapper;
    private final NotificationMapper notificationMapper;

    // ============================= CORE METHODS ===============================
    /**
     * Manages vehicle and driver assignments for a mission.
     *
     * @param missionId ID of the mission to update
     * @param missionAssignmentRequest New assignment configuration
     * @return Updated mission response
     * @throws EntityNotFoundException if mission doesn't exist
     * @throws InvalidOperationException if mission has started or request is invalid
     */
    @Override
    @Transactional
    public MissionResponse managingVehicleDriverAssigment(
            Integer missionId,
            MissionAssignmentRequest missionAssignmentRequest
    ) {
        // Retrieve mission with required relationships
        Mission existingMission = missionRepository.findByIdWithMissionVehiclesAndDrivers(missionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No Mission found with ID: " + missionId,
                        ErrorCodes.MISSION_NOT_FOUND
                ));

        // Validate mission hasn't started
        if (existingMission.getStartDate().isBefore(LocalDateTime.now())) {
            log.warn("Mission {} started at {} - cannot modify assignments",
                    missionId, existingMission.getStartDate());
            throw new InvalidOperationException(
                    "Cannot modify assignments: Mission has already started",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        // Validate request structure
        validateMissionAssignmentRequest(missionAssignmentRequest);

        // Create lookup map of existing vehicle assignments
        Map<Integer, MissionVehicle> existingMissionVehiclesMap = existingMission.getMissionVehicles()
                .stream()
                .collect(Collectors.toMap(
                        missionVehicle -> missionVehicle.getVehicle().getId(),
                        Function.identity()
                ));

        // Process each vehicle in request
        List<MissionVehicle> updatedMissionVehicles = new ArrayList<>();
        missionAssignmentRequest.missionVehicles().forEach(missionVehicleRequest -> {
            Integer vehicleId = missionVehicleRequest.vehicleId();
            log.debug("Processing vehicle assignment for ID: {}", vehicleId);
            log.debug("Existing assignment keys: {}", existingMissionVehiclesMap.keySet());

            MissionVehicle existingMissionVehicle = existingMissionVehiclesMap.get(vehicleId);
            log.debug("Assignment status: {}",
                    existingMissionVehicle != null ? "EXISTING" : "NEW");

            if (existingMissionVehicle != null) {
                checkVehicleStatus(existingMissionVehicle.getVehicle());
                // Update existing assignment
                existingMissionVehicle.setAssignedAt(missionVehicleRequest.assignedAt());
                updateDriverVehicles(existingMissionVehicle, missionVehicleRequest.driverVehicles());
                updatedMissionVehicles.add(existingMissionVehicle);
            } else {
                // Create new assignment
                MissionVehicle newMissionVehicle = createNewMissionVehicle(missionVehicleRequest, existingMission);
                addDriverVehicles(newMissionVehicle, missionVehicleRequest.driverVehicles());
                existingMission.addMissionVehicle(newMissionVehicle);
                updatedMissionVehicles.add(newMissionVehicle);
                log.info("Created new assignment for vehicle ID: {}", vehicleId);
            }
        });

        // Remove obsolete assignments
        List<MissionVehicle> vehiclesToRemove = existingMission.getMissionVehicles()
                .stream()
                .filter(vehicle -> !updatedMissionVehicles.contains(vehicle))
                .collect(Collectors.toList());
        vehiclesToRemove.forEach(existingMission::removeMissionVehicle);

        return missionMapper.toResponse(existingMission);
    }

    /**
     * Checks for vehicle scheduling conflicts.
     *
     * @param missionVehicleRequest Request containing mission and vehicle details
     * @return ID of conflicting vehicle if found, 0 otherwise
     * @throws EntityNotFoundException if mission or vehicle doesn't exist
     */
    @Override
    public Integer checkOverlappingVehicles(MissionVehicleRequest missionVehicleRequest) {
        Mission currentMission = missionRepository.findById(missionVehicleRequest.missionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Mission With ID :: " + missionVehicleRequest.missionId() + " not found!",
                        ErrorCodes.MISSION_NOT_FOUND
                ));

        Integer vehicleId = missionVehicleRequest.vehicleId();
        vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Vehicle with ID " + missionVehicleRequest.vehicleId() + " not found",
                        ErrorCodes.MISSION_NOT_FOUND
                )
        );

        return missionVehicleService.findUnavailableVehicles(
                vehicleId,
                currentMission.getStartDate(),
                currentMission.getEndDate()
        );
    }

    /**
     * Checks for driver scheduling conflicts.
     *
     * @param missionVehicleRequest Request containing mission and driver details
     * @return List of conflicting driver IDs
     * @throws EntityNotFoundException if mission or vehicle doesn't exist
     * @throws InvalidOperationException if driver list is empty
     */
    @Override
    public List<Integer> checkOverlappingDrivers(MissionVehicleRequest missionVehicleRequest) {
        Mission currentMission = missionRepository.findById(missionVehicleRequest.missionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Mission with " + missionVehicleRequest.missionId() + " not found",
                        ErrorCodes.MISSION_NOT_FOUND
                ));

        vehicleRepository.findById(missionVehicleRequest.vehicleId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Vehicle with " + missionVehicleRequest.vehicleId() + " not found",
                        ErrorCodes.MISSION_NOT_FOUND
                )
        );

        if (missionVehicleRequest.driverVehicles() == null ||
                missionVehicleRequest.driverVehicles().isEmpty()) {
            throw new InvalidOperationException(
                    "Driver list is empty or null",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        List<Integer> driverIds = missionVehicleRequest.driverVehicles()
                .stream()
                .map(driverVehicle -> {
                    Integer driverId = driverVehicle.driverId();
                    checkExistingDriver(driverId);
                    return driverId;
                })
                .collect(Collectors.toList());

        return driverVehicleService.findUnavailableDrivers(
                driverIds,
                currentMission.getStartDate(),
                currentMission.getEndDate()
        );
    }

    // ======================== DRIVER ASSIGNMENT METHODS ======================
    /**
     * Updates driver assignments for a vehicle.
     *
     * @param missionVehicle The vehicle assignment to update
     * @param driverVehicleRequests List of driver assignment requests
     */
    private void updateDriverVehicles(
            MissionVehicle missionVehicle,
            List<DriverVehicleRequest> driverVehicleRequests
    ) {
        // Map existing drivers by their ID
        Map<Integer, DriverVehicle> existingDriversMap = missionVehicle.getDriverVehicles()
                .stream()
                .collect(Collectors.toMap(
                        driverVehicle -> driverVehicle.getDriver().getId(),
                        Function.identity()
                ));

        List<DriverVehicle> updatedDrivers = new ArrayList<>();
        driverVehicleRequests.forEach(driverVReq -> {
            Integer driverId = driverVReq.driverId();
            DriverVehicle existingDriverVehicle = existingDriversMap.get(driverId);

            if (existingDriverVehicle != null) {
                checkDriverAccountStatus(existingDriverVehicle.getDriver());
                // Keep existing driver assignment
                updatedDrivers.add(existingDriverVehicle);
            } else {
                // Create new driver assignment
                DriverVehicle newDriverVehicle = new DriverVehicle();
                Driver newDriver = driverRepository.findById(driverId).orElseThrow(
                        () -> new EntityNotFoundException("Driver with ID :: "+ driverId + " not found")
                );
                checkDriverAccountStatus(newDriver);
                newDriverVehicle.setDriver(newDriver);
                missionVehicle.addDriverVehicle(newDriverVehicle);
                updatedDrivers.add(newDriverVehicle);
            }
        });

        // Remove obsolete drivers
        List<DriverVehicle> toRemove = missionVehicle.getDriverVehicles().stream()
                .filter(dv -> !updatedDrivers.contains(dv))
                .collect(Collectors.toList());
        toRemove.forEach(missionVehicle::removeDriverVehicle);
    }



    /**
     * Adds new drivers to a vehicle assignment.
     *
     * @param newMissionVehicle The vehicle assignment to add drivers to
     * @param driverVehicleRequests List of driver assignment requests
     */
    private void addDriverVehicles(
            MissionVehicle newMissionVehicle,
            List<DriverVehicleRequest> driverVehicleRequests
    ) {
        driverVehicleRequests.forEach(driverVehicleRequest -> {
            Driver driver = driverRepository.findById(driverVehicleRequest.driverId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Driver with ID: " + driverVehicleRequest.driverId() + " Not Found",
                            ErrorCodes.DRIVER_NOT_FOUND
                    ));

            DriverVehicle driverVehicle = new DriverVehicle();
            driverVehicle.setDriver(driver);
            driverVehicle.setMissionVehicle(newMissionVehicle);
            newMissionVehicle.addDriverVehicle(driverVehicle);
        });
    }

    // ============================= VALIDATION METHODS =========================
    /**
     * Validates the entire assignment request.
     *
     * @param missionAssignmentRequest Request to validate
     * @throws InvalidOperationException if validation fails
     */
    private void validateMissionAssignmentRequest(MissionAssignmentRequest missionAssignmentRequest) {
        validateRequestNotNull(missionAssignmentRequest);
        validateVehiclesExist(missionAssignmentRequest);
        validateDriverAssignments(missionAssignmentRequest);
        validateDriverCount(missionAssignmentRequest);
        validateVehicleIds(missionAssignmentRequest);
        validateDriverIds(missionAssignmentRequest);
    }

    /**
     * Validates request is not null.
     */
    private void validateRequestNotNull(MissionAssignmentRequest request) {
        if (request == null) {
            throw new InvalidOperationException(
                    "Mission assignment request cannot be null",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * Validates vehicles exist in request.
     */
    private void validateVehiclesExist(MissionAssignmentRequest request) {
        if (CollectionUtils.isEmpty(request.missionVehicles())) {
            throw new InvalidOperationException(
                    "At least one vehicle must be specified",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * Validates each vehicle has driver assignments.
     */
    private void validateDriverAssignments(MissionAssignmentRequest request) {
        request.missionVehicles().forEach(vehicle -> {
            if (CollectionUtils.isEmpty(vehicle.driverVehicles())) {
                throw new InvalidOperationException(
                        "Each vehicle must have at least one driver",
                        ErrorCodes.MISSION_NOT_VALID_OPERATION
                );
            }
        });
    }

    /**
     * Validates driver count per vehicle (max 2).
     */
    private void validateDriverCount(MissionAssignmentRequest request) {
        request.missionVehicles().forEach(vehicle -> {
            if (vehicle.driverVehicles().size() > 2) {
                throw new InvalidOperationException(
                        "Maximum 2 drivers allowed per vehicle",
                        ErrorCodes.MISSION_NOT_VALID_OPERATION
                );
            }
        });
    }

    /**
     * Validates vehicle IDs in request.
     */
    private void validateVehicleIds(MissionAssignmentRequest request) {
        List<Integer> vehicleIds = request.missionVehicles().stream()
                .map(MissionVehicleRequest::vehicleId)
                .collect(Collectors.toList());

        checkDuplicateVehicleIds(vehicleIds);
        checkExistingVehicle(request.missionVehicles());
    }

    /**
     * Validates driver IDs in request.
     */
    private void validateDriverIds(MissionAssignmentRequest request) {
        checkDriverDuplicate(request);
        checkExistingDriver(request);
    }

    /**
     * Checks for duplicate vehicle IDs in request.
     *
     * @param vehicleIds List of vehicle IDs to check
     * @throws InvalidOperationException if duplicates found
     */
    private void checkDuplicateVehicleIds(List<Integer> vehicleIds) {
        Set<Integer> uniqueIds = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();

        for (Integer vehicleId : vehicleIds) {
            if (!uniqueIds.add(vehicleId)) {
                duplicates.add(vehicleId);
            }
        }

        if (!duplicates.isEmpty()) {
            throw new InvalidOperationException(
                    "Duplicate vehicle IDs found: " + duplicates,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * Checks for duplicate driver assignments in request.
     *
     * @param request Assignment request to validate
     * @throws InvalidOperationException if duplicates found
     */
    private void checkDriverDuplicate(MissionAssignmentRequest request) {
        Set<Integer> allDriverIds = new HashSet<>();
        Map<Integer, Set<Integer>> vehicleDriverMap = new HashMap<>();

        for (MissionVehicleRequest vehicleReq : request.missionVehicles()) {
            int vehicleId = vehicleReq.vehicleId();
            Set<Integer> vehicleDrivers = new HashSet<>();
            vehicleDriverMap.put(vehicleId, vehicleDrivers);

            for (DriverVehicleRequest driverReq : vehicleReq.driverVehicles()) {
                int driverId = driverReq.driverId();

                // Check duplicate within same vehicle
                if (!vehicleDrivers.add(driverId)) {
                    throw new InvalidOperationException(
                            "Driver " + driverId + " duplicated in vehicle " + vehicleId,
                            ErrorCodes.MISSION_NOT_VALID_OPERATION
                    );
                }

                // Check duplicate across vehicles
                if (allDriverIds.contains(driverId)) {
                    throw new InvalidOperationException(
                            "Driver " + driverId + " assigned to multiple vehicles",
                            ErrorCodes.MISSION_NOT_VALID_OPERATION
                    );
                }
                allDriverIds.add(driverId);
            }
        }
    }

    /**
     * Verifies vehicles exist in database.
     *
     * @param requests List of vehicle requests to validate
     * @throws EntityNotFoundException if vehicles not found
     * @throws InvalidOperationException if no vehicles provided
     */
    private void checkExistingVehicle(List<MissionVehicleRequest> requests) {
        Set<Integer> vehicleIds = requests.stream()
                .map(MissionVehicleRequest::vehicleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (vehicleIds.isEmpty()) {
            throw new InvalidOperationException(
                    "At least one vehicle must be assigned!",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        Set<Integer> existingIds = vehicleRepository.findAllById(vehicleIds).stream()
                .map(Vehicle::getId)
                .collect(Collectors.toSet());

        Set<Integer> missing = vehicleIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toSet());

        if (!missing.isEmpty()) {
            throw new EntityNotFoundException(
                    "Vehicles not found: " + missing,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * Verifies drivers exist in database.
     *
     * @param request Assignment request to validate
     * @throws InvalidOperationException if drivers not found
     */
    private void checkExistingDriver(MissionAssignmentRequest request) {
        Set<Integer> driverIds = request.missionVehicles().stream()
                .flatMap(mv -> mv.driverVehicles().stream())
                .map(DriverVehicleRequest::driverId)
                .collect(Collectors.toSet());

        Set<Integer> existingIds = driverRepository.findAllById(driverIds).stream()
                .map(Driver::getId)
                .collect(Collectors.toSet());

        Set<Integer> missing = driverIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toSet());

        if (!missing.isEmpty()) {
            throw new InvalidOperationException(
                    "Drivers not found: " + missing,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    // ========================== HELPER METHODS ==============================
    /**
     * Creates a new MissionVehicle entity.
     *
     * @param request Vehicle assignment request
     * @param mission Mission to assign vehicle to
     * @return New MissionVehicle entity
     */
    private MissionVehicle createNewMissionVehicle(
            MissionVehicleRequest request,
            Mission mission
    ) {
        Vehicle vehicle =  vehicleRepository.getReferenceById(request.vehicleId());
        checkVehicleStatus(vehicle);
        MissionVehicle newMissionVehicle = new MissionVehicle();
        newMissionVehicle.setAssignedAt(request.assignedAt());
        newMissionVehicle.setVehicle(vehicle);
        newMissionVehicle.setMission(mission);
        return newMissionVehicle;
    }

    /**
     * Creates a DriverVehicle entity.
     *
     * @param request Driver assignment request
     * @param missionVehicle Vehicle assignment to link to
     * @return New DriverVehicle entity
     */
    private DriverVehicle createDriverVehicle(
            DriverVehicleRequest request,
            MissionVehicle missionVehicle
    ) {
        return DriverVehicle.builder()
                .missionVehicle(missionVehicle)
                .driver(driverRepository.getReferenceById(request.driverId()))
                .assignedAt(LocalDateTime.now())
                .build();
    }


    /**
     * Verifies driver exists in database.
     *
     * @param driverId Driver ID to check
     * @throws EntityNotFoundException if driver not found
     */
    private void checkExistingDriver(Integer driverId) {
        driverRepository.findById(driverId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Driver with ID " + driverId + " not found",
                        ErrorCodes.DRIVER_NOT_FOUND
                )
        );
    }

    // ========================= NOTIFICATION METHODS =========================
    /**
     * Sends notifications to drivers asynchronously.
     *
     * @param driverIds List of driver IDs to notify
     * @param missionId Mission ID for notification context
     */
    @Async
    protected void sendNotificationsAsync(List<Integer> driverIds, Integer missionId) {
        driverIds.parallelStream().forEach(driverId -> {
            notificationService.sendNotification(
                    driverId,
                    notificationMapper.toRequest(
                            Notification.builder()
                                    .title("New Mission Assignment")
                                    .user(User.builder().id(driverId).build())
                                    .message("Assigned to mission #" + missionId)
                                    .status(NotificationStatus.SEND)
                                    .build()
                    )
            );
        });
    }

    /**
     * Notifies drivers about their assignments.
     *
     * @param driverVehicleRequests Driver assignment requests
     * @param missionId Mission ID for notification context
     */
    private void notifyDrivers(List<DriverVehicleRequest> driverVehicleRequests, Integer missionId) {
        List<Integer> driverIds = driverVehicleRequests.stream()
                .map(DriverVehicleRequest::driverId)
                .collect(Collectors.toList());
        sendNotificationsAsync(driverIds, missionId);
    }

    /**
     * Check the vehicle's status (to assign this vehicle the vehicle must be enabled)
     * @param vehicle
     */
    private void checkVehicleStatus(Vehicle vehicle){
        if(!vehicle.getIsActive()){
            throw new InvalidOperationException("Vehicle is not active",ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
    }

    /**
     *  Check the driver's account (to assign this driver, the account must be enabled and not locked)
     * @param driver
     */
    private void checkDriverAccountStatus(Driver driver) {
        if (driver.isAccountLocked() && !driver.isEnabled()) {
            throw new InvalidOperationException(
                    "Cannot assign the driver " + driver.fullName() +
                            ". Please verify that the account is not locked or disabled.",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }
}
