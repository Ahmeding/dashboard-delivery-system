package com.hitecart.deliverysystem.dto.deliveryservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationResponse;
import com.hitecart.deliverysystem.model.enums.RouteDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryServiceResponse {
    @Schema(description = "Unique identifier of the delivery service", example = "789")
    private Integer id;
    @Schema(description = "Unique business reference code", example = "DSVC-2023-Q3")
    private String reference;
    @Schema(description = "Color code in HEX format", example = "#FF5733")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    private String color;
    @Schema(description = "Pick-up location address", example = "123 Main St, Paris")
    private String pickUpLocation;
    @Schema(description = "Destination city", example = "Lyon")
    private String destinationCity;
    @Schema(description = "Destination country", example = "France")
    private String destinationCountry;
    @Schema(description = "Route distance in kilometers", example = "150.5")
    @Positive
    private double distance;
    @Schema(description = "Service title", example = "Premium Paris-Lyon Delivery")
    private String title;
    @Schema(description = "Service description", example = "Express delivery with temperature control")
    private String description;
    @Schema(description = "Base cost for the service", example = "299.99")
    @Positive
    private BigDecimal basicTripCost;
    @Schema(description = "Service activation status")
    private boolean enabled;

    @Schema(description = "Route difficulty level", allowableValues = {"SIMPLE", "MEDIUM", "HARD"})
    private RouteDifficulty difficulty; // SIMPLE; MEDIUM; HARD


    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Creation timestamp",type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;


    @Schema(description = "Associated delivery destinations")
    private List<Integer> deliveryDestinationIds;
}
