package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.address.AddressRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AdresseValidator {

    public static List<String> validate(AddressRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Please provide address line 1!");
            errors.add("Please provide the city!");
            errors.add("Please provide the country!");
            errors.add("Please provide the postal code!");
            return errors;
        }
        if (!StringUtils.hasLength(request.street())) {
            errors.add("Please provide address line 1!");
        }
        if (!StringUtils.hasLength(request.city())) {
            errors.add("Please provide the city!");
        }
        if (!StringUtils.hasLength(request.country())) {
            errors.add("Please provide the country!");
        }
        if (!StringUtils.hasLength(request.postalCode())) {
            errors.add("Please provide the postal code!");
        }
        return errors;
    }
}
