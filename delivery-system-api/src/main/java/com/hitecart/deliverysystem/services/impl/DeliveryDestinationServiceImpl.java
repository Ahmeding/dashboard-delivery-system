package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationRequest;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationResponse;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.*;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.services.DeliveryDestinationService;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.DeliveryDestinationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryDestinationServiceImpl implements DeliveryDestinationService {

    private final DeliveryDestinationRepository deliveryDestinationRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final DeliveryServiceRepository deliveryServiceRepository;

    private final ClientService clientService;
    private final DeliveryDestinationMapper deliveryDestinationResponseMapper;
    private final DeliveryDestinationMapper deliveryDestinationMapper;
    private final ClientMapper clientMapper;
    private final DeliveryPointMapper deliveryPointMapper;
    private final ClientRepository clientRepository;
    private final MissionRepository missionRepository;

    @Transactional
    @Override
    public DeliveryDestinationResponse save(DeliveryDestinationRequest request) {

        checkDto(request, "Delivery Destination not valid", "le Delivery Destination n'est pas valid");
        checkClient(request.deliveryPoints().get(0).clientId());
        return savingDeliveryDestination(request);
    }

    /**
     * @param deliveryDestinationRequest
     * @return
     */
    private DeliveryDestinationResponse savingDeliveryDestination(DeliveryDestinationRequest deliveryDestinationRequest) {

        Client managedClient = clientRepository.findById(deliveryDestinationRequest.deliveryPoints().get(0).clientId()).orElseThrow(
                () -> new EntityNotFoundException("Client with :: ID " + deliveryDestinationRequest.deliveryPoints().get(0).clientId() + " not found"
                        , ErrorCodes.CLIENT_NOT_FOUND)
        );
        deliveryServiceRepository.findById(deliveryDestinationRequest.deliveryServiceId())
                .orElseThrow(() -> new EntityNotFoundException("DeliveryService not found"));

        DeliveryDestination existingDeliveryDestination = deliveryDestinationExists(deliveryDestinationRequest);
        //DeliveryDestination ddToSave = deliveryDestinationMapper.toEntity(deliveryDestinationRequest);
        DeliveryDestination deliveryDestinationToSave;


        if (existingDeliveryDestination != null) {
            DeliveryPointFilterResult deliveryPointFilterResult = this.validateDeliveryPoint(deliveryDestinationRequest.deliveryPoints(),
                    existingDeliveryDestination.getDeliveryPoints());
            deliveryDestinationToSave = existingDeliveryDestination;

            if (!deliveryPointFilterResult.duplicateDeliveryPoints.isEmpty()) {
                throw new InvalidOperationException("This Delivery Point(s) to "
                        + deliveryDestinationToSave.getDeliveryService().getDestinationCountry() +" - "
                        + deliveryDestinationToSave.getDeliveryService().getDestinationCity() +
                        " already exists:: " + String.join(", ", deliveryPointFilterResult.duplicateDeliveryPoints),
                ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);
            }
            /** todo check same scheduel time!! */

            deliveryDestinationToSave.setId(existingDeliveryDestination.getId());
            updateCurrentDeliveryPointsList(deliveryPointFilterResult.newDeliveryPoints, deliveryDestinationToSave);
            //  log a warning
            log.warn("DeliveryDestination already existed (id={}). New DeliveryPoint(s) have been appended.", existingDeliveryDestination.getId());

        } else {
            // -------------------------------------------------------------
            //  “Create” path: no existing DD → build a fresh entity
            // -------------------------------------------------------------
            deliveryDestinationToSave = deliveryDestinationMapper.toEntity(deliveryDestinationRequest);
            linkedToNewDeliveryPoints(deliveryDestinationToSave, managedClient);
            log.warn("Creating brand-new DeliveryDestination (no prior match).");
        }

        DeliveryDestination savedDeliveryDestination = deliveryDestinationRepository.save(deliveryDestinationToSave);
        return deliveryDestinationMapper.toResponse(savedDeliveryDestination);

    }

    private void updateCurrentDeliveryPointsList(List<DeliveryPointRequest> newDeliveryPoints, DeliveryDestination deliveryDestinationToSave) {
        if (newDeliveryPoints != null
                && !newDeliveryPoints.isEmpty()) {

            for (DeliveryPointRequest deliveryPointRequest : newDeliveryPoints) {

                DeliveryPoint newDeliveryPoint = deliveryPointMapper.toEntity(deliveryPointRequest);
                newDeliveryPoint.setDeliveryDestination(deliveryDestinationToSave);
                deliveryDestinationToSave.getDeliveryPoints().add(newDeliveryPoint);
            }
        }
    }

    private DeliveryPointFilterResult validateDeliveryPoint(List<DeliveryPointRequest> deliveryPointRequests, List<DeliveryPoint> deliveryPoints) {
        Set<String> existingName = deliveryPoints.stream().map(dp -> dp.getName().trim().toUpperCase()).collect(Collectors.toSet());

        List<String> duplicates = deliveryPointRequests
                .stream()
                .map(deliverPointReq -> deliverPointReq.name().trim().toUpperCase())
                .filter(existingName::contains)
                .toList();

        List<DeliveryPointRequest> newUniqueDeliveryPoints = deliveryPointRequests.stream()
                .filter(dPointReq -> {
                    String nameKey = dPointReq.name().trim().toUpperCase();
                    return !existingName.contains(nameKey);
                })
                .toList();
        return new DeliveryPointFilterResult(newUniqueDeliveryPoints, duplicates);

    }

    private record DeliveryPointFilterResult(
            List<DeliveryPointRequest> newDeliveryPoints,
            List<String> duplicateDeliveryPoints
    ) {
    }

    /**
     * @param deliveryDestinationToSave
     * @param managedClient
     */
    private void linkedToNewDeliveryPoints(DeliveryDestination deliveryDestinationToSave, Client managedClient) {
        if (deliveryDestinationToSave.getDeliveryPoints() != null
                && !deliveryDestinationToSave.getDeliveryPoints().isEmpty()) {
            for (DeliveryPoint deliveryPoint : deliveryDestinationToSave.getDeliveryPoints()) {
                deliveryPoint.setClient(managedClient);
                deliveryPoint.setDeliveryDestination(deliveryDestinationToSave);
            }
        }
    }


    @Override
    public DeliveryDestinationResponse updateDeliveryDestinationInfo(Integer deliveryDestinationId, DeliveryDestinationRequest deliveryDestinationRequest) {

        if (deliveryDestinationId == null || deliveryDestinationId <= 0) {
            throw new InvalidOperationException("Delivery Destination ID Not Valid", ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);
        }
        checkClient(deliveryDestinationRequest.deliveryPoints().get(0).clientId());

        var exsistingDeliveryDestination = deliveryDestinationRepository.findById(deliveryDestinationId).orElseThrow(
                () -> new EntityNotFoundException("DeliveryPoint not found")
        );
        deliveryDestinationMapper.updateDeliveryDestinationFromRequest(deliveryDestinationRequest, exsistingDeliveryDestination);

        DeliveryDestination updatedDeliveryDestination = deliveryDestinationRepository.save(exsistingDeliveryDestination);

        return deliveryDestinationMapper.toResponse(updatedDeliveryDestination);
    }

    private void checkClient(Integer clientId) {
        clientRepository.findById(clientId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Client with ID :: " + clientId + " not found",
                        ErrorCodes.CLIENT_NOT_FOUND)
        );
    }


    /**
     * @param deliveryDestinationRequest
     * @return DeliveryDestination
     */
    private DeliveryDestination deliveryDestinationExists(DeliveryDestinationRequest deliveryDestinationRequest) {
        return deliveryDestinationRepository.
                findDeliveryDestinationsByDeliveryServiceId(deliveryDestinationRequest.deliveryServiceId()).orElse(null);
    }


    private void validateAllDeliveryPoints(List<DeliveryPoint> newDeliveryPoint, Set<String> existingPointNames) {

        List<String> duplicateNames = newDeliveryPoint.stream()
                .filter(dp -> existingPointNames.contains(dp.getName()))
                .map(DeliveryPoint::getName)
                .collect(Collectors.toList());

        if (!duplicateNames.isEmpty()) {
            throw new InvalidOperationException(
                    "Les points suivants existent déjà pour ce client: " + String.join(", ", duplicateNames),
                    ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION
            );
        }
    }

    /**
     * @param ddRequest
     * @param ddToSave
     */
    private void setAllDeliveryPoints(DeliveryDestinationRequest ddRequest, DeliveryDestination ddToSave) {

        if (ddRequest.deliveryPoints().isEmpty()) {
            return;
        }

        // return all dp already exist
        List<DeliveryPoint> existingPoints =
                deliveryPointRepository.findDeliveryPointByDeliveryServiceId(ddRequest.deliveryServiceId());
        // get all Names of the Existing dps
        Set<String> existingPointNames = existingPoints.stream()
                .map(DeliveryPoint::getName)
                .collect(Collectors.toSet());
/*       // return all new dps
        List<DeliveryPoint> newPoints = deliveryPointRepository.findAllById(ddRequest.deliveryPoint());*/

        // get the client
        Client client = clientRepository.findById(ddRequest.deliveryPoints().get(0).clientId()).orElseThrow(
                () -> new EntityNotFoundException("Client not found"));
        // Validate all delivery points before saving any of them

        List<DeliveryPoint> newPoints = ddRequest.deliveryPoints().stream().map(deliveryPointMapper::toEntity).toList();
        validateAllDeliveryPoints(newPoints, existingPointNames);
        List<DeliveryPoint> deliveryPointsToSave = new ArrayList<>();

        newPoints.forEach(deliveryPoint -> {
            DeliveryPoint deliveryPointToSave = deliveryPoint;
            deliveryPointToSave.setDeliveryDestination(ddToSave);
            deliveryPointToSave.setClient(client);
            deliveryPointsToSave.add(deliveryPointToSave);
        });
        deliveryPointRepository.saveAll(deliveryPointsToSave);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDestinationResponse findById(Integer id) {
        CheckParmasUtil.checkID(id, "Le parametre ID Delivery Destination est null",
                "Impossible de trouver un Delivery Destination avec un ID null", ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);

        DeliveryDestination DeliveryDestinationOp = deliveryDestinationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Opps! Aucun Delivery Destination avec l'id :" + id + "n'a ete trouvé dans la base de donnée",
                        ErrorCodes.DELIVERY_DESTINATION_NOT_FOUND));

        return deliveryDestinationMapper.toResponse(DeliveryDestinationOp);
    }


    @Override
    public List<DeliveryDestinationResponse> findAllByClientId(Integer clientId) {

        CheckParmasUtil.checkID(clientId, "Le parametre ID est null",
                "Impossible de trouver un Delivery Destination avec un parametre id client  null", ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);
        List<DeliveryDestination> deliveryDestinations = deliveryDestinationRepository.findAll();
        if (deliveryDestinations.isEmpty()) {
            throw new EntityNotFoundException("Opps! Aucun Delivery Destination avec l'id client :" + clientId + "n'a ete trouvé dans la base de donnée",
                    ErrorCodes.DELIVERY_DESTINATION_NOT_FOUND);
        }
        return deliveryDestinations.stream()
                .map(deliveryDestinationMapper::toResponse).toList();
    }

    @Override
    public List<DeliveryDestinationResponse> findAll() {

        return deliveryDestinationRepository.findAll().stream()
                .map(deliveryDestinationResponseMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Integer delivDestId) {
        if (delivDestId == null || delivDestId <= 0) {
            throw new InvalidEntityException("::ID not valid.", ErrorCodes.DELIVERY_SERVICE_NOT_VALID_OPERATION);
        }
        this.findById(delivDestId);

        List<DeliveryPoint> deliveryPoints = deliveryPointRepository.findDeliveryPointByDeliveryDestinationId(delivDestId);
        if (!deliveryPoints.isEmpty()) {
            throw new InvalidOperationException("Delivery Destination has at least one deliver point you can't deleted it",
                    ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);
        }
        List<Mission> missions = missionRepository.findAllByDeliveryDestinationId(delivDestId);
        if (!missions.isEmpty()) {
            throw new InvalidOperationException("Delivery Destination has at list one mission, you can't deleted it",
                    ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);
        }

        deliveryDestinationRepository.deleteById(delivDestId);
    }

    @Override
    public PageResponse<DeliveryDestinationResponse> findAllDeliveryDestinations(int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        Page<DeliveryDestination> deliveryDestinations = deliveryDestinationRepository.findAll(pageable);
        List<DeliveryDestinationResponse> deliveryDestinationsDto = deliveryDestinations.stream()
                .map(deliveryDestinationMapper::toResponse)
                .toList();

        return new PageResponse<>(
                deliveryDestinationsDto,
                deliveryDestinations.getNumber(),
                deliveryDestinations.getSize(),
                deliveryDestinations.getTotalElements(),
                deliveryDestinations.getTotalPages(),
                deliveryDestinations.isFirst(),
                deliveryDestinations.isLast()

        );
    }

    private void checkDto(DeliveryDestinationRequest request, String msgLog, String msgException) {
        List<String> errors = DeliveryDestinationValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error(msgLog, request);
            throw new InvalidEntityException(msgException, ErrorCodes.DELIVERY_POINT_NOT_FOUND, errors);
        }
    }
}
