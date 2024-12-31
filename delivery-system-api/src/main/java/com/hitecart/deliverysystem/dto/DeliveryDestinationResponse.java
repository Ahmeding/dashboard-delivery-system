package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.DeliveryDestination;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
public class DeliveryDestinationResponse {

    private Integer id;
    private String reference;
    private Integer clientId;
    private ServiceDeliveryDto serviceDelivery;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static DeliveryDestinationResponse fromEntity(DeliveryDestination deliveryDestination) {
                if(deliveryDestination == null)
                {
                    return null;
                }
                return DeliveryDestinationResponse.builder()
                        .id(deliveryDestination.getId())
                        .reference(deliveryDestination.getReference())

                        .clientId(deliveryDestination.getClient()!=null ? deliveryDestination.getClient().getId() : null)
                        .serviceDelivery(deliveryDestination.getServiceDelivery()!=null ?
                               ServiceDeliveryDto.fromEntity(deliveryDestination.getServiceDelivery()) : null)
                        .createdAt(deliveryDestination.getCreatedAt())
                        .updatedAt(deliveryDestination.getUpdatedAt())
                        .createdBy(deliveryDestination.getCreatedBy())
                        .lastModifiedBy(deliveryDestination.getLastModifiedBy())
                        .build();
    }

    public static DeliveryDestination toEntity(DeliveryDestinationResponse dto) {
        if(dto == null)
        {
            return null;
        }
        return DeliveryDestination.builder()
                .id(dto.getId())
                .reference(dto.getReference())
                .serviceDelivery(ServiceDeliveryDto.toEntity(dto.getServiceDelivery()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .lastModifiedBy(dto.getLastModifiedBy())
                .build();
    }
}
