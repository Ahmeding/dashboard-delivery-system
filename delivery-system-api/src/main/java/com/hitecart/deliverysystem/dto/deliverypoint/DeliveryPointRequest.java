package com.hitecart.deliverysystem.dto.deliverypoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.deliverypointcontact.DeliveryPointContactRequest;
import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternRequest;
import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.model.enums.DeliveryPointType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

/**
 * Represents a request for creating or updating a delivery point.
 * <p>
 * Delivery points define specific locations where goods are delivered, including scheduling,
 * contact information, and operational parameters. All time values are in the system's default timezone.
 * </p>
 *
 * @param id Unique identifier for existing delivery points (null when creating new)
 * @param name Human-readable name for the delivery point
 * @param categoryId Associated category ID
 * @param type Classification of delivery location
 * @param clientId Owning client identifier
 * @param deliveryDestinationId Target destination identifier
 * @param recurrencePattern Recurrence rules for regular deliveries
 * @param schedule Daily delivery time window
 * @param address Physical location details
 * @param contact Primary contact information
 * @param enabled Operational status flag
 */
@Schema(
        name = "DeliveryPointRequest",
        description = "Request payload for delivery point management",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record DeliveryPointRequest(
        @Schema(
                description = "Unique ID for existing delivery points (omit for new entries)",
                example = "1024",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Integer id,

        @Schema(
                description = "Unique name for the delivery point",
                example = "Main Office Reception",
                minLength = 2,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
        String name,

        @Schema(
                description = "Category ID defining delivery point characteristics",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Category ID is required")
        @Positive(message = "Category ID must be positive")
        Integer categoryId,

        @Schema(
                description = "Type classification of delivery location",
                implementation = DeliveryPointType.class,
                example = "SHOP",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Type is required")
        DeliveryPointType type,

        @Schema(
                description = "Client ID owning the delivery point",
                example = "789",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Client ID is required")
        @Positive(message = "Client ID must be positive")
        Integer clientId,

        @Schema(
                description = "Destination ID for routing purposes",
                example = "56",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Destination ID is required")
        @Positive(message = "Destination ID must be positive")
        Integer deliveryDestinationId,

        @Schema(
                description = "Recurrence rules for regular deliveries",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Valid
        @NotNull(message = "Recurrence pattern is required")
        RecurrencePatternRequest recurrencePattern,

        @Schema(
                description = "Daily delivery time window in HH:mm format",
                type = "string",
                pattern = "HH:mm",
                example = "14:30",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Schedule is required")
        @JsonFormat(pattern = "HH:mm")
        @FutureOrPresent(message = "Schedule must be current or future time")
        LocalTime schedule,

        @Schema(
                description = "Physical address details",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Valid
        @NotNull(message = "Address is required")
        AddressRequest address,

        @Schema(
                description = "Contact information for delivery point",
                requiredMode = Schema.RequiredMode.REQUIRED
        )

        @Valid
        @NotNull(message = "Contact information is required")
        DeliveryPointContactRequest contact,

        @Schema(
                description = "Operational status (true = active, false = inactive)",
                example = "true",
                defaultValue = "true"
        )
        boolean enabled
) {}
