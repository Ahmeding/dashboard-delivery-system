package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.MissionRepository;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.StopPointMapper;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.StopPoint;
import com.hitecart.deliverysystem.dao.StopPointRepository;
import com.hitecart.deliverysystem.services.StopPointService;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.StopPointValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StopPointServiceImpl implements StopPointService {

    private final StopPointRepository stopPointRepository;
    private final StopPointMapper stopPointMapper;
    private final MissionRepository missionRepository;


    @Override
    public StopPointResponse save(StopPointRequest stopPointRequest) {
        checkDto(stopPointRequest, "StopPoint not valid",
                "StopPoint not valide", ErrorCodes.STOP_POINT_NOT_VALID_OPERATION);
        return savingStopPoint(stopPointRequest);
    }

private void checkDuplication(StopPoint stopPoint){
    Integer excludeId =
            stopPoint.getId() != null ?
                    stopPoint.getId() :
                    -1;
        stopPointRepository.findByMissionIdByDateByLocationByName(
                stopPoint.getMission().getId(),
                stopPoint.getTitle(),
                stopPoint.getDepartureDate(),
                stopPoint.getArrivalDate(),
                stopPoint.getDepartureLocation(),
                stopPoint.getArrivalLocation(),
                excludeId).ifPresent(existing->{
                throw  new InvalidOperationException("StopPoint already exists" , ErrorCodes.STOP_POINT_NOT_VALID_OPERATION);
                }
        );
}
    /**
     * @param stopPointRequest
     * @param savedMission
     * @return
     */
    public StopPointResponse saveNewStopPoint(StopPointRequest stopPointRequest, Mission savedMission) {
        checkDto(stopPointRequest, "StopPoint not valid",
                "StopPoint not valide", ErrorCodes.STOP_POINT_NOT_VALID_OPERATION);
        Mission currentMission = missionRepository.findById(savedMission.getId()).orElseThrow(
                () -> new EntityNotFoundException("Mission with ::ID : "
                        + savedMission.getId() + " not found", ErrorCodes.MISSION_NOT_FOUND));
        // check date
        validateStopPointDates(stopPointRequest, currentMission);

        StopPoint stopPointToSave = stopPointMapper.toEntity(stopPointRequest);
        // Add custom logic here
        stopPointToSave.setTitle(
                stopPointRequest.arrivalLocation() + " - " +
                        currentMission.getDeliveryDestination()
                                .getDeliveryService().getDestinationCity()
        );
        stopPointToSave.setComment(
                stopPointRequest.arrivalLocation() + " - " +
                        currentMission.getDeliveryDestination().getDeliveryService().getDestinationCity() +
                        " start on " + stopPointRequest.departureDate() +
                        " will be delivered at " + stopPointRequest.scheduledDeliveryTime()
        );
        stopPointToSave.setDepartureDate(stopPointRequest.departureDate());
        stopPointToSave.setArrivalDate(stopPointRequest.arrivalDate());
        stopPointToSave.setScheduledDeliveryTime(stopPointRequest.scheduledDeliveryTime());
        stopPointToSave.setActualDeliveryTime(stopPointRequest.actualDeliveryTime());
        stopPointToSave.setDepartureLocation(stopPointRequest.departureLocation());
        stopPointToSave.setArrivalLocation(stopPointRequest.arrivalLocation());
        stopPointToSave.setMission(currentMission);
        return stopPointMapper.toResponse(stopPointRepository.save(stopPointToSave));
    }

    /**
     * @param stopPointRequest
     * @return savingStopPoint
     */
    private StopPointResponse savingStopPoint(StopPointRequest stopPointRequest) {

        checkDto(stopPointRequest, "StopPoint not valid",
                "StopPoint not valide", ErrorCodes.STOP_POINT_NOT_VALID_OPERATION);

        Mission currentMission = missionRepository.findById(stopPointRequest.missionId()).orElseThrow(
                () -> new EntityNotFoundException(" Mission with ::ID : " + stopPointRequest.missionId() + " not found", ErrorCodes.MISSION_NOT_FOUND)
        );
        // check date
        validateStopPointDates(stopPointRequest,currentMission);

        StopPoint stopPointToSave = stopPointMapper.toEntity(stopPointRequest);
        checkDuplication(stopPointToSave);
        // Add custom logic here
/*        stopPointToSave.setTitle(
                stopPointRequest.arrivalLocation() + " - " +
                        currentMission.getDeliveryDestination().getDeliveryService().getDestinationCity()
        );*/
        stopPointToSave.setComment(
                stopPointRequest.arrivalLocation() + " - " +
                        currentMission.getDeliveryDestination().getDeliveryService().getDestinationCity() +
                        " start on " + stopPointRequest.departureDate() +
                        " will be delivered at " + stopPointRequest.scheduledDeliveryTime()
        );
        stopPointToSave.setDepartureDate(stopPointRequest.departureDate());
        stopPointToSave.setArrivalDate(stopPointRequest.arrivalDate());
        stopPointToSave.setScheduledDeliveryTime(stopPointRequest.scheduledDeliveryTime());
        //stopPointToSave.setActualDeliveryTime(stopPointRequest.actualDeliveryTime());
        stopPointToSave.setDepartureLocation(stopPointRequest.departureLocation());
        stopPointToSave.setArrivalLocation(stopPointRequest.arrivalLocation());
        stopPointToSave.setMission(currentMission);
        return stopPointMapper.toResponse(stopPointRepository.save(stopPointToSave));
    }

    private void validateStopPointDates(StopPointRequest stopPointRequest, Mission mission) {
        Objects.requireNonNull(stopPointRequest, "StopPointRequest cannot be null");
        Objects.requireNonNull(mission, "Mission cannot be null");

        LocalDate missionStart = mission.getStartDate().toLocalDate();
        LocalDate missionEnd = mission.getEndDate().toLocalDate();
        LocalDate departureDate = stopPointRequest.departureDate();
        LocalDate arrivalDate = stopPointRequest.arrivalDate();

        // Validate departure date against mission dates
        if (departureDate.isBefore(missionStart)) {
            throw new InvalidOperationException(
                    "Departure date " + departureDate + " must be on or after mission start date " + missionStart,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        if (departureDate.isAfter(missionEnd)) {
            throw new InvalidOperationException(
                    "Departure date " + departureDate + " must be on or before mission end date " + missionEnd,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        // Validate arrival date against mission dates
        if (arrivalDate.isBefore(missionStart)) {
            throw new InvalidOperationException(
                    "Arrival date " + arrivalDate + " must be on or after mission start date " + missionStart,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        if (arrivalDate.isAfter(missionEnd)) {
            throw new InvalidOperationException(
                    "Arrival date " + arrivalDate + " must be on or before mission end date " + missionEnd,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        // Validate date sequence
        if (departureDate.isAfter(arrivalDate)) {
            throw new InvalidOperationException(
                    "Departure date " + departureDate + " must be before arrival date " + arrivalDate,
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }

        // Additional time validation if needed
/*        if (mission.getStartDate().toLocalTime().isAfter(mission.getEndDate().toLocalTime())) {
            throw new InvalidOperationException(
                    "Mission start time must be before end time",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        }*/
    }

    @Override
    public StopPointResponse updateStopPointInfo(Integer stopPointId, StopPointRequest stopPointRequest) {

        var existStopPoint = stopPointRepository.findById(stopPointId).orElseThrow(
                () -> new EntityNotFoundException("Stop Point with ::ID : " + stopPointId + " not found", ErrorCodes.STOP_POINT_NOT_FOUND)
        );

        Mission currentMission = missionRepository.findById(stopPointRequest.missionId()).orElseThrow(
                ()-> new EntityNotFoundException("Mission with ::ID : "
                        + stopPointRequest.missionId()
                        + " not found", ErrorCodes.MISSION_NOT_FOUND));

        stopPointMapper.updateStopPointFromRequest(stopPointRequest, existStopPoint);
        //
        checkDuplication(existStopPoint);

        // check date
        validateStopPointDates(stopPointRequest,currentMission);
        StopPoint updatedStopPoint = stopPointRepository.save(existStopPoint);

        return stopPointMapper.toResponse(updatedStopPoint);
    }

    @Override
    public StopPointResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Etape est null",
                "Impossible de trouver une etape avec un ID null",
                ErrorCodes.STOP_POINT_NOT_VALID_OPERATION);
        StopPoint stopPointById = stopPointRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aucune Etape avec l'id :"
                        + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.STOP_POINT_NOT_FOUND)
        );
        return stopPointMapper.toResponse(stopPointById);

    }

    @Override
    public List<StopPointResponse> findByMissionId(Integer missionId) {
        CheckParmasUtil.checkID(missionId,
                "ERREUR: Le parametre Id mision est null",
                "Impossible de trouver la liste des etapes par misson avec id misson null",
                ErrorCodes.STOP_POINT_NOT_VALID_OPERATION);
        missionRepository.findById(missionId).orElseThrow(()->
                new EntityNotFoundException("Mission with ID : " + missionId +" not found."));
        List<StopPoint> stopPoints = stopPointRepository.findByMissionId(missionId);

        return stopPoints.stream()
                .map(stopPointMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StopPointResponse> findAll() {
        return stopPointRepository.findAll().stream()
                .map(stopPointMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "l'ID Etape  à supprimer est null",
                "Impossible de supprimer un frais de Mission  avec un ID null", ErrorCodes.STOP_POINT_NOT_VALID_OPERATION);
        findById(id);
        stopPointRepository.deleteById(id);
    }

    private void checkDto(StopPointRequest stopPointRequest, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = StopPointValidator.validate(stopPointRequest);
        if (!errors.isEmpty()) {
            log.error(msgLog, stopPointRequest);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }

}
