package com.hitecart.deliverysystem.validator.postalcode;


import com.hitecart.deliverysystem.dto.address.AddressRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<ValidAddress, AddressRequest> {

    @Override
    public boolean isValid(AddressRequest address, ConstraintValidatorContext context) {
        if (address == null || address.country() == null || address.postalCode() == null) {
            return true; // Let @NotNull handle nulls
        }

        String postalCode = address.postalCode().trim();
        String country = address.country().trim().toUpperCase();

        return switch (country) {
            case "FR" -> postalCode.matches("^[0-9]{5}$");  // France
            case "US" -> postalCode.matches("^[0-9]{5}(?:-[0-9]{4})?$"); // USA
            case "CA" -> postalCode.matches("^[A-Z][0-9][A-Z] ?[0-9][A-Z][0-9]$"); // Canada
            case "DE" -> postalCode.matches("^[0-9]{5}$"); // Germany
            case "UK" -> postalCode.matches("^[A-Z]{1,2}[0-9][A-Z0-9]? ?[0-9][A-Z]{2}$"); // UK
            default -> postalCode.matches("^[a-zA-Z0-9\\-\\s]{3,10}$"); // Generic
        };
    }
}
