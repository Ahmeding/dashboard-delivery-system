package com.hitecart.deliverysystem.dto.deliveryservice;
import com.hitecart.deliverysystem.model.enums.RouteDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Represents a request for creating or updating a delivery service.
 * <p>
 * Contains configuration details for delivery routes including geographical information,
 * pricing, and operational parameters. Enforces business rules for route difficulty
 * and distance combinations.
 * </p>
 *
 * @param id Unique service identifier (null for new services)
 * @param color Hex color code for route visualization
 * @param pickUpLocation Starting point for deliveries
 * @param destinationCity Target city for deliveries
 * @param destinationCountry Target country for deliveries
 * @param distance Route distance in kilometers
 * @param title Service name or title
 * @param description Service details
 * @param basicTripCost Base price for the service
 * @param enabled Operational status flag
 * @param difficulty Route complexity classification
 */
@Schema(
        name = "DeliveryServiceRequest",
        description = "Request payload for delivery service configuration",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record DeliveryServiceRequest(
        @Schema(
                description = "Unique ID for existing services (omit for new entries)",
                example = "25",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Integer id,

        @Schema(
                description = "Hex color code for route visualization",
                example = "#4A90E2",
                pattern = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Color is required")
        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
                message = "Invalid color format. Use hex format (#RGB or #RRGGBB)")
        String color,

        @Schema(
                description = "Pick-up location address",
                example = "123 Warehouse Rd, Lyon",
                maxLength = 200,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Pick-up location is required")
        @Size(max = 200, message = "Pick-up location must be ≤200 characters")
        String pickUpLocation,

        @Schema(
                description = "Destination city name",
                example = "Paris",
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Destination city is required")
        @Size(max = 100, message = "Destination city must be ≤100 characters")
        String destinationCity,

        @Schema(
                description = "Destination country name",
                example = "France",
                maxLength = 60,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Destination country is required")
        @Size(max = 60, message = "Destination country must be ≤60 characters")
        String destinationCountry,

        @Schema(
                description = "Route distance in kilometers",
                example = "450.75",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Distance is required")
        @Positive(message = "Distance must be a positive number")
        @Digits(integer = 4, fraction = 2, message = "Distance must have max 4 whole digits and 2 decimals")
        BigDecimal distance,

        @Schema(
                description = "Service name or title",
                example = "Lyon-Paris Express",
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be ≤100 characters")
        String title,

        @Schema(
                description = "Service description",
                example = "Overnight delivery service between Lyon and Paris",
                maxLength = 500,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        @Size(max = 500, message = "Description must be ≤500 characters")
        String description,

        @Schema(
                description = "Base price in EUR",
                example = "299.99",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Basic trip cost is required")
        @Positive(message = "Basic trip cost must be positive")
        @Digits(integer = 6, fraction = 2, message = "Price must have max 6 whole digits and 2 decimals")
        BigDecimal basicTripCost,

        @Schema(
                description = "Operational status (true = active)",
                example = "true",
                defaultValue = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Enabled status is required")
        Boolean enabled,

        @Schema(
                description = "Route complexity classification",
                implementation = RouteDifficulty.class,
                example = "MEDIUM",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Route difficulty is required")
        RouteDifficulty difficulty

) {
    /**
     * Validates that route difficulty matches distance constraints.
     * <p>
     * Business Rules:
     * - SIMPLE: Distance ≤ 700 km
     * - MEDIUM: Distance ≤ 1200 km
     * - HARD: No distance limit
     * </p>
     *
     * @return true if difficulty/distance combination is valid
     */
    @AssertTrue(message = "Difficulty must match distance requirements")
    private boolean isValidDifficultyDistanceCombination() {
        if (distance == null || difficulty == null) return true;
        return switch (difficulty) {
            case SIMPLE -> distance.compareTo(BigDecimal.valueOf(700)) <= 0;
            case MEDIUM -> distance.compareTo(BigDecimal.valueOf(1200)) <= 0;
            case HARD -> true;
        };
    }
}
