package com.hitecart.deliverysystem.dto.deliverypointcategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Represents a request for creating or updating a delivery point category.
 * <p>
 * Delivery point categories define types of delivery locations (e.g., 'Apartment', 'Office',
 * 'Locker') and may include additional fees for special handling requirements.
 * </p>
 *
 * @param id Unique identifier for existing categories (null when creating new)
 * @param name Category name (e.g., 'Ground Floor', 'Secure Access')
 * @param description Optional description of category characteristics
 * @param additionalFees Extra charges applied for this category (non-negative)
 */
@Schema(
        name = "DeliveryPointCategoryRequest",
        description = "Request payload for delivery point category operations",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record DeliveryPointCategoryRequest(
        @Schema(
                description = "Unique ID for existing categories (omit for new entries)",
                example = "5",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Integer id,

        @Schema(
                description = "Unique category name",
                example = "High-Rise",
                minLength = 2,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
        String name,

        @Schema(
                description = "Category description",
                example = "Buildings with elevator access above 5 floors",
                maxLength = 255,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        @Schema(
                description = "Additional fees for this category in EUR",
                example = "2.50",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Additional fees are required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Fees cannot be negative")
        @Digits(integer = 5, fraction = 2, message = "Fees must have max 5 whole digits and 2 decimals")
        Double additionalFees
) {}
