package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DeliveryPointApi;
import com.hitecart.deliverysystem.dto.DeliveryPointDto;
import com.hitecart.deliverysystem.services.IDeliveryPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeliveryPointController implements DeliveryPointApi {

    private final IDeliveryPointService deliveryPointService;

    @Override
    public ResponseEntity<DeliveryPointDto> save(DeliveryPointDto dto) {
        return ResponseEntity.ok(deliveryPointService.save(dto));
    }

    @Override
    public ResponseEntity<DeliveryPointDto> findById(Integer id) {
        return  ResponseEntity.ok(deliveryPointService.findById(id));
    }

    @Override
    public ResponseEntity<DeliveryPointDto> findByCity(String city) {
        return ResponseEntity.ok(deliveryPointService.findByCity(city));
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
