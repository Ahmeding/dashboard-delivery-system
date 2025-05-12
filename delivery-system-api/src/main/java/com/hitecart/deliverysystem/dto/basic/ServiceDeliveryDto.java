package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.ServiceDelivery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
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
