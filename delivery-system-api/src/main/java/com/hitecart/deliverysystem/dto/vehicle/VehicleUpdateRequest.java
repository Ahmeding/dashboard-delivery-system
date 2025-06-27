package com.hitecart.deliverysystem.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.EnergyType;
import com.hitecart.deliverysystem.model.enums.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VehicleUpdateRequest(

        @NotBlank(message = "Logo URL is required")
        String logo,

        @NotNull(message = "Vehicle type is required")
        VehicleType vehicleType,

        @NotBlank(message = "Registration number is required")
        @Size(min = 1, max = 20, message = "Registration number must be 1–20 characters")
        String registrationNbr,

        @NotBlank(message = "Brand is required")
        @Size(min = 1, max = 50, message = "Brand must be 1–50 characters")
        String brand,

        @NotBlank(message = "Model is required")
        @Size(min = 1, max = 50, message = "Model must be 1–50 characters")
        String model,

        @NotBlank(message = "VIN number is required")
        @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
        String vinNbr,

        @NotNull(message = "Date of first circulation is required")
        @PastOrPresent(message = "Date of first circulation cannot be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2020-01-15")
        LocalDate dfcDate,

        @NotNull(message = "Mileage is required")
        @DecimalMin(value = "0", inclusive = true, message = "Mileage must be zero or positive")
        BigDecimal mileage,

        @NotNull(message = "Energy type is required")
        EnergyType energyType,

        @NotNull(message = "Weight capacity is required")
        @DecimalMin(value = "0", inclusive = true, message = "Weight capacity must be zero or positive")
        BigDecimal weightCapacity,

        @NotNull(message = "Volume capacity is required")
        @DecimalMin(value = "0", inclusive = true, message = "Volume capacity must be zero or positive")
        BigDecimal volumeCapacity,

        @NotNull(message = "Control technique expiry date is required")
        @Future(message = "Control technique expiry date must be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2026-05-30")
        LocalDate ctExpDate,

        @NotNull(message = "Active status is required")
        Boolean isActive,

        boolean isAssigned,
        boolean isAvailable

) {
}
