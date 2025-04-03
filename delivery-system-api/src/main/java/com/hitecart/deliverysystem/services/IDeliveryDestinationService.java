package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.DeliveryDestinationDto;
import com.hitecart.deliverysystem.dto.basic.DeliveryDestinationResponse;

import java.util.List;

public interface IDeliveryDestinationService {
    DeliveryDestinationDto save(DeliveryDestinationDto dto);
    DeliveryDestinationDto findById(Integer id);
    List<DeliveryDestinationResponse> findAll();
    void delete(Integer id);
    PageResponse<DeliveryDestinationDto> findAllDeliveryDestinations(int page, int size);
    List<DeliveryDestinationDto> findAllByClientId(Integer clientId);
}
