package com.hitecart.deliverysystem.mapper.mission;

import com.hitecart.deliverysystem.dto.client.ClientUpdateRequest;
import com.hitecart.deliverysystem.dto.mission.MissionRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.mapper.DeliveryDestinationMapper;
import com.hitecart.deliverysystem.mapper.MissionExpenseMapper;
import com.hitecart.deliverysystem.mapper.MissionVehicleMapper;
import com.hitecart.deliverysystem.mapper.StopPointMapper;
import com.hitecart.deliverysystem.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        DeliveryDestinationMapper.class,
        StopPointMapper.class
},unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MissionMapper {


    @Mapping(target = "deliveryDestination",source ="deliveryDestinationId")
    @Mapping(target = "stopPoints", ignore = true) // Handled in @AfterMapping
    Mission   toEntity(MissionRequest request);

    @Mapping(target = "deliveryDestination",source ="deliveryDestination")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    MissionResponse toResponse(Mission entity);

    default DeliveryDestination mapDeliveryDestination(Integer deliveryDestinationId) {
        if (deliveryDestinationId == null) return null;
        return DeliveryDestination.builder().id(deliveryDestinationId).build();
    }

    @AfterMapping
    default void linkChildrenToMission(@MappingTarget Mission mission, MissionRequest request,
                                       @Context StopPointMapper stopPointMapper) {
        // Link StopPoints
        if (request.stopPoints() != null) {
            mission.setStopPoints(request.stopPoints().stream()
                    .map(stopPointRequest -> {
                        StopPoint stopPoint = new StopPoint();//stopPointMapper.toEntity(stopPointRequest);
                        stopPoint.setTitle(stopPointRequest.arrivalLocation() + " - " + mission.getDeliveryDestination().getDeliveryService().getDestinationCity() );
                        stopPoint.setComment(stopPointRequest.arrivalLocation() + " - " + mission.getDeliveryDestination().getDeliveryService().getDestinationCity() +
                        " start on "+stopPointRequest.departureDate() + "will delivered at "+stopPointRequest.scheduledDeliveryTime() );
                        stopPoint.setDepartureDate(stopPointRequest.departureDate());
                        stopPoint.setArrivalDate(stopPointRequest.arrivalDate());
                        stopPoint.setScheduledDeliveryTime(stopPointRequest.scheduledDeliveryTime());
                        stopPoint.setActualDeliveryTime(stopPointRequest.actualDeliveryTime());
                        stopPoint.setDepartureLocation(stopPointRequest.departureLocation());
                        stopPoint.setArrivalLocation(stopPointRequest.arrivalLocation());
                        stopPoint.setMission(mission);
                        return stopPoint;
                    })
                    .toList());
        }
    }

    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMissionFromRequest(MissionRequest request, @MappingTarget Mission entity);


}
