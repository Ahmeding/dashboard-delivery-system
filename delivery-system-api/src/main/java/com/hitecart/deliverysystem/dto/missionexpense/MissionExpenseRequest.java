package com.hitecart.deliverysystem.dto.missionexpense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.ExpensesType;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public record MissionExpenseRequest(
        Integer id,

        @NotBlank(message = "Expense name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        @Schema(
                description = "Mission expenses name",
                example = "Fule Expenses",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        String name,

        @NotNull(message = "Expense type is required")
        @Schema(
                description = "Expenses Type",
                implementation = ExpensesType.class,
                example = "DIESEL",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        ExpensesType type,

        @NotNull(message = "Expense date is required")
        @PastOrPresent(message = "Expense date cannot be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00")
        LocalDateTime expenseDate,

        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
        @Schema(
                description = "Quantity",
                example = "58.12",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        BigDecimal quantity,

        @NotNull(message = "Cost is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Cost cannot be negative")
        @Schema(
                description = "Cost",
                example = "112.12",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        BigDecimal cost,

        @Size(max = 255, message = "Comment cannot exceed 255 characters")
        @Schema(
                description = "Comment",
                example = "Fuel Expenses",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        String comment,

        @NotNull(message = "Mission ID is required")
        @Min(value = 1, message = "Mission ID must be a positive number")
        @Schema(
                description = "requested mission",
                example = "46",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        Integer missionId
) {
        // Custom validation to ensure expense date makes sense
        public MissionExpenseRequest {
                // Validate expense cannot be created with future date
                if (expenseDate != null && expenseDate.isAfter(LocalDateTime.now())) {
                        throw new IllegalArgumentException("Expense date cannot be in the future");
                }

                // Validate that if both quantity and cost are present, they make sense together
                if (quantity != null && cost != null) {
                        // Check for unreasonably large values (example threshold - adjust as needed)
                        if (quantity.multiply(cost).compareTo(new BigDecimal("1000000")) > 0) {
                                throw new IllegalArgumentException("Total expense value seems unreasonably high");
                        }
                }
        }
}
