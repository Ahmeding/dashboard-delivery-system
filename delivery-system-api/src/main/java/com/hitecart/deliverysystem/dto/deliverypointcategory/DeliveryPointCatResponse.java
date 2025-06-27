package com.hitecart.deliverysystem.dto.deliverypointcategory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointResponse;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryPointCatResponse {
    private Integer id;
    private String reference;
    private String name;
    private String description;
    private Double additionalFees;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;

    private List<Integer> deliveryPointIds;

}
