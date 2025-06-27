package com.hitecart.deliverysystem.dto.repair;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import com.hitecart.deliverysystem.model.enums.TypeReparation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RepairRequest(
        Integer id,

        @NotNull(message = "Repair type is required")
        TypeReparation type,
        @Size(max = 255, message = "Comment cannot exceed 255 characters")
        String comment,

        @NotNull(message = "Start date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2025-05-27")
        @PastOrPresent(message = "Start date cannot be in the future")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2025-05-27")
        @PastOrPresent(message = "End date cannot be in the future")
        LocalDate endDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2025-05-27")
        @FutureOrPresent(message = "Next repair date must be today or in the future")
        LocalDate nextRepairDate,

        @NotNull(message = "Cost is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Cost must be zero or positive")
        BigDecimal cost,

        @NotNull(message = "Vehicle ID is required")
        Integer vehicleId,

        @Valid
        @NotEmpty(message = "At least one spare part must be provided")
        List<SparePartRequest> spareParts
) {
}
