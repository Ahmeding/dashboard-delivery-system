package com.hitecart.deliverysystem.controllers.delivery;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DeliveryServiceApi;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceRequest;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceResponse;
import com.hitecart.deliverysystem.services.DeliveryServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DeliveryServiceController implements DeliveryServiceApi {

    private final DeliveryServiceService deliveryService;


    @Override
    public ResponseEntity<DeliveryServiceResponse> saveDeliveryService(DeliveryServiceRequest deliveryServiceRequest) {
        return ResponseEntity.ok(deliveryService.save(deliveryServiceRequest));
    }

    @Override
    public ResponseEntity<DeliveryServiceResponse> updateDeliveryServiceInfo(Integer deliveryServiceId, DeliveryServiceRequest deliveryServiceRequest) {
        return ResponseEntity.ok(deliveryService.updateDeliveryServiceInfo(deliveryServiceId,deliveryServiceRequest));
    }

    @Override
    public ResponseEntity<DeliveryServiceResponse> findDeliveryServiceById(Integer id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @Override
    public ResponseEntity<List<DeliveryServiceResponse>> findAllDeliveryService() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    @Override
    public ResponseEntity<DeliveryServiceResponse> updateDeliveryServiceEnabled(Integer id, boolean enabled) {
        return ResponseEntity.ok(deliveryService.updateEnabled(id, enabled));
    }

    @Override
    public ResponseEntity<List<DeliveryServiceResponse>> findAllEnabledDeliveryService(boolean enabled) {
        return ResponseEntity.ok(deliveryService.findAllEnabled(enabled));
    }

    @Override
    public ResponseEntity<PageResponse<DeliveryServiceResponse>> findAllDeliveryServicesPageable(int page, int size) {
        return ResponseEntity.ok(deliveryService.findAllServicesDelivery(page,
                size));
    }

    @Override
    public  ResponseEntity<?> deleteDeliveryService(Integer id) {
        try {
            deliveryService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "DeliveryService deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "DeliveryService not found with ID: " + id
                    ));
        }
    }


}
