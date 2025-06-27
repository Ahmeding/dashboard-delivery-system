package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.order.OrderRequest;
import com.hitecart.deliverysystem.dto.order.OrderResponse;
import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternRequest;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.Order;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = {
        //OrderLineMapper.class,
        ClientMapper.class,
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderMapper {

   @Mapping(target = "orderLines", source = "orderLines")
   @Mapping(target = "client", source = "clientId")
   Order toEntity(OrderRequest request);

   @Mapping(target = "orderLines", source = "orderLines")
   @Mapping(target = "clientId", source = "client.id")
   @Mapping(target = "createdAt", source = "createdAt")
   @Mapping(target = "updatedAt", source = "updatedAt")
   @Mapping(target = "createdBy", source = "createdBy")
   @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
   OrderResponse toResponse(Order entity);

   default Client mapOrder(Integer clientId) {
      if (clientId == null) return null;
      return Client.builder().id(clientId).build();
   }

   @AfterMapping
   default void linkOrderLinesToOrder(@MappingTarget Order order) {
      if (order.getOrderLines() != null) {
         order.getOrderLines().forEach(orderLine -> orderLine.setOrder(order));
      }
   }



   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   void updateOrderFromRequest(OrderRequest request, @MappingTarget Order order);

}
