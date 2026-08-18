package com.hitecart.deliverysystem.dto.claim;

import com.hitecart.deliverysystem.model.enums.ClaimStatus;
import com.hitecart.deliverysystem.model.enums.ClaimType;
import com.hitecart.deliverysystem.model.enums.Priority;
import com.hitecart.deliverysystem.model.enums.Severity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
/**
 * Represents a request for creating or updating a claim in the delivery system.
 * <p>
 * Claims can be submitted by users, drivers, or administrators to report issues
 * related to drivers, vehicles, missions, deliveries, or other operational concerns.
 * Each claim is categorized by type, priority, and severity for efficient processing.
 * </p>
 *
 * @param id Unique identifier for existing claims (null when creating new)
 * @param title Brief summary of the claim issue
 * @param description Detailed explanation of the problem
 * @param claimType Category of the claim (driver, vehicle, mission, etc.)
 * @param status Current processing status of the claim
 * @param priority Urgency level for resolution
 * @param severity Impact level of the issue
 * @param reportedDate When the claim was initially submitted
 * @param resolvedDate When the claim was resolved (null if pending)
 * @param reportedByUserId ID of the user who submitted the claim
 * @param subjectEntityId ID of the entity this claim concerns (driver, vehicle, mission)
 * @param relatedEntityType Type of entity this claim concerns
 */
@Schema(
        name = "ClaimRequest",
        description = "Request payload for claim management operations",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record ClaimRequest(
        @Schema(
                description = "Unique ID for existing claims (omit for new entries)",
                example = "1001",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Long id,

        @Schema(
                description = "Brief summary of the claim issue",
                example = "Driver arrived 2 hours late for delivery",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 5,
                maxLength = 100
        )
        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 100, message = "Title must be 5-100 characters")
        String title,

        @Schema(
                description = "Detailed explanation of the problem",
                example = "The scheduled delivery was for 2:00 PM but the driver did not arrive until 4:15 PM, causing customer dissatisfaction and potential business loss.",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 10,
                maxLength = 1000
        )
        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 1000, message = "Description must be 10-1000 characters")
        String description,

        @Schema(
                description = "Category of the claim",
                example = "DRIVER_ISSUE",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"DRIVER_ISSUE", "VEHICLE_PROBLEM", "MISSION_COMPLAINT", "DELIVERY_ISSUE", "PAYMENT_PROBLEM", "OTHER"}
        )
        @NotNull(message = "Claim type is required")
        ClaimType claimType,

        @Schema(
                description = "Current processing status",
                example = "OPEN",
                defaultValue = "OPEN",
                allowableValues = {"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED"}
        )
        ClaimStatus status,

        @Schema(
                description = "Urgency level for resolution",
                example = "HIGH",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"LOW", "MEDIUM", "HIGH", "URGENT"}
        )
        @NotNull(message = "Priority is required")
        Priority priority,

        @Schema(
                description = "Impact level of the issue",
                example = "MODERATE",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"MINOR", "MODERATE", "MAJOR", "CRITICAL"}
        )
        @NotNull(message = "Severity is required")
        Severity severity,

        @Schema(
                description = "When the claim was initially submitted (auto-set if null)",
                example = "2024-01-15T14:30:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime reportedDate,

        @Schema(
                description = "When the claim was resolved (null if pending)",
                example = "2024-01-16T10:15:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime resolvedDate,

        @Schema(
                description = "ID of the user who submitted the claim",
                example = "45",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Reported by user ID is required")
        @PositiveOrZero(message = "User ID must be positive or zero")
        Integer reportedByUserId,

        @Schema(
                description = "ID of the entity this claim concerns (driver, vehicle, mission, etc.)",
                example = "123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Subject entity ID is required")
        @PositiveOrZero(message = "Subject entity ID must be positive or zero")
        Integer subjectEntityId,

        @Schema(
                description = "Type of entity this claim concerns",
                example = "DRIVER",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"DRIVER", "VEHICLE", "MISSION", "DELIVERY", "OTHER"}
        )
        @NotBlank(message = "Related entity type is required")
        @Size(min = 2, max = 20, message = "Related entity type must be 2-20 characters")
        String relatedEntityType
) {
    /**
     * Validation group for claim creation
     */
    public interface CreationValidation {}

    /**
     * Validation group for claim update
     */
    public interface UpdateValidation {}
}
