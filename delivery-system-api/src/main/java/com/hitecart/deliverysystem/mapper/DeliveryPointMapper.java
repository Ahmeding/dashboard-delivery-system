package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointRequest;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointResponse;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
uses = {
        AddressMapper.class,
        DeliveryPointContactMapper.class,
        RecurrencePatternMapper.class,
        //DeliveryDestinationMapper.class,
        ClientMapper.class,
        DeliveryPointCategoryMapper.class
})
public interface DeliveryPointMapper {

    @Mapping(target = "deliveryDestination", source = "deliveryDestinationId")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "recurrencePattern", source = "recurrencePattern")
    @Mapping(target = "deliveryPointCategory", source = "categoryId")
    @Mapping(target = "client", source = "clientId")
    @Mapping(target = "deliveryPointType", source = "type")
    @Mapping(target = "enabled", source = "enabled")
    DeliveryPoint toEntity(DeliveryPointRequest request);

    @Mapping(target = "deliveryDestinationId", source = "deliveryDestination.id")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "recurrencePattern", source = "recurrencePattern")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "category", source = "deliveryPointCategory")
    @Mapping(target = "type", source = "deliveryPointType")
    DeliveryPointResponse toResponse(DeliveryPoint entity);

    default DeliveryDestination mapDeliveryDestination(Integer id) {
        if (id == null) return null;
        DeliveryDestination destination = new DeliveryDestination();
        destination.setId(id);
        return destination;
    }

    default DeliveryPointCategory mapDeliveryPointCategory(Integer id) {
        if (id == null) return null;
        DeliveryPointCategory category = new DeliveryPointCategory();
        category.setId(id);
        return category;
    }

    default ClientResponse mapClientResponse(Integer id){
        if (id == null) return null;
        ClientResponse clientResp = new ClientResponse();
        clientResp.setId(id);
        return clientResp;
    }

    default Client mapClient(Integer id) {
        if (id == null) return null;
        Client client = new Client();
        client.setId(id);
        return client;
    }

    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeliveryPointFromRequest(DeliveryPointRequest request, @MappingTarget DeliveryPoint entity);
}
