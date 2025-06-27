package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SparePartValidator {
    public static List<String> validate(SparePartRequest sparePartRequest){
        List<String> errors= new ArrayList<>();

        if (sparePartRequest == null) {
            errors.add("Please provide the name of the spare part!");
            errors.add("Please provide the price of the spare part!");
            return errors;
        }

        if (!StringUtils.hasLength(sparePartRequest.name())) {
            errors.add("Please provide the name of the spare part!");
        }

        if (sparePartRequest.price() == null) {
            errors.add("Please provide the price of the spare part!");
        }

        return errors;

    }
}
