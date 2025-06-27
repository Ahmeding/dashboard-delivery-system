package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCategoryRequest;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCatResponse;


import java.util.List;

public interface DeliveryPointCategoryService {

    DeliveryPointCatResponse save(DeliveryPointCategoryRequest dto);
    DeliveryPointCatResponse updateDeliveryPointCategoryInfo(Integer deliveryPointCatId,DeliveryPointCategoryRequest dto);
    DeliveryPointCatResponse findById(Integer id);

    DeliveryPointCatResponse findByName(String name);

    List<DeliveryPointCatResponse> findAll();

    void delete(Integer id);

    PageResponse<DeliveryPointCatResponse> findAllDeliveryPointCategories(int page, int size);
}
