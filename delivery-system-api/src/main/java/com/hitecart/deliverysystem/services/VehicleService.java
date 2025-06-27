package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.vehicle.VehicleRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;

import java.util.List;

public interface VehicleService {
    VehicleResponse save(VehicleRequest request);

    VehicleResponse updateVehicleInfo(Integer vehicleId, VehicleRequest request);

    VehicleResponse findById(Integer id);

    VehicleResponse findByRegistrationNbr(String immat);

    List<VehicleResponse> findAll();

    void delete(Integer id);

    VehicleResponse updateIsActive(Integer id, Boolean isActive);

    PageResponse<VehicleResponse> findAllVehicles(int page, int size);
}
