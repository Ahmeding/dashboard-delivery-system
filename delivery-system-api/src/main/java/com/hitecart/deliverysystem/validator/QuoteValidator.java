package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.quote.QuoteRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QuoteValidator {
    public static List<String> validate(QuoteRequest quoteRequest){
        List<String> errors= new ArrayList<>();
        if (quoteRequest == null) {
            errors.add("Please provide the quote code!");
            errors.add("Please provide the client for the quote!");
            errors.add("Please provide the quote date!");
            return errors;
        }

        if (!StringUtils.hasLength(quoteRequest.code())) {
            errors.add("Please provide the quote code!");
        }

        if (quoteRequest.clientId() == null) {
            errors.add("Please provide the client for the quote!");
        }

        if (quoteRequest.quoteDate() == null) {
            errors.add("Please provide the quote date!");
        }

        return errors;

    }
}
