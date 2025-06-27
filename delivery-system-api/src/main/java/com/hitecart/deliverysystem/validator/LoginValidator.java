package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.SigninRequest;
import com.hitecart.deliverysystem.utils.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginValidator {
    public static List<String> validate(SigninRequest signinRequest) {
        List<String> errors = new ArrayList<>();
        if (signinRequest == null) {
            errors.add("Please provide the email address!");
            errors.add("Please provide the password!");
            return errors; // Return early if request is null to avoid NullPointerException
        }

        if(!StringUtils.hasLength(signinRequest.getEmail()) || !EmailValidator.isValidEmail(signinRequest.getEmail())){
            errors.add("Please provide a valid email address!");
        }

        if (!StringUtils.hasLength(signinRequest.getPassword())) {
            errors.add("Please provide the password!");
        }

        return errors;
    }
}
