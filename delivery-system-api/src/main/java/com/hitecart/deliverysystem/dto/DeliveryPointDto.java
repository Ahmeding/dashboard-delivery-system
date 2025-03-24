package com.hitecart.deliverysystem.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.enums.DeliveryDay;
import com.hitecart.deliverysystem.model.enums.DeliveryPointType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class DeliveryPointDto {

    private Integer id;
    private String name;

    private AdresseDto addressDto;
    private DeliveryPointType deliveryPointType;

    private Integer delivDestinationId;
    private Integer clientId;
    private Integer deliveryPointCategoryId;
    private DeliveryPointContactDto contact;

    private RecurrencePatternDto recurrencePatternDto;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime schedule;

    private boolean enabled;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static DeliveryPointDto fromEntity(DeliveryPoint deliveryPoint) {
                if(deliveryPoint == null)
                {
                    return null;
                }

                return DeliveryPointDto.builder()
                        .id(deliveryPoint.getId())
                        .name(deliveryPoint.getName())
                        .deliveryPointType(deliveryPoint.getDeliveryPointType())
                        .addressDto(AdresseDto.fromEntity(deliveryPoint.getAddress()) )
                        .schedule(deliveryPoint.getSchedule())
                        .contact(DeliveryPointContactDto.fromEntity(deliveryPoint.getContact()))
                        .delivDestinationId(deliveryPoint.getDeliveryDestination()!=null ?
                                deliveryPoint.getDeliveryDestination().getId()
                                : null)
                        .clientId(deliveryPoint.getClient()!=null ?
                                deliveryPoint.getClient().getId()
                                : null)
                        .deliveryPointCategoryId(deliveryPoint.getDeliveryPointCategory()!= null ? deliveryPoint.getDeliveryPointCategory().getId():null)
                        .enabled(deliveryPoint.isEnabled())
                        .createdAt(deliveryPoint.getCreatedAt())
                        .updatedAt(deliveryPoint.getUpdatedAt())
                        .createdBy(deliveryPoint.getCreatedBy())
                        .lastModifiedBy(deliveryPoint.getLastModifiedBy())
                        .recurrencePatternDto(deliveryPoint.
                                getRecurrencePattern() !=null ?
                                RecurrencePatternDto.fromEntity(deliveryPoint.getRecurrencePattern()):
                                null)

                        .build();
    }

    public static DeliveryPoint toEntity(DeliveryPointDto dto) {
        if(dto == null)
        {
            return null;
        }
        return DeliveryPoint.builder()
                .id(dto.getId())
                .name(dto.getName())
                .deliveryPointType(dto.getDeliveryPointType())
                .address(AdresseDto.toEntity(dto.getAddressDto()) )
                .schedule(dto.getSchedule())
                .contact(DeliveryPointContactDto.toEntity(dto.getContact()))
                .enabled(dto.isEnabled())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .lastModifiedBy(dto.getLastModifiedBy())

                .recurrencePattern(
                        dto.getRecurrencePatternDto() != null ?
                                RecurrencePatternDto.toEntity(dto.getRecurrencePatternDto()) :
                                null
                )
                .build();
    }
}
