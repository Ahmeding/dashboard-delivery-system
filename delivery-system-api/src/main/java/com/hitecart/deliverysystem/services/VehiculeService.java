package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.VehicleDto;

import java.util.List;

public interface VehiculeService {
    VehicleDto save(VehicleDto dto);
    VehicleDto findById(Integer id);
    VehicleDto findByImmatriculation(String immat);
    List<VehicleDto> findAll();
    void delete(Integer id);
    VehicleDto updateIsActive(Integer id, Boolean isActive);

    PageResponse<VehicleDto> findAllVehicles(int page, int size);
}
