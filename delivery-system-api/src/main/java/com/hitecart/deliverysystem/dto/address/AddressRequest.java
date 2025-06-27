package com.hitecart.deliverysystem.dto.address;


import com.hitecart.deliverysystem.validator.postalcode.ValidAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents a physical address request.
 * <p>
 * Addresses follow international standards with country-specific validation.
 * All fields are trimmed and normalized during construction.
 * </p>
 *
 * @param street Primary street address (required)
 * @param additionalInfo Additional address details (e.g., apartment, floor)
 * @param city City or locality name (required)
 * @param postalCode Postal/ZIP code (required, format varies by country)
 * @param country ISO 3166-1 alpha-2 country code (required)
 */
@Schema(
        name = "AddressRequest",
        description = "Request payload for address information",
        accessMode = Schema.AccessMode.READ_WRITE
)
@ValidAddress // Add class-level validation
public record AddressRequest(
        @Schema(
                description = "Primary street address",
                example = "123 Main Street",
                minLength = 2,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Street address is required")
        @Size(min = 2, max = 100, message = "Street must be 2-100 characters")
        String street,

        @Schema(
                description = "Additional address information",
                example = "Apartment 4B",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        @Size(max = 50, message = "Additional info cannot exceed 50 characters")
        String additionalInfo,

        @Schema(
                description = "City or locality name",
                example = "Paris",
                minLength = 2,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "City is required")
        @Size(min = 2, max = 50, message = "City must be 2-50 characters")
        String city,

        @Schema(
                description = "Postal code format varies by country",
                example = "75001",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Postal code is required")
        //@ValidAddress(countryField = "country", message = "Invalid postal code for country")
        String postalCode,

        @Schema(
                description = "ISO 3166-1 alpha-2 country code",
                example = "FR",
                minLength = 2,
                maxLength = 2,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Country code is required")
        @Size(min = 2, max = 2, message = "Country code must be 2 characters")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Must be uppercase ISO country code")
        String country
) {
    /**
     * Compact constructor for normalization
     */
    public AddressRequest {
        street = normalizeString(street);
        additionalInfo = normalizeString(additionalInfo);
        city = normalizeString(city);
        postalCode = normalizeString(postalCode);
        country = country != null ? country.trim().toUpperCase() : null;
    }

    private String normalizeString(String value) {
        return value != null ? value.trim().replaceAll("\\s+", " ") : null;
    }
}
