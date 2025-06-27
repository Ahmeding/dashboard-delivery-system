package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.SignupRequest;
import com.hitecart.deliverysystem.utils.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SignupRequestValidator {
    public static List<String> validate(SignupRequest signupRequest){
        List<String> errors= new ArrayList<>();

        if (signupRequest == null) {
            errors.add("Please provide the user's last name!");
            errors.add("Please provide the user's first name!");
            errors.add("Please provide the user's phone number!");
            errors.add("Please provide the user's email address!");
            errors.add("Please provide the user's password!");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(signupRequest.getLastName())) {
            errors.add("Please provide the user's last name!");
        }

        if (!StringUtils.hasLength(signupRequest.getFirstName())) {
            errors.add("Please provide the user's first name!");
        }

        if(!StringUtils.hasLength(signupRequest.getEmail())
                || !EmailValidator.isValidEmail(signupRequest.getEmail())){
            errors.add("Please provide a valid email address!");
        }

        if (!StringUtils.hasLength(signupRequest.getPassword())) {
            errors.add("Please provide the user's password!");
        }

        return errors;

    }
}
