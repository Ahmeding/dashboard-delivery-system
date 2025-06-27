package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointRequest;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPointValidator {
    public static List<String> validate(DeliveryPointRequest request){
        List<String> errors= new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the city name!");
            errors.add("Please provide the client!");
            errors.add("Please provide the delivery information!");
            errors.add("Please provide the delivery point type!");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }

        if (request.deliveryDestinationId() == null) {
            errors.add("Please provide the destination!");
        }
        if (request.schedule() == null) {
            errors.add("Please provide the delivery time!");
        }
        if (request.type() == null) {
            errors.add("Please provide the delivery point type!");
        }

        errors.addAll(AdresseValidator.validate(request.address()));
        errors.addAll(DeliveryPointContactValidator.validate(request.contact()));

        return errors;

    }
}
