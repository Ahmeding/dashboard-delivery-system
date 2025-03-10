package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.DeliveryPointDto;

import java.util.List;

public interface IDeliveryPointService {
    DeliveryPointDto save(DeliveryPointDto dto);

    DeliveryPointDto findById(Integer id);

    List<DeliveryPointDto> findAllByCity(String city);

    List<DeliveryPointDto> findAll();

    DeliveryPointDto updateIsActive(Integer id, Boolean isActive);

    void delete(Integer id);

    PageResponse<DeliveryPointDto> findAllDeliveryPoints(int page, int size);
}
