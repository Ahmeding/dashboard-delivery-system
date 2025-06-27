package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.quoteline.QuoteLineRequest;

import java.util.ArrayList;
import java.util.List;

public class QuoteLineValidator {
    public static List<String> validate(QuoteLineRequest quoteLineRequest){
        List<String> errors= new ArrayList<>();
        if (quoteLineRequest == null) {
            errors.add("Please provide the quantity in this quote line!");
            errors.add("Please provide the unit price in this quote line!");
            return errors;
        }

        if (quoteLineRequest.quantity() == null) {
            errors.add("Please provide the quantity in this quote line!");
        }

        if (quoteLineRequest.unitPrice() == null) {
            errors.add("Please provide the unit price in this quote line!");
        }

        return errors;

    }
}
