package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ChauffeurDto;
import com.hitecart.deliverysystem.dto.ServiceDeliveryDto;
import com.hitecart.deliverysystem.model.ServiceDelivery;
import com.hitecart.deliverysystem.repository.IServiceDeliveryRepository;

import java.util.List;

public interface IServiceDeliveryService {
    ServiceDeliveryDto save(ServiceDeliveryDto dto);
    ServiceDeliveryDto findById(Integer id);
    List<ServiceDeliveryDto> findAll();
    void delete(Integer id);
    ServiceDeliveryDto updateEnabled(Integer id, boolean isActive);
    List<ServiceDeliveryDto> findAllEnabled(boolean enabled);
    PageResponse<ServiceDeliveryDto> findAllServicesDelivery(int page, int size);
}
