package com.hitecart.deliverysystem.dto.basic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class DeliveryDestinationDto {

    private Integer id;
    private String reference;

    private List<MissionDto> missionsDto;
    private List<DeliveryPointDto> deliveryPointsDto;
    private Integer serviceDeliveryId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
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
                        .serviceDeliveryId(deliveryDestination.getServiceDelivery()!=null ? deliveryDestination.getServiceDelivery().getId() : null)
                        .missionsDto(deliveryDestination.getMissions()!=null ?
                                deliveryDestination.getMissions().stream()
                                        .map(MissionDto::fromEntity).collect(Collectors.toList()):null)
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
                .missions(dto.missionsDto!=null?
                        dto.missionsDto.stream()
                            .map(MissionDto::toEntity)
                            .collect(Collectors.toList()) : null)
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
