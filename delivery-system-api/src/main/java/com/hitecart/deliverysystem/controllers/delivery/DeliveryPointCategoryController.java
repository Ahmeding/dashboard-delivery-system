package com.hitecart.deliverysystem.controllers.delivery;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DeliveryPointCategoryApi;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCategoryRequest;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCatResponse;
import com.hitecart.deliverysystem.services.DeliveryPointCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DeliveryPointCategoryController implements DeliveryPointCategoryApi {


    private final DeliveryPointCategoryService deliveryPointCategoryService;
    @Override
    public ResponseEntity<DeliveryPointCatResponse> saveDeliveryPointCategory(DeliveryPointCategoryRequest deliveryPointCategoryRequest) {
        return ResponseEntity.ok(this.deliveryPointCategoryService.save(deliveryPointCategoryRequest));
    }

    @Override
    public ResponseEntity<DeliveryPointCatResponse> updateDeliveryPointCategoryInfo(Integer deliveryPointCatId,DeliveryPointCategoryRequest deliveryPointCategoryRequest) {
        return ResponseEntity.ok(deliveryPointCategoryService.updateDeliveryPointCategoryInfo(deliveryPointCatId,deliveryPointCategoryRequest));
    }

    @Override
    public ResponseEntity<DeliveryPointCatResponse> findDeliveryPointCategoryById(Integer id) {
        return ResponseEntity.ok(this.deliveryPointCategoryService.findById(id));
    }

    @Override
    public ResponseEntity<DeliveryPointCatResponse> findDeliveryPointCategoryByName(String name) {
        return ResponseEntity.ok(this.deliveryPointCategoryService.findByName(name));
    }

    @Override
    public ResponseEntity<List<DeliveryPointCatResponse>> findAllDeliveryPointCategory() {
        return ResponseEntity.ok(this.deliveryPointCategoryService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteDeliveryPointCategory(Integer id) {
        try {
            deliveryPointCategoryService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "DeliveryPointCategory deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "DeliveryPointCategory not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<DeliveryPointCatResponse>> findAllDeliveryPointCategoriesPageable(int page, int size) {
        return ResponseEntity.ok(this.deliveryPointCategoryService.findAllDeliveryPointCategories(page, size));
    }
}
