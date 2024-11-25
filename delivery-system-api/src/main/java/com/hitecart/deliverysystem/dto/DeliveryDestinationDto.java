package com.hitecart.deliverysystem.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.DeliveryDestination;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.ServiceDelivery;
import com.hitecart.deliverysystem.model.enums.DeliveryPointType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
public class DeliveryDestinationDto {

    private Integer id;
    private String reference;

    private List<DeliveryPointDto> deliveryPointsDto;
    private Integer clientId;
    private Integer serviceDeliveryId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static DeliveryDestinationDto fromEntity(DeliveryDestination deliveryDestination) {
                if(deliveryDestination == null)
                {
                    return null;
                }
                return DeliveryDestinationDto.builder()
                        .id(deliveryDestination.getId())
                        .reference(deliveryDestination.getReference())

                        .clientId(deliveryDestination.getClient()!=null ? deliveryDestination.getClient().getId() : null)
                        .serviceDeliveryId(deliveryDestination.getServiceDelivery()!=null ? deliveryDestination.getServiceDelivery().getId() : null)
                        .deliveryPointsDto(
                                deliveryDestination.getDeliveryPoints()!=null?
                                        deliveryDestination.getDeliveryPoints().stream()
                                                .map(DeliveryPointDto::fromEntity)
                                                .collect(Collectors.toList()) : null)

                        .createdAt(deliveryDestination.getCreatedAt())
                        .updatedAt(deliveryDestination.getUpdatedAt())
                        .createdBy(deliveryDestination.getCreatedBy())
                        .lastModifiedBy(deliveryDestination.getLastModifiedBy())
                        .build();
    }

    public static DeliveryDestination toEntity(DeliveryDestinationDto dto) {
        if(dto == null)
        {
            return null;
        }
        return DeliveryDestination.builder()
                .id(dto.getId())
                .reference(dto.getReference())
                .deliveryPoints(
                        dto.getDeliveryPointsDto()!=null?
                                dto.getDeliveryPointsDto().stream()
                                        .map(DeliveryPointDto::toEntity)
                                        .collect(Collectors.toList()) : null)

                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .lastModifiedBy(dto.getLastModifiedBy())
                .build();
    }
}
