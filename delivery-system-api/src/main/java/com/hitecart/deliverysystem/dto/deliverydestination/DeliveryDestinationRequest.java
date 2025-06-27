package com.hitecart.deliverysystem.dto.deliverydestination;

import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointRequest;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
/**
 * Represents a request for creating or updating a delivery destination.
 * <p>
 * A delivery destination groups multiple delivery points under a single service,
 * typically representing a logical or geographical delivery cluster (e.g.,
 * "Downtown Area" or "Tech Park Complex").
 * </p>
 *
 * @param id Unique identifier for existing destinations (null when creating new)
 * @param deliveryPoints Collection of delivery points belonging to this destination
 * @param deliveryServiceId Associated delivery service provider
 */
@Schema(
        name = "DeliveryDestinationRequest",
        description = "Request payload for delivery destination configuration",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record DeliveryDestinationRequest(
        @Schema(
                description = "Unique ID for existing destinations (omit for new entries)",
                example = "78",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Integer id,

        @ArraySchema(
                minItems = 1,
                arraySchema = @Schema(
                        description = "Delivery points included in this destination",
                        requiredMode = Schema.RequiredMode.REQUIRED
                ),
                schema = @Schema(implementation = DeliveryPointRequest.class)
        )
        @Valid
        @NotEmpty(message = "At least one delivery point is required")
        @Size(min = 1, max = 50, message = "Must contain 1-50 delivery points")
        List<DeliveryPointRequest> deliveryPoints,

        @Schema(
                description = "Service provider responsible for this destination",
                example = "15",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Delivery service ID is required")
        @Positive(message = "Service ID must be positive")
        Integer deliveryServiceId
) {}
