package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.SigninRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginValidator {
    public static List<String> validate(SigninRequest signinRequest) {
        List<String> errors = new ArrayList<>();
        if(signinRequest == null) {
            errors.add("Veuillez renseigner l'adresse email!");
            errors.add("Veuillez renseigner le mot de passe!");
        }
    if(!StringUtils.hasLength(signinRequest.getEmail())){
            errors.add("Veuillez renseigner l'adresse email!");
    }
    if(!StringUtils.hasLength(signinRequest.getPassword())){
            errors.add("Veuillez renseigner le mote de passe!");
    }
        return errors;
    }
}
