package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.dto.mission.MissionRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseResponse;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleResponse;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.*;
import com.hitecart.deliverysystem.mapper.mission.MissionMapper;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import com.hitecart.deliverysystem.services.*;
import com.hitecart.deliverysystem.services.DriverVehicleService;
import com.hitecart.deliverysystem.specification.MissionSpecification;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.MissionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MissionServiceImpl implements MissionService {

    private final DriverVehicleService chauffeurVehicleService;
    private final IMissionVehicleService missionVehicleService;
    private final MissionExpensesRepository missionExpensesRepository;
    private final MissionVehicleRepository missionVehicleRepository;
    private final MissionRepository missionRepository;
    private final StopPointRepository stopPointRepository;
    private final VehicleRepository vehicleRepository;
    private final DeliveryDestinationRepository deliveryDestinationRepository;
    private final MissionExpenseMapper missionExpenseMapper;
    private final StopPointMapper stopPointMapper;
    private final MissionMapper missionMapper;
    private final StopPointServiceImpl stopPointServiceImpl;


    /**
     * @param missionRequest
     * @return
     */
    @Transactional
    @Override
    public MissionResponse save(MissionRequest missionRequest) {
        checkRequestValidating(
                missionRequest,
                "Mission is not valid",
                "Mission is not valid",
                ErrorCodes.MISSION_NOT_VALID);

        return savingMission(missionRequest);
    }

    /**
     * @param missionRequest
     * @return MissionResponse
     */
    private MissionResponse savingMission(MissionRequest missionRequest) {

        Mission missionToSave = createAndSaveMission(missionRequest);
        createMissionRelationships(missionRequest, missionToSave);
        return missionMapper.toResponse(missionToSave);
    }

    @Override
    public MissionResponse updateMissionInfo(Integer missionId, MissionRequest missionRequest) {
        if (missionId == null || missionId == 0) {
            throw new InvalidOperationException("ID not valide( null or equals 0)", ErrorCodes.MISSION_NOT_VALID);
        }
        checkMissionDates(missionRequest);

        var existingMission = missionRepository.findById(missionId).orElseThrow(
                () -> new EntityNotFoundException("Mission with :: ID : " + missionId + "not found ")
        );
        missionMapper.updateMissionFromRequest(missionRequest, existingMission);
        //checkDuplication(existingMission);
        Mission updatedMission = missionRepository.save(existingMission);

        return missionMapper.toResponse(updatedMission);
    }

    private void checkDuplication(Mission mission) {
        Integer excludeId =
                mission.getId() != null ?
                        mission.getId() :
                        -1;
        missionRepository.findAllByDeliveryDestinationIdAndStartDateAndEndDate(
                mission.getDeliveryDestination().getId(),
                mission.getStartDate(),
                mission.getEndDate(),
                excludeId).ifPresent(existing -> {
            throw new InvalidOperationException(
                    String.format(
                            "Mission already exists for destination ID [%s] with overlapping dates (%s to %s). Conflicting mission ID: %d",
                            mission.getDeliveryDestination().getId(),
                            mission.getStartDate(),
                            mission.getEndDate(),
                            existing.getId()
                    ),
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        });
    }

    private void checkMissionDates(MissionRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = request.startDate();
        LocalDateTime end = request.endDate();

        // Date formatter for user-friendly messages
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");

        // Validate start date
        if (start.isBefore(now)) {
            String msg = String.format(
                    "Start date %s must be in the future. Current time is %s",
                    start.format(formatter),
                    now.format(formatter)
            );
            throw new InvalidOperationException(msg, ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }

        // Validate start-end sequence
        if (start.isAfter(end)) {
            String msg = String.format(
                    "Start date %s must be BEFORE end date %s",
                    start.format(formatter),
                    end.format(formatter)
            );
            throw new InvalidOperationException(msg, ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }

        // Validate end date
        if (end.isBefore(now)) {
            String msg = String.format(
                    "End date %s must be in the future. Current time is %s",
                    end.format(formatter),
                    now.format(formatter)
            );
            throw new InvalidOperationException(msg, ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }

        // Redundant but explicit sequence validation
        if (end.isBefore(start)) {
            String msg = String.format(
                    "End date %s must be AFTER start date %s",
                    end.format(formatter),
                    start.format(formatter)
            );
            throw new InvalidOperationException(msg, ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }

        // Additional validation: Ensure mission has positive duration
        if (start.equals(end)) {
            String msg = String.format(
                    "Start and end dates cannot be identical: %s",
                    start.format(formatter)
            );
            throw new InvalidOperationException(msg, ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
    }

    /**
     * @param missionId
     * @param expenseRequests
     * @return
     */
    @Override
    public MissionResponse updateExpensesMission(Integer missionId, List<MissionExpenseRequest> expenseRequests) {
        CheckParmasUtil.checkID(missionId,
                "Le parametre ID Mission est null",
                "Impossible de modifier une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        if (expenseRequests.isEmpty()) {
            throw new InvalidOperationException("Empty expenses List", ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        Mission missionToUpdate = missionRepository.findById(missionId).orElseThrow(
                () -> new EntityNotFoundException("Aucune Mission avec l'id :"
                        + missionId + "n'a ete trouvé dans la base deonnée", ErrorCodes.MISSION_NOT_FOUND)
        );
        List<MissionExpense> newExpenses = expenseRequests.stream().map(missionExpenseMapper::toEntity).toList();
        List<MissionExpenseResponse> invalidExpenses = checkExpensesDates(missionToUpdate, newExpenses);

        if (!invalidExpenses.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<String> errorInvalids = invalidExpenses.stream().map(expense -> {
                return expense.getType() + " | " + expense.getExpenseDate().format(formatter);
            }).toList();
            throw new InvalidOperationException("Some expenses has invalid date ",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION,
                    errorInvalids);
        }


        this.missionExpensesRepository.saveAll(newExpenses);
        missionToUpdate.setMissionExpenseList(newExpenses);
        return missionMapper.toResponse(this.missionRepository.save(missionToUpdate));
    }

    /**
     * @param missionToUpdate
     * @param newExpenses
     * @return
     */
    private List<MissionExpenseResponse> checkExpensesDates(Mission missionToUpdate, List<MissionExpense> newExpenses) {
        List<MissionExpenseResponse> invalidExpsResp = new ArrayList<>();
        newExpenses.forEach(expenseRequest -> {
                    if (expenseRequest.getExpenseDate().isBefore(missionToUpdate.getStartDate()) ||
                            expenseRequest.getExpenseDate().isAfter(missionToUpdate.getEndDate())) {
                        invalidExpsResp.add(missionExpenseMapper.toResponse(expenseRequest));
                    }
                }
        );
        return invalidExpsResp;
    }

    /**
     * @param missionId
     * @param stopPointRequests
     * @return
     */
    @Override
    public MissionResponse updateMissionStopPoints(Integer missionId, List<StopPointRequest> stopPointRequests) {

        CheckParmasUtil.checkID(missionId,
                "Le parametre ID Mission est null",
                "Impossible de modifier une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        if (stopPointRequests.isEmpty()) {
            throw new InvalidOperationException("Empty stop points List", ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        Mission missionToUpdate = missionRepository.findById(missionId).orElseThrow(
                () -> new EntityNotFoundException("Aucune Mission avec l'id :"
                        + missionId + "n'a ete trouvé dans la base deonnée", ErrorCodes.MISSION_NOT_FOUND)
        );

        List<StopPoint> newStopPoints = stopPointRequests.stream().map(stopPointMapper::toEntity).toList();
        stopPointRepository.saveAll(newStopPoints);
        missionToUpdate.setStopPoints(newStopPoints);

        return missionMapper.toResponse(this.missionRepository.save(missionToUpdate));

    }

    /**
     * @param id
     * @return
     */
    @Override
    public MissionResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Mission est null",
                "Impossible de trouver une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        Mission missionById = missionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aucune Mission avec l'id :"
                        + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.MISSION_NOT_FOUND)
        );
        return missionMapper.toResponse(missionById);
    }

    @Override
    public List<MissionResponse> findAllByDate(Instant dateMission) {
        CheckParmasUtil.checkDate(dateMission,
                "ERREUR: Le parametre date_mission est null",
                "Impossible de trouver la liste des missions par date  avec une date null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);


        return missionRepository.findAllByStartDate(dateMission).stream()
                //.map(MissionFullDto::fromEntity)
                .map(missionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * @return
     */
    @Override
    public List<MissionResponse> findAll() {
        return missionRepository.findAll().stream()
                //.map(MissionDto::fromEntity)
                .map(missionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * @param id
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "l'ID Mission  à supprimer est null",
                "Impossible de supprimer une Mission  avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);

        Mission mission = missionRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Mission with ::ID :" + id + " Not found", ErrorCodes.MISSION_NOT_FOUND));


        stopPointRepository.deleteByMissionId(id);

        List<MissionVehicleResponse> missionVehicleBasicDtos = new ArrayList<>();
        missionVehicleBasicDtos = missionVehicleService.findByMissionId(id);
        for (MissionVehicleResponse missionVehicleBasicDto : missionVehicleBasicDtos) {
            chauffeurVehicleService.deleteByMissionVehicleId(missionVehicleBasicDto.getId());
        }
        missionVehicleRepository.deleteByMissionId(id);
        missionRepository.deleteById(id);
    }

    /**
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResponse<MissionResponse> findAllMissions(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Mission> missions = missionRepository.findAllMissions(pageable);

        List<MissionResponse> missionsDto = missions.stream()
                //.map(MissionFullDto::fromEntity)
                .map(missionMapper::toResponse)
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

    /**
     * @param page
     * @param size
     * @param connectedUser
     * @return
     */
    @Override
    public PageResponse<MissionResponse> findAllMissionsByDriverId(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Mission> missions = missionRepository.findAll(MissionSpecification.withChauffeurId(user.getId()), pageable);

        List<MissionResponse> missionsDto = missions.stream()
                //.map(MissionFullDto::fromEntity)
                .map(missionMapper::toResponse)
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
        User user = (User) connectedUser.getPrincipal();
        if (user == null) {
            throw new InvalidOperationException("Not found user :", ErrorCodes.DRIVER_NOT_FOUND);
        }
        List<Mission> missionByDriver = missionRepository.findAll(MissionSpecification.withChauffeurId(user.getId()));

        return missionByDriver.stream()
                .map(missionMapper::toResponse)
                .toList();
    }

    /**
     * @param missionRequest
     * @param msgLog
     * @param msgException
     * @param errorCodes
     */
    private void checkRequestValidating(MissionRequest missionRequest, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = MissionValidator.validate(missionRequest);
        if (!errors.isEmpty()) {
            log.error(msgLog, missionRequest);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }

    /**
     * @param missionRequest
     * @param savedMission
     */
    private void createMissionRelationships(MissionRequest missionRequest, Mission savedMission) {
        if (missionRequest.stopPoints() != null || !missionRequest.stopPoints().isEmpty()) {
        }
        missionRequest.stopPoints().forEach(stopPoint -> {
            stopPointServiceImpl.saveNewStopPoint(stopPoint, savedMission); // createStopPoint(stopPoint, savedMission);

        });
        // stopPointRepository.saveAll(stopPoints);
        missionRepository.save(savedMission);
    }

    private StopPoint createStopPoint(StopPointRequest pointRequest, Mission savedMission) {
        StopPoint stopPoint = stopPointMapper.toEntity(pointRequest);
        stopPoint.setMission(savedMission);
        return stopPoint;
    }

    /**
     * @param missionRequest
     * @return
     */
    private Mission createAndSaveMission(MissionRequest missionRequest) {
        DeliveryDestination destination = deliveryDestinationRepository.
                findById(missionRequest.deliveryDestinationId()).orElseThrow(
                        () -> new EntityNotFoundException("Opps! Aucun Delivery Destination avec l'id :" + missionRequest.id() + "n'a ete trouvé dans la base de donnée",
                                ErrorCodes.DELIVERY_DESTINATION_NOT_FOUND)
                );
        checkMissionDates(missionRequest);

        Mission missionToSave = missionMapper.toEntity(missionRequest);
        checkDuplication(missionToSave);
        missionToSave.setMissionStatus(MissionStatus.PLANNED);
        missionToSave.setDeliveryDestination(destination);

        return missionRepository.save(missionToSave);
    }

}
