package com.hitecart.deliverysystem.dto.deliverypoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationResponse;
import com.hitecart.deliverysystem.dto.deliverypointcontact.DeliveryPointContactResponse;
import com.hitecart.deliverysystem.dto.address.AddressResponse;
import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCatResponse;
import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternResponse;
import com.hitecart.deliverysystem.model.enums.DeliveryPointType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryPointResponse {

    private Integer id;
    private String reference;
    private String name;
    private DeliveryPointType type;
    private boolean enabled;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime schedule;

    private AddressResponse address;
    private DeliveryPointContactResponse contact;
    private RecurrencePatternResponse recurrencePattern;
    private Integer clientId;
    private DeliveryPointCatResponse category;
    private Integer deliveryDestinationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
}
