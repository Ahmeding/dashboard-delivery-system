package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointRequest;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointResponse;

import java.util.List;

public interface DeliveryPointService {

    DeliveryPointResponse save(DeliveryPointRequest dto);

    DeliveryPointResponse updateDeliveryPointInfo(Integer deliveryPointId,DeliveryPointRequest deliveryPointRequest);

    DeliveryPointResponse findById(Integer id);

    List<DeliveryPointResponse> findAllByCity(String city);

    List<DeliveryPointResponse> findAll();

    DeliveryPointResponse updateIsActive(Integer id, Boolean isActive);

    void delete(Integer id);

    PageResponse<DeliveryPointResponse> findAllDeliveryPoints(int page, int size);
}
