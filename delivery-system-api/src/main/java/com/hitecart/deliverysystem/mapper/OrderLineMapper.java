package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.orderline.OrderLineRequest;
import com.hitecart.deliverysystem.dto.orderline.OrderLineResponse;
import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternRequest;
import com.hitecart.deliverysystem.model.DeliveryService;
import com.hitecart.deliverysystem.model.Order;
import com.hitecart.deliverysystem.model.OrderLine;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        DeliveryServiceMapper.class,
        OrderMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderLineMapper {

    @Mapping(target = "deliveryService", source = "deliveryServiceId")
    @Mapping(target = "order", source = "orderId")
    OrderLine toEntity(OrderLineRequest request);

    @Mapping(target = "deliveryService", source = "deliveryService")
    @Mapping(target = "order", source = "order")
    OrderLineResponse toResponse(OrderLine entity);


    // Convert IDs to entities
    default Order mapOrder(Integer orderId){
        if(orderId == null) return null;
        return Order.builder().id(orderId).build();
    }

    default DeliveryService mapDeliveryService(Integer dsId){
        if(dsId == null) return null;
        return DeliveryService.builder().id(dsId).build();
    }



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrderLineFromRequest(OrderLineRequest request, @MappingTarget OrderLine orderLine);

}
