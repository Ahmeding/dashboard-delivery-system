package com.hitecart.deliverysystem.dto.driver;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleRequest;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.model.enums.DriverExperience;
import com.hitecart.deliverysystem.model.enums.DrivingLicenseType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record DriverUpdateRequest(
        @Schema(
                description = "Legal first name",
                example = "Jean",
                minLength = 2,
                maxLength = 50
        )
        @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
        String firstname,

        @Schema(
                description = "Legal last name",
                example = "Dupont",
                minLength = 2,
                maxLength = 50
        )
        @Size(min = 2, max = 50, message = "Last name must be 2-50 characters")
        String lastname,

        @Schema(
                description = "Primary contact number in international format",
                example = "+33123456789"
        )
        @Pattern(
                regexp = "^\\+[0-9]{1,3}[0-9]{4,14}$",
                message = "Invalid phone format. Use +[country code][number]"
        )
        String phoneNbr,

        @Schema(
                description = "Professional email address",
                example = "driver.updated@example.com"
        )
        @Email(message = "Invalid email format")
        String email,

/*        @Schema(
                description = "System access credential (minimum 8 characters with mixed case and number)",
                example = "N3wS3cur3P@ss!",
                accessMode = Schema.AccessMode.WRITE_ONLY
        )
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "Password must contain uppercase, lowercase, and number"
        )
        String password,*/

        @Schema(
                description = "Date of birth in ISO format",
                type = "string",
                format = "date",
                example = "1985-05-15"
        )
        @Past(message = "Birth date must be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate,

        @Schema(
                description = "Primary residence address"
        )
        @Valid
        AddressRequest address,

        // ========================
        // Driver-Specific Section
        // ========================
        @Schema(
                description = "Government-issued driving license number",
                example = "DL-UPDATED-123"
        )
        @NotBlank(message = "License number must not be blank if provided")
        String drivinglicenseNbr,

        @Schema(
                description = "License classification type",
                example = "CLASS_C"
        )
        DrivingLicenseType drivinglicenseType,

        @Schema(
                description = "15-digit social security number",
                example = "987654321098765"
        )
        @Pattern(regexp = "^[0-9]{15}$", message = "SSN must be 15 digits")

        String secuNbr,

        @Schema(
                description = "French banking details (RIB)",
                example = "FR7698765432109876543210987"
        )
        @Pattern(regexp = "^FR[0-9]{23}$", message = "Invalid RIB format")

        String rib,

        @Schema(
                description = "Professional driving experience level",
                example = "EXPERT"
        )
        DriverExperience experienceLevel,

        @ArraySchema(
                arraySchema = @Schema(description = "Employment contracts"),
                schema = @Schema(implementation = EmploymentContractRequest.class)
        )
        @Valid
        @Size(max = 3, message = "Maximum 3 active contracts allowed")
        List<EmploymentContractRequest> employmentContracts,

        @ArraySchema(
                arraySchema = @Schema(description = "Vehicle assignments"),
                schema = @Schema(implementation = DriverVehicleRequest.class)
        )
        @Valid
        @Size(max = 5, message = "Maximum 5 vehicle assignments allowed")
        List<DriverVehicleRequest> assignments,

        @ArraySchema(
                minItems = 1,
                arraySchema = @Schema(
                        description = "System access roles",
                        requiredMode = Schema.RequiredMode.REQUIRED
                ),
                schema = @Schema(implementation = RoleRequest.class)
        )
        @Valid
        @NotEmpty(message = "At least one role is required")
        @Size(min = 1, max = 5, message = "Must have 1-5 roles")
        List<RoleRequest> roles
) {}
