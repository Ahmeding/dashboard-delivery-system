package com.hitecart.deliverysystem.controllers.vehicle;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.VehicleApi;
import com.hitecart.deliverysystem.dto.vehicle.VehicleRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;
import com.hitecart.deliverysystem.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class VehicleController implements VehicleApi {


    private final VehicleService vehicleService;

    @Override
    public ResponseEntity<VehicleResponse> saveVehicle(VehicleRequest vehicleRequest) {
        return ResponseEntity.ok(vehicleService.save(vehicleRequest));
    }

    @Override
    public ResponseEntity<VehicleResponse> updateVehicleInfo(Integer vehicleId, VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicleInfo(vehicleId, request));
    }

    @Override
    public ResponseEntity<VehicleResponse> findVehicleById(Integer id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @Override
    public ResponseEntity<VehicleResponse> findVehicleByRegisterNbr(String registerNbr) {
        return ResponseEntity.ok(vehicleService.findByRegistrationNbr(registerNbr));
    }

    @Override
    public ResponseEntity<List<VehicleResponse>> findAllVehicle() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @Override
    public ResponseEntity<VehicleResponse> updateVehicleIsActive(Integer id, Boolean isActive) {
        return ResponseEntity.ok(vehicleService.updateIsActive(id,isActive));
    }

    @Override
    public  ResponseEntity<?> deleteVehicle(Integer id) {
        try {
            vehicleService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Vehicle deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Vehicle not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<VehicleResponse>> findAllVehiclesPageable(int page, int size) {
        return ResponseEntity.ok(vehicleService.findAllVehicles(page,size));
    }
}
