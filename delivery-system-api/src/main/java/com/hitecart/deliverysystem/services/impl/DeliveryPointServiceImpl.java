package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationResponse;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointRequest;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.DeliveryPointMapper;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.services.DeliveryPointService;
import com.hitecart.deliverysystem.services.UserService;
import com.hitecart.deliverysystem.services.DeliveryDestinationService;
import com.hitecart.deliverysystem.utils.CapitalizeFirstLetter;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.DeliveryPointValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryPointServiceImpl implements DeliveryPointService {

    private final DeliveryPointRepository deliveryPointRepository;
    private final DeliveryServiceRepository serviceDeliveryRepository;

    private final UserService userService;
    private final DeliveryPointMapper deliveryPointMapper;
    private final DeliveryDestinationService deliveryDestinationService;
    private final DeliveryDestinationRepository deliveryDestinationRepository;
    private final DeliveryPointCategoryRepository deliveryPointCategoryRepository;
    private final ClientRepository clientRepository;

    @Override
    public DeliveryPointResponse save(DeliveryPointRequest request) {
        checkDto(request,"DeliveryPoint not valid","le DeliveryPoint n'est pas valid");
    return savingDeliveryPoint(request);
    }

    /**
     *
     * @param request
     * @return DeliveryPointResponse
     */
    private DeliveryPointResponse savingDeliveryPoint(DeliveryPointRequest request){ Client currentClient  =  clientRepository.findById(request.clientId()).orElseThrow(
            ()-> new EntityNotFoundException("Client with ::ID : "+request.clientId() + " not found"));

        DeliveryDestination currentDeliveryDestination
                = deliveryDestinationRepository.findById(request.deliveryDestinationId()).orElseThrow(
                ()-> new EntityNotFoundException("Delivery Destination with ::ID : "+request.deliveryDestinationId() + " not found"));

        DeliveryPointCategory deliveryPointCategory = deliveryPointCategoryRepository.findById(request.categoryId()).orElseThrow(
                ()-> new EntityNotFoundException("Delivery Point Category with ::ID : "+request.categoryId() + " not found")
        );



        DeliveryPoint deliveryPointToSave = deliveryPointMapper.toEntity(request);

        deliveryPointToSave.setDeliveryDestination(currentDeliveryDestination);
        deliveryPointToSave.setClient(currentClient);
        deliveryPointToSave.setDeliveryPointCategory(deliveryPointCategory);
        deliveryPointToSave.setName(CapitalizeFirstLetter.capitalizeFirstLetter(deliveryPointToSave.getName().trim()));

        checkExists(deliveryPointToSave);

        return deliveryPointMapper.toResponse(deliveryPointRepository.save(deliveryPointToSave));
    }

    private void checkExists(DeliveryPoint deliveryPoint) {
        Integer excludeId =
                deliveryPoint.getId() != null ?
                        deliveryPoint.getId() :
                        -1;
            deliveryPointRepository.findByNameAndCity(
                    deliveryPoint.getName(),
                    deliveryPoint.getDeliveryDestination().getDeliveryService().getDestinationCity(),
                            excludeId)
                    .ifPresent(existing->{
                        throw new InvalidOperationException("Delivery point alredy exist ",
                                ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_VALID_OPERATION);
                    });
    }


    @Override
    public DeliveryPointResponse updateDeliveryPointInfo(Integer deliveryPointId,DeliveryPointRequest request) {
        if(deliveryPointId == null || deliveryPointId == 0){
            throw new InvalidOperationException("Delivery Point ID is null",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);
        }
        var existingDeliveryPoint = deliveryPointRepository.findById(deliveryPointId).orElseThrow(
                ()-> new EntityNotFoundException("Delivery Point with ::ID : "+deliveryPointId + " not found")
        );
        deliveryPointMapper.updateDeliveryPointFromRequest(request,existingDeliveryPoint);

        existingDeliveryPoint.setName(CapitalizeFirstLetter.capitalizeFirstLetter(existingDeliveryPoint.getName().trim()));

        checkExists(existingDeliveryPoint);

        DeliveryPoint updatedDeliveryPoint = deliveryPointRepository.save(existingDeliveryPoint);
        return deliveryPointMapper.toResponse(updatedDeliveryPoint);
    }

    private DeliveryDestinationResponse getDeliveryDestination(Integer ddestinationId){
        if(ddestinationId == null){
            throw new InvalidOperationException("Can't find any DeliveryDestination with Null ID",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);
        }
        return deliveryDestinationService.findById(ddestinationId);
    }


    @Override
    public DeliveryPointResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,"The parameter DeliveryPoint ID is null",
                "Cannot find a DeliveryPoint with a null ID",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);

        DeliveryPoint deliveryPointById = deliveryPointRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException( "Oops! No DeliveryPoint with ID: " + id + " was found in the database.",
                        ErrorCodes.DELIVERY_POINT_NOT_FOUND));
        return deliveryPointMapper.toResponse(deliveryPointById);
    }

    @Override
    public List<DeliveryPointResponse> findAllByCity(String city) {
        CheckParmasUtil.checkStringParma(city,"The parameter DeliveryPoint city is null",
                "Cannot find a DeliveryPoint with a null city",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);
        List<DeliveryPoint> deliveryPoints= deliveryPointRepository.findAllByAddress_City(city);
        if(deliveryPoints.isEmpty()){
            throw new EntityNotFoundException(
                            "Oops! No DeliveryPoint with City: " + city + " was found in the database.",
                            ErrorCodes.DELIVERY_POINT_NOT_FOUND
                    );
        }
        return deliveryPoints.stream()
                .map(deliveryPointMapper::toResponse)
                .toList();
    }

    @Override
    public List<DeliveryPointResponse> findAll() {
        return deliveryPointRepository.findAll().stream()
                .map(deliveryPointMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryPointResponse updateIsActive(Integer id, Boolean enabled) {
        CheckParmasUtil.checkID(id,"The parameter DeliveryPoint ID is null",
        "Cannot find a DeliveryPoint with a null ID",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);

        DeliveryPoint deliveryPointToUpdateSatatus = deliveryPointRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Opps! Aucun deliveryPoint avec l'id :"
                        + id + "n'a ete trouvé dans la base deonnée",
                        ErrorCodes.CLIENT_NOT_FOUND)
        );

        deliveryPointToUpdateSatatus.setEnabled(enabled);

        return deliveryPointMapper.toResponse(deliveryPointRepository.save(deliveryPointToUpdateSatatus));
    }

    @Override
    public void delete(Integer deliveryPointId) {

        if(deliveryPointId == null || deliveryPointId <= 0) {
            throw new InvalidEntityException("::ID not valid.", ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);
        }
        this.findById(deliveryPointId);

        Set<DayOfWeek> daysOfWeek = checkHaveDaysOfWeek(deliveryPointId);
        if(!daysOfWeek.isEmpty()){
            throw new InvalidOperationException("DeliveryPoint has  at least one reccurent days of week , you can't deleted it",
                    ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);
        }
        deliveryPointRepository.deleteById(deliveryPointId);
    }

    /**
     *
     * @param id
     * @return
     */
   private Set<DayOfWeek> checkHaveDaysOfWeek(Integer id){
       DeliveryPoint deliveryPoint =  deliveryPointRepository.findDeliveryPointWithRecurrent(id);
       return deliveryPoint.getRecurrencePattern().getDaysOfWeek();

   }
    @Override
    public PageResponse<DeliveryPointResponse> findAllDeliveryPoints(int page, int size) {
        if(page<=0){page=1;}
        if(size<=0){size= 5;}
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        Page<DeliveryPoint> deliveryPoints = deliveryPointRepository.findAll(pageable);
        List<DeliveryPointResponse> deliveryPointsDto = deliveryPoints.stream()
                .map(deliveryPointMapper::toResponse)
                .toList();


        return new PageResponse<>(
                deliveryPointsDto,
                deliveryPoints.getNumber(),
                deliveryPoints.getSize(),
                deliveryPoints.getTotalElements(),
                deliveryPoints.getTotalPages(),
                deliveryPoints.isFirst(),
                deliveryPoints.isLast()
        );
    }



    private void checkDto(DeliveryPointRequest request, String msgLog, String msgException) {
        List<String> errors = DeliveryPointValidator.validate(request);
        if(!errors.isEmpty()){
            log.error(msgLog,request);
            throw new InvalidEntityException(msgException, ErrorCodes.DELIVERY_POINT_NOT_FOUND,errors);
        }
    }
}
