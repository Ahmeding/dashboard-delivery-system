package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DeliveryDestinationApi;
import com.hitecart.deliverysystem.dto.DeliveryDestinationDto;
import com.hitecart.deliverysystem.dto.DeliveryDestinationResponse;
import com.hitecart.deliverysystem.services.IDeliveryDestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class DeliveryDestinationController implements DeliveryDestinationApi {

    private final IDeliveryDestinationService deliveryDestinationService;

    @Override
    public ResponseEntity<DeliveryDestinationDto> save(DeliveryDestinationDto dto) {
        System.out.println(dto.toString());
        return ResponseEntity.ok(
                deliveryDestinationService.save(dto)
                );
    }

    @Override
    public ResponseEntity<DeliveryDestinationDto> findById(Integer id) {
        return ResponseEntity.ok(
                deliveryDestinationService.findById(id)
                );
    }

    @Override
    public ResponseEntity<List<DeliveryDestinationDto>> findAllByClientId(Integer clientId) {
        return ResponseEntity.ok(
                deliveryDestinationService.findAllByClientId(clientId)
        );
    }

    @Override
    public ResponseEntity<List<DeliveryDestinationResponse>> findAll() {
        return ResponseEntity.ok(
                deliveryDestinationService.findAll()
                );
    }


    @Override
    public void delete(Integer id) {
        deliveryDestinationService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<DeliveryDestinationDto>> findAllDeliveryDestinations(int page, int size) {
        return ResponseEntity.ok(
                deliveryDestinationService.findAllDeliveryDestinations(page, size)
                );
    }
}
