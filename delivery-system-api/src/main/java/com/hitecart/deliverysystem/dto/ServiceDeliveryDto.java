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
    private String title;
    private String description;
    private BigDecimal unitPriceHT;
    private BigDecimal priceTTC;
    private BigDecimal tva;
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
                .title(serviceDelivery.getTitle())
                .description(serviceDelivery.getDescription())
                .unitPriceHT(serviceDelivery.getUnitPriceHT())
                .tva(serviceDelivery.getTva())
                .priceTTC(serviceDelivery.getPriceTTC())
                .enabled(serviceDelivery.isEnabled())
                .createdBy(serviceDelivery.getCreatedBy())
                .build();
    }

    public static ServiceDelivery toEntity(ServiceDeliveryDto serviceDeliveryDto) {
        if(serviceDeliveryDto == null){return null;}
        return ServiceDelivery.builder()
                .id(serviceDeliveryDto.getId())
                .reference(serviceDeliveryDto.getReference())
                .title(serviceDeliveryDto.getTitle())
                .description(serviceDeliveryDto.getDescription())
                .unitPriceHT(serviceDeliveryDto.getUnitPriceHT())
                .tva(serviceDeliveryDto.getTva())
                .priceTTC(serviceDeliveryDto.getPriceTTC())
                .enabled(serviceDeliveryDto.isEnabled())
                .build();
    }
}
