package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FeedbackValidator {

    public static List<String> validate(FeedbackRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Please provide comment!");
            errors.add("Please provide driver!");
            errors.add("Please provide note!");
            return errors;
        }
        if (!StringUtils.hasLength(request.comment())) {
            errors.add("Please provide comment!");
        }
        if (request.driverId() == null) {
            errors.add("Please provide driver!");
        }
        if (request.note() == null) {
            errors.add("Please provide note!");
        }
        return errors;
    }
}
