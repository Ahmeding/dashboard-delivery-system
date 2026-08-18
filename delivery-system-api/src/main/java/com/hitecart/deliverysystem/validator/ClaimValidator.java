package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.claim.ClaimRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClaimValidator {
    public static List<String> validate(ClaimRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {

            errors.add("Please provide the claim's title!");
            errors.add("Please provide the claim's description!");
            errors.add("Please provide the claim's type!");
            errors.add("Please provide the subject!");
        }

        if (!StringUtils.hasLength(request.title())) {
            errors.add("Please provide the claim's title!");
        }
        if (!StringUtils.hasLength(request.description())) {
            errors.add("Please provide the claim's description!");
        }
        if (!StringUtils.hasLength(String.valueOf(request.claimType()))) {
            errors.add("Please provide the claim's type!");
        }
        if (!StringUtils.hasLength(request.relatedEntityType())) {
            errors.add("Please provide the subject!");
        }
        return errors;
    }
}
