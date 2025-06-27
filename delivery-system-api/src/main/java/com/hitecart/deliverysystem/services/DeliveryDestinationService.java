package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationRequest;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationResponse;

import java.util.List;

public interface DeliveryDestinationService {
    DeliveryDestinationResponse save(DeliveryDestinationRequest deliveryDestinationRequest);
    DeliveryDestinationResponse updateDeliveryDestinationInfo(Integer deliveryDestinationId, DeliveryDestinationRequest deliveryDestinationRequest);
    DeliveryDestinationResponse findById(Integer id);
    List<DeliveryDestinationResponse> findAll();
    void delete(Integer id);
    PageResponse<DeliveryDestinationResponse> findAllDeliveryDestinations(int page, int size);
    List<DeliveryDestinationResponse> findAllByClientId(Integer clientId);
}
