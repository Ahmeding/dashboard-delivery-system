package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.MissionRepository;
import com.hitecart.deliverysystem.dao.VehicleRepository;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.mission.MissionStartEndRequest;
import com.hitecart.deliverysystem.dto.mission.MissionStatusUpdateRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.mission.MissionMapper;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.Vehicle;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import com.hitecart.deliverysystem.services.MissionStartEndService;
import com.hitecart.deliverysystem.validator.MissionStartingValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing mission start/end operations and status updates.
 * Handles mileage updates, status transitions, and related validations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MissionStartEndServiceImpl implements MissionStartEndService {

    private final MissionRepository missionRepository;
    private final VehicleRepository vehicleRepository;
    private final MissionMapper missionMapper;

    // ============================= CORE METHODS ===============================

    /**
     * Updates mission mileage (start or end) and handles related operations.
     *
     * @param missionId ID of the mission to update
     * @param missionStartEndRequest Request containing mileage details and operation flag
     * @return Updated mission response
     * @throws EntityNotFoundException if mission or vehicle not found
     * @throws InvalidOperationException for invalid operations or data
     */
    @Override
    @Transactional
    public MissionResponse updateMileage(Integer missionId, MissionStartEndRequest missionStartEndRequest) {
        Mission existingMission = validateRequest(missionId, missionStartEndRequest);

        switch (missionStartEndRequest.flag()) {
            case START_KM:
                handleStartKilometers(existingMission, missionStartEndRequest);
                break;
            case END_KM:
                handleEndKilometers(existingMission, missionStartEndRequest);
                break;
            default:
                throw new InvalidOperationException(
                        "Invalid KmFlag: " + missionStartEndRequest.flag(),
                        ErrorCodes.MISSION_NOT_VALID_OPERATION
                );
        }

        return missionMapper.toResponse(missionRepository.save(existingMission));
    }

    /**
     * Updates the status of a mission.
     *
     * @param missionId ID of the mission to update
     * @param missionStatusUpdateRequest Request containing new status
     * @return Updated mission response
     * @throws EntityNotFoundException if mission not found
     * @throws InvalidOperationException for invalid status transitions or conditions
     */
    @Override
    public MissionResponse updateMissionStatus(
            Integer missionId,
            MissionStatusUpdateRequest missionStatusUpdateRequest
    ) {
        Mission missionToUpdate = validateStatusUpdateRequest(missionId, missionStatusUpdateRequest);
        missionToUpdate.setMissionStatus(missionStatusUpdateRequest.missionStatus());
        return missionMapper.toResponse(missionRepository.save(missionToUpdate));
    }

    // =========================== PROCESSING METHODS ===========================

    /**
     * Handles start kilometer update operations.
     *
     * @param mission Mission to update
     * @param request Request containing start mileage
     */
    private void handleStartKilometers(Mission mission, MissionStartEndRequest request) {
        mission.setStartMileage(request.startMileage());
        mission.setMissionStatus(MissionStatus.IN_PROGRESS);
        log.info("Mission {} started with mileage: {}", mission.getId(), request.startMileage());
    }

    /**
     * Handles end kilometer update operations.
     *
     * @param mission Mission to update
     * @param request Request containing end mileage
     */
    private void handleEndKilometers(Mission mission, MissionStartEndRequest request) {
        updateVehicleMileage(request.vehicleId(), request.endMileage());
        mission.setEndMileage(request.endMileage());
        mission.setMissionStatus(MissionStatus.COMPLETED);
        log.info("Mission {} completed with end mileage: {}", mission.getId(), request.endMileage());
    }

    /**
     * Updates a vehicle's current mileage.
     *
     * @param vehicleId ID of vehicle to update
     * @param kilometrage New mileage value
     * @throws EntityNotFoundException if vehicle not found
     * @throws InvalidOperationException for invalid mileage values
     */
    private void updateVehicleMileage(Integer vehicleId, double kilometrage) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Vehicle not found with ID: " + vehicleId,
                        ErrorCodes.VEHICLE_NOT_FOUND
                )
        );

        if (vehicle.getMileage() == null) {
            throw new IllegalArgumentException("Vehicle mileage cannot be null");
        }

        BigDecimal newMileage = new BigDecimal(kilometrage);
        if (newMileage.compareTo(vehicle.getMileage()) <= 0) {
            throw new InvalidOperationException(
                    "New mileage (" + kilometrage + ") must be higher than current (" +
                            vehicle.getMileage() + ")",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        vehicle.setMileage(newMileage);
        vehicleRepository.save(vehicle);
        log.info("Updated mileage for vehicle {} to {}", vehicleId, kilometrage);
    }

    // ========================== VALIDATION METHODS ============================

    /**
     * Validates mission start/end request.
     *
     * @param missionId ID of mission to validate
     * @param request Request to validate
     * @return Validated mission entity
     * @throws EntityNotFoundException if mission/vehicle not found
     * @throws InvalidOperationException for validation failures
     */
    private Mission validateRequest(Integer missionId, MissionStartEndRequest request) {
        // Validate request structure
        validateRequestStructure(request);

        // Retrieve mission
        Mission existingMission = missionRepository.findById(missionId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Mission not found with ID: " + missionId,
                                ErrorCodes.MISSION_NOT_FOUND
                        )
                );

        // Check mission has vehicles
        if (existingMission.getMissionVehicles().isEmpty()) {
            throw new InvalidOperationException(
                    "Mission has no assigned vehicles",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        // Retrieve vehicle
        Vehicle existingVehicle = vehicleRepository.findById(request.vehicleId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Vehicle not found with ID: " + request.vehicleId(),
                        ErrorCodes.MISSION_NOT_VALID_OPERATION
                )
        );

        // Validate timing and assignments
        validateMissionDate(existingMission);
        validateVehicleAssignment(existingMission, request.vehicleId());
        validateMileageValues(request, existingVehicle);
        validateETA(request, existingMission);

        return existingMission;
    }

    /**
     * Validates mission status update request.
     *
     * @param missionId ID of mission to update
     * @param request Status update request
     * @return Validated mission entity
     * @throws EntityNotFoundException if mission not found
     * @throws InvalidOperationException for validation failures
     */
    private Mission validateStatusUpdateRequest(
            Integer missionId,
            MissionStatusUpdateRequest request
    ) {
        if (request == null) {
            throw new InvalidOperationException(
                    "Status update request cannot be null",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        if (missionId <= 0) {
            throw new InvalidOperationException(
                    "Invalid mission ID: " + missionId,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        Mission mission = missionRepository.findById(missionId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Mission not found with ID: " + missionId,
                        ErrorCodes.MISSION_NOT_FOUND
                )
        );

        validateMissionDate(mission);
        validateMissionAccomplishedStatus(mission);

        return mission;
    }

    /**
     * Validates basic request structure.
     */
    private void validateRequestStructure(MissionStartEndRequest request) {
        List<String> errors = MissionStartingValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error("Invalid mission start/end request: {}", request);
            throw new InvalidEntityException(
                    "Invalid mission start/end request",
                    ErrorCodes.MISSION_NOT_VALID,
                    errors
            );
        }
    }

    /**
     * Validates mission date constraints.
     */
    private void validateMissionDate(Mission mission) {
        if (mission.getStartDate() == null) {
            throw new InvalidOperationException(
                    "Mission has no start date defined",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        LocalDate missionStartDate = mission.getStartDate().toLocalDate();
        LocalDate currentDate = LocalDate.now();

        if (missionStartDate.isAfter(currentDate)) {
            throw new InvalidOperationException(
                    "Cannot start mission: Start date is in the future",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        if (missionStartDate.isBefore(currentDate)) {
            throw new InvalidOperationException(
                    "Cannot start mission: Start date has passed",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * Validates vehicle is assigned to mission.
     */
    private void validateVehicleAssignment(Mission mission, Integer vehicleId) {
        boolean isAssigned = mission.getMissionVehicles().stream()
                .anyMatch(mv -> mv.getVehicle().getId().equals(vehicleId));

        if (!isAssigned) {
            throw new InvalidOperationException(
                    "Vehicle " + vehicleId + " not assigned to mission: " + mission.getTitle(),
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * Validates mileage values against vehicle's current mileage.
     */
    private void validateMileageValues(MissionStartEndRequest request, Vehicle vehicle) {
        if (request.startMileage() < vehicle.getMileage().doubleValue()) {
            throw new InvalidOperationException(
                    "Start mileage (" + request.startMileage() + ") cannot be less than vehicle's current mileage (" +
                            vehicle.getMileage().doubleValue() + ")",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * Validates Estimated Time of Arrival (ETA).
     */
    private void validateETA(MissionStartEndRequest request, Mission mission) {
        if (request.eta().isBefore(mission.getStartDate())) {
            throw new InvalidOperationException(
                    "ETA must be after mission start date",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * Validates mission isn't marked as accomplished.
     */
    private void validateMissionAccomplishedStatus(Mission mission) {
        if (mission.getIsAccomplished() != null && mission.getIsAccomplished()) {
            throw new InvalidOperationException(
                    "Cannot update status of an accomplished mission",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }
    }
}
