package com.hitecart.deliverysystem.controllers.driver;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DriverApi;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.dto.driver.DriverResponse;
import com.hitecart.deliverysystem.dto.driver.DriverUpdateRequest;
import com.hitecart.deliverysystem.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DriverController implements DriverApi {

    private final DriverService driverService;

    @Override
    public ResponseEntity<Integer> saveDriver(DriverRequest driverRequest) {
        return  ResponseEntity.ok(driverService.save(driverRequest));
    }

    @Override
    public ResponseEntity<Integer> updateDriverInfo(Integer driverId, DriverUpdateRequest request) {
        return ResponseEntity.ok(driverService.updateDriverInfo(driverId, request));
    }

    @Override
    public ResponseEntity<DriverResponse> findDriverById(Integer id) {
        return ResponseEntity.ok(driverService.findById(id));
    }

    @Override
    public ResponseEntity<DriverResponse> findDriverByEmail(String email) {
        return ResponseEntity.ok(driverService.findByEmail(email));
    }

    @Override
    public ResponseEntity<List<DriverResponse>> findAllDriverIsActive(Boolean isActive) {
        return ResponseEntity.ok(driverService.findAllIsActive(isActive));
    }


    @Override
    public ResponseEntity<List<DriverResponse>> findAllDriver() {
        return ResponseEntity.ok(driverService.findAll());
    }

    @Override
    public ResponseEntity<DriverResponse> updateDriverIsActive(Integer id, Boolean isActive) {
        return ResponseEntity.ok(driverService.updateIsActive(id,isActive));
    }

    @Override
    public ResponseEntity<PageResponse<DriverResponse>> findAllDriversPageable(int page, int size) {
        return ResponseEntity.ok(driverService.findAllDrivers(page, size));
    }

    @Override
    public  ResponseEntity<?> deleteDriver(Integer id) {
        try {
            driverService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Driver deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Driver not found with ID: " + id
                    ));
        }
    }
}
