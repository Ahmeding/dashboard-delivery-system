package com.hitecart.deliverysystem.dto.company;

import com.hitecart.deliverysystem.dto.address.AddressRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * Represents a request for creating or updating a company profile.
 * <p>
 * Companies represent business entities that utilize the delivery system,
 * either as service providers or clients. All tax identifiers are validated
 * according to regional specifications.
 * </p>
 *
 * @param id Unique identifier for existing companies (null when creating new)
 * @param name Legal business name
 * @param taxNbr Government-issued tax identification number
 * @param phoneNbr Primary business contact number
 * @param email Business contact email
 * @param address Registered business address
 */
@Schema(
        name = "CompanyRequest",
        description = "Request payload for company management operations",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record CompanyRequest(
        @Schema(
                description = "Unique ID for existing companies (omit for new entries)",
                example = "1001",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @PositiveOrZero(message = "ID must be positive or zero")
        Integer id,

        @Schema(
                description = "Legal business name",
                example = "Hitech Delivery Solutions",
                minLength = 2,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Company name is required")
        @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
        String name,

        @Schema(
                description = "Tax identification number (e.g., SIRET in France)",
                example = "12345678901234",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Tax number is required")
        @Pattern(regexp = "^[0-9]{14}$", message = "Tax number must be 14 digits")
        String taxNbr,

        @Schema(
                description = "Primary business phone number",
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
                description = "Business contact email",
                example = "contact@company.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(
                description = "Registered business address",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Valid
        @NotNull(message = "Address is required")
        AddressRequest address
) {}
