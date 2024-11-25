package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.DeliveryDestinationDto;

import java.util.List;

public interface IDeliveryDestinationService {
    DeliveryDestinationDto save(DeliveryDestinationDto dto);
    DeliveryDestinationDto findById(Integer id);
    List<DeliveryDestinationDto> findAll();
    void delete(Integer id);
    PageResponse<DeliveryDestinationDto> findAllDeliveryDestinations(int page, int size);
    List<DeliveryDestinationDto> findAllByClientId(Integer clientId);
}
