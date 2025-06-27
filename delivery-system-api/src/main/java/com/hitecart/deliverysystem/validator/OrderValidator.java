package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.order.OrderRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderValidator {
    public static List<String> validate(OrderRequest orderRequest){
        List<String> errors= new ArrayList<>();

        if (orderRequest == null) {
            errors.add("Please provide the order code!");
            errors.add("Please provide the client for the order!");
            errors.add("Please provide the order date!");
            return errors;
        }

        if (!StringUtils.hasLength(orderRequest.code())) {
            errors.add("Please provide the order code!");
        }

        if (orderRequest.clientId() == null) {
            errors.add("Please provide the client for the order!");
        }

        if (orderRequest.orderDate() == null) {
            errors.add("Please provide the order date!");
        }

        return errors;

    }
}
