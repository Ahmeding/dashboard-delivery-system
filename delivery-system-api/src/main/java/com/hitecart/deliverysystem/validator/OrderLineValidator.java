package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.orderline.OrderLineRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderLineValidator {
    public static List<String> validate(OrderLineRequest orderLineRequest){
        List<String> errors= new ArrayList<>();

        if (orderLineRequest == null) {
            errors.add("Please provide the quantity for this customer order line!");
            errors.add("Please provide the unit price for this customer order line!");
            return errors;
        }

        if (orderLineRequest.quantity() == null) {
            errors.add("Please provide the quantity for this customer order line!");
        }

        if (orderLineRequest.unitPrice() == null) {
            errors.add("Please provide the unit price for this customer order line!");
        }

        return errors;

    }
}
