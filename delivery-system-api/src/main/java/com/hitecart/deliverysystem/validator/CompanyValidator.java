package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.company.CompanyRequest;
import com.hitecart.deliverysystem.utils.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CompanyValidator {
    public static List<String> validate(CompanyRequest request){
        List<String> errors= new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the company name!");
            errors.add("Please provide the company phone number!");
            errors.add("Please provide the company email address!");
            errors.add("Please provide the company tax number!");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(request.name())) {
            errors.add("Please provide the company name!");
        }
        if (!StringUtils.hasLength(request.phoneNbr())) {
            errors.add("Please provide the company phone number!");
        }
        if(!StringUtils.hasLength(request.email()) || !EmailValidator.isValidEmail(request.email())){
            errors.add("Please provide a valid email address!");
        }
        if (!StringUtils.hasLength(request.taxNbr())) {
            errors.add("Please provide the company tax number!");
        }

        errors.addAll(AdresseValidator.validate(request.address()));
        return errors;

    }
}
