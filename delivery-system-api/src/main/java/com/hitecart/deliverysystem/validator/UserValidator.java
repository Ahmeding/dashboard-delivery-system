package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.user.UserRequest;
import com.hitecart.deliverysystem.utils.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {
    public static List<String> validate(UserRequest userRequest){
        List<String> errors= new ArrayList<>();

        if (userRequest == null) {
            errors.add("Please provide the user's last name!");
            errors.add("Please provide the user's first name!");
            errors.add("Please provide the user's phone number!");
            errors.add("Please provide the user's email address!");
            errors.add("Please provide the user's password!");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(userRequest.lastname())) {
            errors.add("Please provide the user's last name!");
        }

        if (!StringUtils.hasLength(userRequest.firstname())) {
            errors.add("Please provide the user's first name!");
        }

        if (!StringUtils.hasLength(userRequest.phoneNbr())) {
            errors.add("Please provide the user's phone number!");
        }

        if (!StringUtils.hasLength(userRequest.email())) {
            errors.add("Please provide the user's email address!");
        }

        if (!StringUtils.hasLength(userRequest.password())) {
            errors.add("Please provide the user's password!");
        }

        errors.addAll(AdresseValidator.validate(userRequest.address()));
        return errors;

    }
    public static List<String> validateForUpdate(UserRequest userRequest){
        List<String> errors= new ArrayList<>();

        if (userRequest == null) {
            errors.add("Please provide the user's last name!");
            errors.add("Please provide the user's first name!");
            errors.add("Please provide the user's phone number!");
            errors.add("Please provide the user's email address!");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(userRequest.lastname())) {
            errors.add("Please provide the user's last name!");
        }

        if (!StringUtils.hasLength(userRequest.firstname())) {
            errors.add("Please provide the user's first name!");
        }

        if (!StringUtils.hasLength(userRequest.phoneNbr())) {
            errors.add("Please provide the user's phone number!");
        }

        if(!StringUtils.hasLength(userRequest.email()) || !EmailValidator.isValidEmail(userRequest.email())){
            errors.add("Please provide a valid email address!");
        }

        errors.addAll(AdresseValidator.validate(userRequest.address()));
        return errors;

    }
}
