package com.hitecart.deliverysystem.mapper;


import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationRequest;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationResponse;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.model.*;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {
                DeliveryServiceMapper.class,
                DeliveryPointMapper.class,
                //MissionMapper.class,
        }
        , unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface DeliveryDestinationMapper {

    @Mapping(target = "deliveryService", source = "deliveryServiceId")
    @Mapping(target = "deliveryPoints", source = "deliveryPoints")
    DeliveryDestination toEntity(DeliveryDestinationRequest request);

    @Mapping(target = "deliveryService", source = "deliveryService")
    @Mapping(target = "deliveryPointIds", source = "deliveryPoints")
    DeliveryDestinationResponse toResponse(DeliveryDestination entity);

    // Convert ID → DeliveryService (performance: avoids full entity fetch)
    default DeliveryService mapDeliveryService(Integer deliveryServiceId) {
        if (deliveryServiceId == null) return null;
        return DeliveryService.builder().id(deliveryServiceId).build();
    }


    default List<Integer> mapDeliveryPointIds(List<DeliveryPoint> deliveryPoints) {
        if (deliveryPoints == null) return null;
        return deliveryPoints.stream()
                .map(DeliveryPoint::getId)
                .collect(Collectors.toList());
    }


    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeliveryDestinationFromRequest(DeliveryDestinationRequest request, @MappingTarget DeliveryDestination entity);


    // Convert List<ID> → List<Mission>
/*    default List<Mission> mapMissions(List<Integer> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> Mission.builder().id(id).build())
                .collect(Collectors.toList());
    }*/
}
