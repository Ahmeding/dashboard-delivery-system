package com.hitecart.deliverysystem.controllers.delivery;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DeliveryDestinationApi;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationRequest;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationResponse;
import com.hitecart.deliverysystem.services.DeliveryDestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class DeliveryDestinationController implements DeliveryDestinationApi {

    private final DeliveryDestinationService deliveryDestinationService;

    @Override
    public ResponseEntity<DeliveryDestinationResponse> saveDeliveryDestination(DeliveryDestinationRequest deliveryDestinationRequest) {
        return ResponseEntity.ok(
                deliveryDestinationService.save(deliveryDestinationRequest)
        );
    }

    @Override
    public ResponseEntity<DeliveryDestinationResponse> updateDeliveryDestinationInfo(Integer deliveryDestinationId,DeliveryDestinationRequest deliveryDestinationRequest) {
        return ResponseEntity.ok(deliveryDestinationService.updateDeliveryDestinationInfo(deliveryDestinationId,deliveryDestinationRequest));
    }

    @Override
    public ResponseEntity<DeliveryDestinationResponse> findDeliveryDestinationById(Integer id) {
        return ResponseEntity.ok(
                deliveryDestinationService.findById(id)
        );
    }

    @Override
    public ResponseEntity<List<DeliveryDestinationResponse>> findAllDeliveryDestinationByClientId(Integer clientId) {
        return ResponseEntity.ok(
                deliveryDestinationService.findAllByClientId(clientId)
        );
    }

    @Override
    public ResponseEntity<List<DeliveryDestinationResponse>> findAllDeliveryDestination() {
        return ResponseEntity.ok(
                deliveryDestinationService.findAll()
        );
    }


    @Override
    public  ResponseEntity<?> deleteDeliveryDestination(Integer id) {
        try {
            deliveryDestinationService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "DeliveryDestination deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "DeliveryDestination not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<DeliveryDestinationResponse>> findAllDeliveryDestinationsPageable(int page, int size) {
        return ResponseEntity.ok(
                deliveryDestinationService.findAllDeliveryDestinations(page, size)
        );
    }
}
