package com.hitecart.deliverysystem.controllers.delivery;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DeliveryPointApi;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointRequest;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointResponse;
import com.hitecart.deliverysystem.services.DeliveryPointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Tag( name = "Delivery Point")
@RequiredArgsConstructor
public class DeliveryPointController implements DeliveryPointApi {

    private final DeliveryPointService deliveryPointService;

    @Override
    public ResponseEntity<DeliveryPointResponse> saveDeliveryPoint(DeliveryPointRequest deliveryPointRequest) {
        return ResponseEntity.ok(
                deliveryPointService.save(deliveryPointRequest)
                );
    }

    @Override
    public ResponseEntity<DeliveryPointResponse> updateDeliveryPointInfo(Integer deliveryPointId,DeliveryPointRequest deliveryPointRequest) {
        return ResponseEntity.ok(deliveryPointService.updateDeliveryPointInfo(deliveryPointId,deliveryPointRequest));
    }

    @Override
    public ResponseEntity<DeliveryPointResponse> findDeliveryPointById(Integer id) {
        return  ResponseEntity.ok(deliveryPointService.findById(id));
    }

    @Override
    public ResponseEntity<List<DeliveryPointResponse>> findAllDeliveryPointByCity(String city) {
        return ResponseEntity.ok(deliveryPointService.findAllByCity(city));
    }

    @Override
    public ResponseEntity<List<DeliveryPointResponse>> findAllDeliveryPoint() {
        return ResponseEntity.ok(deliveryPointService.findAll());
    }

    @Override
    public ResponseEntity<DeliveryPointResponse> updateDeliveryPointIsActive(Integer id, Boolean isActive) {
        return ResponseEntity.ok(deliveryPointService.updateIsActive(id,isActive));
    }

    @Override
    public  ResponseEntity<?> deleteDeliveryPoint(Integer id){
        try {
            deliveryPointService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "DeliveryPoint deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "DeliveryPoint not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<DeliveryPointResponse>> findAllDeliveryPointsPageable(int page, int size) {
        return ResponseEntity.ok(deliveryPointService.findAllDeliveryPoints(page,size));
    }
}
