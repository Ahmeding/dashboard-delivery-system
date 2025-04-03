package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DeliveryPointCategoryApi;
import com.hitecart.deliverysystem.dto.basic.DeliveryPointCategoryDto;
import com.hitecart.deliverysystem.services.IDeliverPointCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeliveryPointCategoryController implements DeliveryPointCategoryApi {


    private final IDeliverPointCategoryService deliveryPointCategoryService;
    @Override
    public ResponseEntity<DeliveryPointCategoryDto> save(DeliveryPointCategoryDto dto) {
        return ResponseEntity.ok(this.deliveryPointCategoryService.save(dto));
    }

    @Override
    public ResponseEntity<DeliveryPointCategoryDto> findById(Integer id) {
        return ResponseEntity.ok(this.deliveryPointCategoryService.findById(id));
    }

    @Override
    public ResponseEntity<DeliveryPointCategoryDto> findByName(String name) {
        return ResponseEntity.ok(this.deliveryPointCategoryService.findByName(name));
    }

    @Override
    public ResponseEntity<List<DeliveryPointCategoryDto>> findAll() {
        return ResponseEntity.ok(this.deliveryPointCategoryService.findAll());
    }

    @Override
    public void delete(Integer id) {
        this.deliveryPointCategoryService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<DeliveryPointCategoryDto>> findAllDeliveryPointCategories(int page, int size) {
        return ResponseEntity.ok(this.deliveryPointCategoryService.findAllDeliveryPointCategories(page, size));
    }
}
