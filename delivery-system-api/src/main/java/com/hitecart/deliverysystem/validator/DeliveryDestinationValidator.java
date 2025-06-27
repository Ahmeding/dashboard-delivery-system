package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationRequest;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDestinationValidator {

    public static List<String> validate(DeliveryDestinationRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Please specify the client");
            errors.add("Please specify the service");
            errors.add("Please specify the delivery points");
            return errors;
        }

        if (request.deliveryServiceId() == null) {
            errors.add("Please specify the service");
        }
        if (request.deliveryPoints() == null || request.deliveryPoints().isEmpty()) {
            errors.add("Please specify the delivery points");
        }

        return errors;

    }
}
