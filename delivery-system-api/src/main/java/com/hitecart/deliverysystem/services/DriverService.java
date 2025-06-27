package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.dto.driver.DriverResponse;
import com.hitecart.deliverysystem.dto.driver.DriverUpdateRequest;

import java.util.List;

public interface DriverService {
    Integer save(DriverRequest request);
    Integer updateDriverInfo(Integer driverId, DriverUpdateRequest request);
    DriverResponse findById(Integer id);
    DriverResponse findByEmail(String email);
    List<DriverResponse> findAll();
    void delete(Integer id);
    DriverResponse updateIsActive(Integer id, Boolean isActive);
    List<DriverResponse> findAllIsActive(Boolean isActive);
    PageResponse<DriverResponse> findAllDrivers(int page, int size);
}
