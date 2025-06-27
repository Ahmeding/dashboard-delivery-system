package com.hitecart.deliverysystem.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.validator.age.Age;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
/**
 * Represents a request for creating or updating a client profile.
 * <p>
 * Clients can be individuals or businesses. Business clients require SIRET/SIREN identification,
 * while individual clients require personal information. All sensitive data is protected.
 * </p>
 *
 * @param id Unique identifier for existing clients (null when creating new)
 * @param firstname Legal first name (required for individuals)
 * @param lastname Legal last name (required for individuals)
 * @param phoneNbr Primary contact number
 * @param email Professional contact email
 * @param password System access credential
 * @param accountLocked Security status flag
 * @param enabled Operational status flag
 * @param birthDate Date of birth (required for individuals)
 * @param description Additional client notes
 * @param siret 14-digit French establishment identification
 * @param siren 9-digit French company identification
 * @param address Registered business or personal address
 * @param roles System access permissions
 */
@Schema(
        name = "ClientRequest",
        description = "Request payload for client management operations",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record ClientRequest(
        @Schema(
                description = "Unique ID for existing clients (omit for new entries)",
                example = "2001",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Integer id,

        @Schema(
                description = "Legal first name (required unless SIRET provided)",
                example = "Marie",
                minLength = 2,
                maxLength = 50
        )
        @NotBlank(groups = IndividualValidation.class, message = "First name is required for individuals")
        @Size(min = 2, max = 50, message = "First name must be 2-50 characters", groups = IndividualValidation.class)
        String firstname,

        @Schema(
                description = "Legal last name (required unless SIRET provided)",
                example = "Curie",
                minLength = 2,
                maxLength = 50
        )
        @NotBlank(groups = IndividualValidation.class, message = "Last name is required for individuals")
        @Size(min = 2, max = 50, message = "Last name must be 2-50 characters", groups = IndividualValidation.class)
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
                description = "Contact email address",
                example = "client@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(
                description = "System access credential (minimum 8 characters with mixed case and number)",
                example = "S3cur3P@ss!",
                requiredMode = Schema.RequiredMode.REQUIRED,
                accessMode = Schema.AccessMode.WRITE_ONLY
        )
        @NotBlank(message = "Password is required")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "Password must contain uppercase, lowercase, and number"
        )
        String password,

        @Schema(
                description = "Security lock status (true = locked)",
                example = "false",
                defaultValue = "false"
        )
        Boolean accountLocked,

        @Schema(
                description = "Operational status (true = active)",
                example = "true",
                defaultValue = "true"
        )
        Boolean enabled,

        @Schema(
                description = "Date of birth in ISO format (required for individuals)",
                type = "string",
                format = "date",
                example = "1985-05-15"
        )
        @Past(message = "Birth date must be in the past", groups = IndividualValidation.class)
        @Age(min = 18, message = "Client must be at least 18 years old", groups = IndividualValidation.class)
        LocalDate birthDate,

        @Schema(
                description = "Additional client information",
                example = "VIP customer with special delivery requirements",
                maxLength = 500
        )
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @Schema(
                description = "14-digit French establishment identifier",
                example = "12345678901234"
        )
        @Pattern(regexp = "^[0-9]{14}$", message = "SIRET must be 14 digits", groups = BusinessValidation.class)
        String siret,

        @Schema(
                description = "9-digit French company identifier",
                example = "123456789"
        )
        @Pattern(regexp = "^[0-9]{9}$", message = "SIREN must be 9 digits", groups = BusinessValidation.class)
        String siren,

        @Schema(
                description = "Primary address",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Valid
        @NotNull(message = "Address is required")
        AddressRequest address,

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
) {
        /**
         * Validation group for individual clients
         */
        public interface IndividualValidation {}

        /**
         * Validation group for business clients
         */
        public interface BusinessValidation {}
}
