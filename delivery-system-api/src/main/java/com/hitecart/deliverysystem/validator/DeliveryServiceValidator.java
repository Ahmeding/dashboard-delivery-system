package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DeliveryServiceValidator {
    public static List<String> validate(DeliveryServiceRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            return errors;
        }

        if (request.color() == null) {
            errors.add("Please provide the color!");
            errors.add("Please provide the destination city!");
            errors.add("Please provide the destination country!");
            errors.add("Please provide the distance in km!");
            errors.add("Please provide the basic trip cost!");
            errors.add("Please provide the pickup location!");
            errors.add("Please provide the title!");
            return errors;
        }

        if (request.destinationCity() == null) {
            errors.add("Please provide the destination city!");
        }
        if (request.destinationCountry() == null) {
            errors.add("Please provide the destination country!");
        }
        if (request.distance() == null) {
            errors.add("Please provide the distance in km!");
        }
        if (request.basicTripCost() == null) {
            errors.add("Please provide the basic trip cost!");
        }
        if (request.pickUpLocation() == null) {
            errors.add("Please provide the pickup location!");
        }
        if (request.title() == null) {
            errors.add("Please provide the title!");
        }

        return errors;

    }
}
