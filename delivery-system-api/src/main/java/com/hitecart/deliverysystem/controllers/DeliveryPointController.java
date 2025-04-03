package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DeliveryPointApi;
import com.hitecart.deliverysystem.dto.basic.DeliveryPointDto;
import com.hitecart.deliverysystem.services.IDeliveryPointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag( name = "Delivery Point")
@RequiredArgsConstructor
public class DeliveryPointController implements DeliveryPointApi {

    private final IDeliveryPointService deliveryPointService;

    @Override
    public ResponseEntity<DeliveryPointDto> save(DeliveryPointDto dto) {
        return ResponseEntity.ok(
                deliveryPointService.save(dto)
                );
    }

    @Override
    public ResponseEntity<DeliveryPointDto> findById(Integer id) {
        return  ResponseEntity.ok(deliveryPointService.findById(id));
    }

    @Override
    public ResponseEntity<List<DeliveryPointDto>> findAllByCity(String city) {
        return ResponseEntity.ok(deliveryPointService.findAllByCity(city));
    }

    @Override
    public ResponseEntity<List<DeliveryPointDto>> findAll() {
        return ResponseEntity.ok(deliveryPointService.findAll());
    }

    @Override
    public ResponseEntity<DeliveryPointDto> updateIsActive(Integer id, Boolean isActive) {
        return ResponseEntity.ok(deliveryPointService.updateIsActive(id,isActive));
    }

    @Override
    public void delete(Integer id){
        deliveryPointService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<DeliveryPointDto>> findAllDeliveryPoints(int page, int size) {
        return ResponseEntity.ok(deliveryPointService.findAllDeliveryPoints(page,size));
    }
}
