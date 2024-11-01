package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.model.Chauffeur;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.ServiceDelivery;
import com.hitecart.deliverysystem.repository.IServiceDeliveryRepository;
import com.hitecart.deliverysystem.services.IServiceDeliveryService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.services.impl.CheckParmasUtil.checkID;

@Service
@RequiredArgsConstructor
public class ServiceDeliveryImpl implements IServiceDeliveryService {

        private final IServiceDeliveryRepository serviceDeliveryRepository;
    private final UtilisateurService utilisateurServiceImpl;

    @Override
    public ServiceDeliveryDto save(ServiceDeliveryDto dto) {
        /* TODO TEST and VALIDATE DTO*/
        ServiceDelivery serviceDeliveryToSave = ServiceDeliveryDto.toEntity(dto);
        return ServiceDeliveryDto.fromEntity(serviceDeliveryRepository.save(serviceDeliveryToSave));
    }

    @Override
    public ServiceDeliveryDto findById(Integer id) {
        ServiceDelivery serviceDeliveryToFind = serviceDeliveryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Service Delivery with ID " + id + " not found",
                        ErrorCodes.SERVICE_DELIVERY_NOT_FOUND));
        ServiceDeliveryDto serviceDeliveryDto = ServiceDeliveryDto.fromEntity(serviceDeliveryToFind);
        setCreatedByUser(serviceDeliveryDto);
        return serviceDeliveryDto;
    }


    @Override
    public List<ServiceDeliveryDto> findAll() {
        return serviceDeliveryRepository.findAll().stream()
                .map(this::updatedResponse).toList();
    }

    @Override
    public void delete(Integer id) {
        serviceDeliveryRepository.deleteById(id);
    }

    @Override
    public ServiceDeliveryDto updateEnabled(Integer id, boolean enabled) {
        checkID(id,"l'ID du chauffeur à supprimer est null",
                "Impossible de supprimer un chauffeur avec un ID null",
                ErrorCodes.SERVICE_DELIVERY_NOT_VALID_OPERATION);
        ServiceDelivery serviceDeliveryOp= serviceDeliveryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Oops! No driver with the ID: " + id + " was found in the database.",
                        ErrorCodes.CHAUFFEUR_NOT_FOUND));
        serviceDeliveryOp.setEnabled(enabled);
        return  ServiceDeliveryDto.fromEntity(serviceDeliveryRepository.save(serviceDeliveryOp));
    }

    @Override
    public List<ServiceDeliveryDto> findAllEnabled(boolean enabled) {
        return serviceDeliveryRepository.findAllByEnabled(enabled).stream()
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ServiceDeliveryDto> findAllServicesDelivery(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<ServiceDelivery> servicesD = serviceDeliveryRepository.findAllServicesDelivery(pageable);
        List<ServiceDeliveryDto> serviceDeliveryDtos = servicesD.stream()
                .map(this::updatedResponse)
                .toList();
        return new PageResponse<>(
                serviceDeliveryDtos,
                servicesD.getNumber(),
                servicesD.getSize(),
                servicesD.getTotalElements(),
                servicesD.getTotalPages(),
                servicesD.isFirst(),
                servicesD.isLast()
        );

    }


    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(ServiceDeliveryDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }
    private ServiceDeliveryDto updatedResponse(ServiceDelivery sd) {
        ServiceDeliveryDto sdDto=ServiceDeliveryDto.fromEntity(sd);
        setCreatedByUser(sdDto);
        return sdDto;
    }
}
