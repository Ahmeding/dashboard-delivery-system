package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.DeliveryPointCategoryDto;


import java.util.List;

public interface IDeliverPointCategoryService {

    DeliveryPointCategoryDto save(DeliveryPointCategoryDto dto);

    DeliveryPointCategoryDto findById(Integer id);

    DeliveryPointCategoryDto findByName(String name);

    List<DeliveryPointCategoryDto> findAll();

    void delete(Integer id);

    PageResponse<DeliveryPointCategoryDto> findAllDeliveryPointCategories(int page, int size);
}
