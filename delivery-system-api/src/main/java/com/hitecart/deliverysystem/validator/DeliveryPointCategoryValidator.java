package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCategoryRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPointCategoryValidator {
    public static List<String> validate(DeliveryPointCategoryRequest request){
        List<String> errors= new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the category name!");
            errors.add("Please provide the value of the additional fees!");
            return errors;
        }

        if (request.name() == null || !StringUtils.hasLength(request.name())) {
            errors.add("Please provide the category name!");
        }
        if (request.additionalFees() == null) {
            errors.add("Please provide the value of the additional fees!");
        }

        return errors;

    }
}
