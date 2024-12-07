package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class DeliveryPointCategoryDto {

    private int id;
    private String reference;
    private String name;
    private String description;
    private Double additionalFees;
    private List<DeliveryPointDto> deliveryPointsDto;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static DeliveryPointCategoryDto fromEntity(DeliveryPointCategory entity) {
        if (entity == null) {
            return null;
        }

        return DeliveryPointCategoryDto.builder()
                .id(entity.getId())
                .reference(entity.getReference())
                .name(entity.getName())
                .description(entity.getDescription())
                .additionalFees(entity.getAdditionalFees())
                .deliveryPointsDto(entity.getDeliveryPoints()!=null?
                        entity.getDeliveryPoints().stream()
                                .map(DeliveryPointDto::fromEntity)
                                .collect(Collectors.toList()):null)

                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .build();
    }
    public static DeliveryPointCategory toEntity(DeliveryPointCategoryDto dto) {
        return DeliveryPointCategory.builder()
                .id(dto.getId())
                .reference(dto.getReference())
                .name(dto.getName())
                .description(dto.getDescription())
                .additionalFees(dto.getAdditionalFees())
                .deliveryPoints(dto.getDeliveryPointsDto()!=null?
                        dto.getDeliveryPointsDto().stream()
                                .map(DeliveryPointDto::toEntity)
                                .collect(Collectors.toList()):null)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .lastModifiedBy(dto.getLastModifiedBy())
                .build();
    }

}
