package com.hitecart.deliverysystem.dto.driver;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleRequest;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.model.enums.DriverExperience;
import com.hitecart.deliverysystem.model.enums.DrivingLicenseType;
import com.hitecart.deliverysystem.validator.age.Age;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
/**
 * Represents a request for creating or updating a driver profile.
 * <p>
 * Includes personal information, professional credentials, employment details,
 * and system access roles. All financial and identity numbers are validated
 * for format and security compliance.
 * </p>
 *
 * @param id Unique identifier for existing drivers (null when creating new)
 * @param firstname Driver's legal first name
 * @param lastname Driver's legal last name
 * @param phoneNbr Primary contact number
 * @param email Professional email address
 * @param password System access credential (write-only)
 * @param birthDate Date of birth
 * @param address Primary residence
 * @param drivinglicenseNbr Official license identifier
 * @param drivinglicenseType License classification
 * @param secuNbr Social security identifier
 * @param rib Banking details
 * @param experienceLevel Professional driving experience
 * @param employmentContracts Active employment agreements
 * @param assignments Vehicle assignments
 * @param roles System access permissions
 */
@Schema(
        name = "DriverRequest",
        description = "Request payload for driver management operations",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record DriverRequest(
        @Schema(
                description = "Unique ID for existing drivers (omit for new entries)",
                example = "42",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Integer id,

        @Schema(
                description = "Legal first name",
                example = "Jean",
                minLength = 2,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be 2–50 characters")
        String firstname,

        @Schema(
                description = "Legal last name",
                example = "Dupont",
                minLength = 2,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be 2–50 characters")
        String lastname,

        @Schema(
                description = "Primary contact number in international format",
                example = "+33123456789",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\+[0-9]{1,3}[0-9]{4,14}$",
                message = "Invalid phone format. Use +[country code][number]"
        )
        String phoneNbr,

        @Schema(
                description = "Professional email address",
                example = "driver@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(
                description = "System access credential (minimum 8 characters with mixed case, number, and special character)",
                example = "S3cur3P@ss!",
                requiredMode = Schema.RequiredMode.REQUIRED,
                accessMode = Schema.AccessMode.WRITE_ONLY
        )
        @NotBlank(message = "Password is required")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        )
        String password,

        @Schema(
                description = "Date of birth in ISO format",
                type = "string",
                format = "date",
                example = "1980-01-01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Age(min = 18, message = "Driver must be at least 18 years old")
        LocalDate birthDate,

        @Schema(
                description = "Primary residence address",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Valid
        @NotNull(message = "Address is required")
        AddressRequest address,

        // ========================
        // Driver-Specific Section
        // ========================
        @Schema(
                description = "Government-issued driving license number",
                example = "DL123456789FR",
                minLength = 5,
                maxLength = 20,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "License number is required")
        @Size(min = 5, max = 20, message = "License number must be 5-20 characters")
        String drivinglicenseNbr,

        @Schema(
                description = "License classification type",
                implementation = DrivingLicenseType.class,
                example = "CLASS_C",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "License type is required")
        DrivingLicenseType drivinglicenseType,

        @Schema(
                description = "15-digit social security number",
                example = "123456789012345",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Social security number is required")
        @Pattern(regexp = "^[0-9]{15}$", message = "Must be 15 digits")
        //@EncryptedField // Custom annotation for encryption handling
        String secuNbr,

        @Schema(
                description = "French banking details (RIB)",
                example = "FR7612345678901234567890123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "RIB is required")
        @Pattern(regexp = "^FR[0-9]{23}$", message = "Invalid RIB format")
        //
        String rib,

        @Schema(
                description = "Professional driving experience level",
                implementation = DriverExperience.class,
                example = "SENIOR",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Experience level is required")
        DriverExperience experienceLevel,

        @ArraySchema(
                minItems = 1,
                arraySchema = @Schema(
                        description = "Active employment contracts",
                        requiredMode = Schema.RequiredMode.REQUIRED
                ),
                schema = @Schema(implementation = EmploymentContractRequest.class)
        )
        @Valid
        @NotEmpty(message = "At least one employment contract is required")
        @Size(max = 3, message = "Maximum 3 active contracts allowed")
        List<EmploymentContractRequest> employmentContracts,

        @ArraySchema(
                arraySchema = @Schema(
                        description = "Vehicle assignments",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED
                ),
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
