package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.ServiceDelivery;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ServiceDeliveryDto {

    private Integer id;
    private String reference;
    private String color;
    private String destinationCity;
    private String destinationCountry;
    private String pickUpLocation;
    private double distance;
    private String title;
    private String description;
    private BigDecimal basicTripCost;
    private boolean enabled;
    private Integer createdBy;
    private CreatedByUserDto createdByUser;

    public static ServiceDeliveryDto fromEntity(ServiceDelivery serviceDelivery) {
        if(serviceDelivery == null){
            return null;
        }
        return ServiceDeliveryDto.builder()
                .id(serviceDelivery.getId())
                .reference(serviceDelivery.getReference())
                .color(serviceDelivery.getColor())
                .destinationCity(serviceDelivery.getDestinationCity())
                .destinationCountry(serviceDelivery.getDestinationCountry())
                .pickUpLocation(serviceDelivery.getPickUpLocation())
                .distance(serviceDelivery.getDistance())
                .title(serviceDelivery.getTitle())
                .description(serviceDelivery.getDescription())
                .basicTripCost(serviceDelivery.getBasicTripCost())
                .enabled(serviceDelivery.isEnabled())
                .createdBy(serviceDelivery.getCreatedBy())
                .build();
    }

    public static ServiceDelivery toEntity(ServiceDeliveryDto serviceDeliveryDto) {
        if(serviceDeliveryDto == null){return null;}
        return ServiceDelivery.builder()
                .id(serviceDeliveryDto.getId())
                .reference(serviceDeliveryDto.getReference())
                .color(serviceDeliveryDto.getColor())
                .destinationCity(serviceDeliveryDto.getDestinationCity())
                .destinationCountry(serviceDeliveryDto.getDestinationCountry())
                .pickUpLocation(serviceDeliveryDto.getPickUpLocation())
                .distance(serviceDeliveryDto.getDistance())
                .title(serviceDeliveryDto.getTitle())
                .description(serviceDeliveryDto.getDescription())
                .basicTripCost(serviceDeliveryDto.getBasicTripCost())
                .enabled(serviceDeliveryDto.isEnabled())
                .build();
    }
}
