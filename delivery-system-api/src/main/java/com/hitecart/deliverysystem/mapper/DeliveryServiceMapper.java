package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceRequest;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceResponse;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.model.DeliveryDestination;
import com.hitecart.deliverysystem.model.DeliveryService;
import com.hitecart.deliverysystem.model.Driver;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryServiceMapper {


    DeliveryService toEntity(DeliveryServiceRequest request);

    @Mapping(target = "deliveryDestinationIds",source="deliveryDestinations")
    DeliveryServiceResponse toResponse(DeliveryService entity);


    default List<Integer> mapDeliveryDestinationId(List<DeliveryDestination> deliveryDestinations) {
        if(deliveryDestinations == null || deliveryDestinations.isEmpty()) return null;
        return deliveryDestinations.stream()
                .map(DeliveryDestination::getId)
                .collect(Collectors.toList());
    }
    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeliveryServiceFromRequest(DeliveryServiceRequest request, @MappingTarget DeliveryService entity);
}
