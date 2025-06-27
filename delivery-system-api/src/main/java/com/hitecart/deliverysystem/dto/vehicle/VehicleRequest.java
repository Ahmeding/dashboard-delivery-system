package com.hitecart.deliverysystem.dto.vehicle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.EnergyType;
import com.hitecart.deliverysystem.model.enums.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a request for creating or updating a vehicle.
 * <p>
 * Contains all necessary information to register or modify a delivery vehicle,
 * including identification details, specifications, and operational status.
 * </p>
 *
 * @param id Unique vehicle identifier (null for new vehicles)
 * @param logo URL to vehicle brand logo
 * @param vehicleType Classification of vehicle
 * @param registrationNbr Official license plate number
 * @param brand Manufacturer brand name
 * @param model Vehicle model name
 * @param vinNbr 17-character Vehicle Identification Number
 * @param dfcDate Date of first circulation (first registration)
 * @param mileage Current distance traveled in kilometers
 * @param energyType Primary power source
 * @param weightCapacity Maximum cargo weight capacity in kilograms
 * @param volumeCapacity Maximum cargo volume capacity in cubic meters
 * @param ctExpDate Technical control certificate expiration date
 * @param isActive Operational status flag
 * @param isAssigned Indicates if vehicle is currently assigned to a mission
 * @param isAvailable Indicates if vehicle is available for new assignments
 */
@Schema(
        name = "VehicleRequest",
        description = "Request payload for vehicle management operations",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record VehicleRequest(
        @Schema(
                description = "Unique ID for existing vehicles (omit for new entries)",
                example = "101",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Integer id,

        @Schema(
                description = "URL to vehicle manufacturer logo",
                example = "https://example.com/logo.png",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Logo URL is required")
        String logo,

        @Schema(
                description = "Vehicle classification type",
                implementation = VehicleType.class,
                example = "VAN",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Vehicle type is required")
        VehicleType vehicleType,

        @Schema(
                description = "Official license plate number",
                example = "AB-123-CD",
                minLength = 1,
                maxLength = 12,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Registration number is required")
        @Size(min = 1, max = 12, message = "Registration number must be 1–12 characters")
        String registrationNbr,

        @Schema(
                description = "Vehicle manufacturer brand",
                example = "Ford",
                minLength = 1,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Brand is required")
        @Size(min = 1, max = 50, message = "Brand must be 1–50 characters")
        String brand,

        @Schema(
                description = "Vehicle model name",
                example = "Transit",
                minLength = 1,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Model is required")
        @Size(min = 1, max = 50, message = "Model must be 1–50 characters")
        String model,

        @Schema(
                description = "17-character Vehicle Identification Number",
                example = "1FM5K7F82DGA11762",
                minLength = 17,
                maxLength = 17,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "VIN number is required")
        @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
        String vinNbr,

        @Schema(
                description = "Date of first registration in ISO format",
                type = "string",
                format = "date",
                example = "2020-01-15",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Date of first circulation is required")
        @PastOrPresent(message = "Date of first circulation cannot be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dfcDate,

        @Schema(
                description = "Current mileage in kilometers",
                example = "12500.75",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Mileage is required")
        @DecimalMin(value = "0", inclusive = true, message = "Mileage must be zero or positive")
        @Digits(integer = 7, fraction = 2, message = "Mileage must have max 7 whole digits and 2 decimals")
        BigDecimal mileage,

        @Schema(
                description = "Primary energy source",
                implementation = EnergyType.class,
                example = "ELECTRIC",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Energy type is required")
        EnergyType energyType,

        @Schema(
                description = "Maximum cargo weight capacity in kilograms",
                example = "1500.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Weight capacity is required")
        @DecimalMin(value = "0", inclusive = true, message = "Weight capacity must be zero or positive")
        @Digits(integer = 5, fraction = 2, message = "Weight capacity must have max 5 whole digits and 2 decimals")
        BigDecimal weightCapacity,

        @Schema(
                description = "Maximum cargo volume capacity in cubic meters",
                example = "12.5",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Volume capacity is required")
        @DecimalMin(value = "0", inclusive = true, message = "Volume capacity must be zero or positive")
        @Digits(integer = 4, fraction = 2, message = "Volume capacity must have max 4 whole digits and 2 decimals")
        BigDecimal volumeCapacity,

        @Schema(
                description = "Technical control certificate expiration date",
                type = "string",
                format = "date",
                example = "2026-05-30",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Control technique expiry date is required")
        @Future(message = "Control technique expiry date must be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate ctExpDate,

        @Schema(
                description = "Operational status (true = active, false = inactive)",
                example = "true",
                defaultValue = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Active status is required")
        Boolean isActive,

        @Schema(
                description = "Assignment status (true = currently assigned to mission)",
                example = "false",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        boolean isAssigned,

        @Schema(
                description = "Availability status (true = available for new missions)",
                example = "true",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        boolean isAvailable
) {}
