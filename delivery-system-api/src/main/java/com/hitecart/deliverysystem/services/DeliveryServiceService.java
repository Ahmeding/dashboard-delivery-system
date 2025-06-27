package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceRequest;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceResponse;

import java.util.List;

public interface DeliveryServiceService {
    DeliveryServiceResponse save(DeliveryServiceRequest dto);
    DeliveryServiceResponse updateDeliveryServiceInfo(Integer deliveryServiceId,DeliveryServiceRequest deliveryServiceRequest);
    DeliveryServiceResponse findById(Integer id);
    List<DeliveryServiceResponse> findAll();
    void delete(Integer id);
    DeliveryServiceResponse updateEnabled(Integer id, boolean isActive);
    List<DeliveryServiceResponse> findAllEnabled(boolean enabled);
    PageResponse<DeliveryServiceResponse> findAllServicesDelivery(int page, int size);
}
