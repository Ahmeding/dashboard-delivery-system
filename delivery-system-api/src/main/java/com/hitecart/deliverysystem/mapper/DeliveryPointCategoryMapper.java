package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCategoryRequest;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCatResponse;
import com.hitecart.deliverysystem.model.AbstractEntity;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {
                DeliveryPointMapper.class
        })
public interface DeliveryPointCategoryMapper {

    DeliveryPointCategory toEntity(DeliveryPointCategoryRequest request);

    @Mapping(target = "deliveryPoints", source = "deliveryPointIds")
    DeliveryPointCategory toEntity(DeliveryPointCatResponse response);

    @Mapping(target = "deliveryPointIds", source = "deliveryPoints")
    DeliveryPointCatResponse toResponse(DeliveryPointCategory entity);

    default List<Integer> mapDeliveryPointIds(List<DeliveryPoint> deliveryPoints) {
        if (deliveryPoints == null || deliveryPoints.isEmpty()) return null;
        return deliveryPoints.stream()
                .map(DeliveryPoint::getId)
                .collect(Collectors.toList());
    }

    default List<DeliveryPoint> mapDeliveryPoints(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return ids.stream()
                .map(id->DeliveryPoint.builder().id(id).build())
                .collect(Collectors.toList());
    }

    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeliveryPointCategoryFromRequest(DeliveryPointCategoryRequest request, @MappingTarget DeliveryPointCategory entity);
}
