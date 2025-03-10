package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.ServiceDeliveryApi;
import com.hitecart.deliverysystem.dto.ServiceDeliveryDto;
import com.hitecart.deliverysystem.services.IServiceDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceDeliveryController  implements ServiceDeliveryApi {

    private final IServiceDeliveryService deliveryService;


    @Override
    public ResponseEntity<ServiceDeliveryDto> save(ServiceDeliveryDto dto) {
        return ResponseEntity.ok(deliveryService.save(dto));
    }

    @Override
    public ResponseEntity<ServiceDeliveryDto> findById(Integer id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @Override
    public ResponseEntity<List<ServiceDeliveryDto>> findAll() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    @Override
    public ResponseEntity<ServiceDeliveryDto> updateEnabled(Integer id, boolean enabled) {
        return ResponseEntity.ok(deliveryService.updateEnabled(id, enabled));
    }

    @Override
    public ResponseEntity<List<ServiceDeliveryDto>> findAllEnabled(boolean enabled) {
        return ResponseEntity.ok(deliveryService.findAllEnabled(enabled));
    }

    @Override
    public ResponseEntity<PageResponse<ServiceDeliveryDto>> findAllServicesDelivery(int page, int size) {
        return ResponseEntity.ok(deliveryService.findAllServicesDelivery(page,
                size));
    }

    @Override
    public void delete(Integer id) {
        deliveryService.delete(id);
    }


}
