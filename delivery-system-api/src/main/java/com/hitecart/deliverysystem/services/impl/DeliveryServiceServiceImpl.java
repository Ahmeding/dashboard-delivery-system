package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.DeliveryDestinationRepository;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceRequest;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.DeliveryServiceMapper;
import com.hitecart.deliverysystem.model.DeliveryService;
import com.hitecart.deliverysystem.dao.DeliveryServiceRepository;
import com.hitecart.deliverysystem.services.DeliveryServiceService;
import com.hitecart.deliverysystem.utils.CapitalizeFirstLetter;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.DeliveryServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.utils.CheckParmasUtil.checkID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceServiceImpl implements DeliveryServiceService {

    private final DeliveryServiceRepository serviceDeliveryRepository;
    private final DeliveryServiceMapper deliveryServiceMapper;
    private final DeliveryServiceRepository deliveryServiceRepository;
    private final DeliveryDestinationRepository deliveryDestinationRepository;

    /**
     *
     * @param deliveryServiceRequest
     * @param msgLog
     * @param msgException
     * @param requesttNotValid
     */
    private void checkRequestValidating(DeliveryServiceRequest deliveryServiceRequest, String msgLog, String msgException, ErrorCodes requesttNotValid) {
        List<String> errors = DeliveryServiceValidator.validate(deliveryServiceRequest);
        if(!errors.isEmpty()){
            log.error(msgLog,deliveryServiceRequest);
            throw new InvalidEntityException(msgException, requesttNotValid,errors);
        }
    }

    @Override
    public DeliveryServiceResponse save(DeliveryServiceRequest deliveryServiceRequest) {

        checkRequestValidating(
                deliveryServiceRequest,
                "DeliveryServiceRequest Not Valid",
                "DeliveryServiceRequest Not Valid",
                ErrorCodes.DELIVERY_SERVICE_NOT_FOUND);
        return savingDeliveryService(deliveryServiceRequest);
    }

    private DeliveryServiceResponse savingDeliveryService(DeliveryServiceRequest deliveryServiceRequest) {
        DeliveryService deliveryServiceToSave = deliveryServiceMapper.toEntity(deliveryServiceRequest);


        deliveryServiceToSave.setTitle(
                CapitalizeFirstLetter.capitalizeFirstLetter(deliveryServiceRequest.destinationCity().trim()));
        deliveryServiceToSave.setDestinationCity(
                CapitalizeFirstLetter.capitalizeFirstLetter(deliveryServiceRequest.destinationCity().trim()));
        deliveryServiceToSave.setDestinationCountry(
                CapitalizeFirstLetter.capitalizeFirstLetter(deliveryServiceRequest.destinationCountry().trim()));
        // check duplicates
        checkExists(deliveryServiceToSave);

        return deliveryServiceMapper.toResponse(serviceDeliveryRepository.save(deliveryServiceToSave));
    }

    /**
     *
     * @param deliveryService
     */
    private void checkExists(DeliveryService deliveryService) {
        Integer excludeId =
                deliveryService.getId() != null ?
                deliveryService.getId() :
                -1;

        serviceDeliveryRepository
                .findDeliveryServiceByDestinationCityAndAndDestinationCountry(
                        deliveryService.getDestinationCity(), deliveryService.getDestinationCountry(), excludeId)
                .ifPresent(existing->{
                    throw new InvalidOperationException("Delivery Service already exists" + excludeId,
                            ErrorCodes.DELIVERY_SERVICE_NOT_VALID_OPERATION);
                });
    }


    @Override
    public DeliveryServiceResponse updateDeliveryServiceInfo(Integer deliveryServiceId,DeliveryServiceRequest deliveryServiceRequest) {

        if(deliveryServiceId == null || deliveryServiceId ==0){
            throw new InvalidOperationException("", ErrorCodes.DELIVERY_SERVICE_NOT_VALID_OPERATION);
        }
        var existingDeliverServiceToUpdate = deliveryServiceRepository.findById(deliveryServiceId).orElseThrow(
                ()-> new EntityNotFoundException("Delivery service with ::ID : "+
                        deliveryServiceId +" not found")
        );
        //
        deliveryServiceMapper.updateDeliveryServiceFromRequest(deliveryServiceRequest, existingDeliverServiceToUpdate);
        //
        existingDeliverServiceToUpdate.setTitle(
                CapitalizeFirstLetter.capitalizeFirstLetter(existingDeliverServiceToUpdate.getTitle().trim()));
        existingDeliverServiceToUpdate.setDestinationCity(
                CapitalizeFirstLetter.capitalizeFirstLetter(existingDeliverServiceToUpdate.getDestinationCity().trim()));
        existingDeliverServiceToUpdate.setDestinationCountry(
                CapitalizeFirstLetter.capitalizeFirstLetter(existingDeliverServiceToUpdate.getDestinationCountry().trim()));

        // check duplicates
        checkExists(existingDeliverServiceToUpdate);

        DeliveryService updatedDeliveryService  = deliveryServiceRepository.save(existingDeliverServiceToUpdate);

        return deliveryServiceMapper.toResponse(updatedDeliveryService);
    }

    @Override
    public DeliveryServiceResponse findById(Integer id) {
        CheckParmasUtil.checkID(id, "Le parametre ID  est null",
                "Impossible de trouver un Delivery Service avec un ID null", ErrorCodes.DELIVERY_SERVICE_NOT_FOUND);

        DeliveryService deliveryServiceToFind = serviceDeliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service Delivery with ID " + id + " not found",
                        ErrorCodes.DELIVERY_SERVICE_NOT_FOUND));

        return deliveryServiceMapper.toResponse(deliveryServiceToFind);
    }


    @Override
    public List<DeliveryServiceResponse> findAll() {
        return serviceDeliveryRepository.findAll().stream()
                .map(deliveryServiceMapper::toResponse).toList();
    }

    @Override
    public void delete(Integer id) {

        if(id == null || id <= 0) {
            throw new InvalidEntityException("::ID not valid.", ErrorCodes.DELIVERY_SERVICE_NOT_VALID_OPERATION);
        }
        this.findById(id);
       deliveryDestinationRepository.findDeliveryDestinationsByDeliveryServiceId(id).ifPresent(dd -> {
            throw new InvalidOperationException(
                    "Cannot delete DeliveryService with ID="
                            + id
                            + " because it has associated DeliveryDestination(s).",
                    ErrorCodes.DELIVERY_SERVICE_NOT_VALID_OPERATION
            );
        });

        serviceDeliveryRepository.deleteById(id);
    }

    @Override
    public DeliveryServiceResponse updateEnabled(Integer id, boolean enabled) {
        checkID(id, "l'ID du driver à supprimer est null",
                "Impossible de supprimer un driver avec un ID null",
                ErrorCodes.DELIVERY_SERVICE_NOT_VALID_OPERATION);
        DeliveryService deliveryServiceToUpdate = serviceDeliveryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Oops! No driver with the ID: " + id + " was found in the database.",
                        ErrorCodes.DRIVER_NOT_FOUND));
        deliveryServiceToUpdate.setEnabled(enabled);
        return deliveryServiceMapper.toResponse(serviceDeliveryRepository.save(deliveryServiceToUpdate));
    }

    @Override
    public List<DeliveryServiceResponse> findAllEnabled(boolean enabled) {
        return serviceDeliveryRepository.findAllByEnabled(enabled).stream()
                .map(deliveryServiceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<DeliveryServiceResponse> findAllServicesDelivery(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<DeliveryService> servicesD = serviceDeliveryRepository.findAllServicesDelivery(pageable);
        List<DeliveryServiceResponse> deliveryServiceBasicDtos = servicesD.stream()
                .map(deliveryServiceMapper::toResponse)
                .toList();
        return new PageResponse<>(
                deliveryServiceBasicDtos,
                servicesD.getNumber(),
                servicesD.getSize(),
                servicesD.getTotalElements(),
                servicesD.getTotalPages(),
                servicesD.isFirst(),
                servicesD.isLast()
        );

    }
}
