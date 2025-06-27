package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.client.ClientRequest;
import com.hitecart.deliverysystem.dto.client.ClientUpdateRequest;
import com.hitecart.deliverysystem.utils.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {
    public static List<String> validate(ClientRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the client's last name!");
            errors.add("Please provide the client's first name!");
            errors.add("Please provide the client's phone number!");
            errors.add("Please provide the client's email address!");
            errors.add("Please provide the client's email address!");
            /* errors.add("Please provide a description of the client!");
               errors.add("Please provide the client's SIREN number!");
               errors.add("Please provide the client's SIRET number!"); */
            errors.addAll(AdresseValidator.validate(null));
        }

        if (!StringUtils.hasLength(request.lastname())) {
            errors.add("Please provide the client's last name!");
        }
        if (!StringUtils.hasLength(request.firstname())) {
            errors.add("Please provide the client's first name!");
        }
        if (!StringUtils.hasLength(request.phoneNbr())) {
            errors.add("Please provide the client's phone number!");
        }
        if (!StringUtils.hasLength(request.email()) || !EmailValidator.isValidEmail(request.email())) {
            errors.add("Please provide a valid email address!");
        }
        /* if (!StringUtils.hasLength(request.getDescription())) {
             errors.add("Please provide a description of the client!");
        }
        if (!StringUtils.hasLength(request.getSiren())) {
             errors.add("Please provide the client's SIREN number!");
        }
        if (!StringUtils.hasLength(request.getSiret())) {
             errors.add("Please provide the client's SIRET number!");
        } */
        errors.addAll(AdresseValidator.validate(request.address()));
        return errors;
    }

    public static List<String> validate(ClientUpdateRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the client's last name!");
            errors.add("Please provide the client's first name!");
            errors.add("Please provide the client's phone number!");
            errors.add("Please provide the client's email address!");
            errors.add("Please provide the client's email address!");
            /* errors.add("Please provide a description of the client!");
               errors.add("Please provide the client's SIREN number!");
               errors.add("Please provide the client's SIRET number!"); */
            errors.addAll(AdresseValidator.validate(null));
        }

        if (!StringUtils.hasLength(request.lastname())) {
            errors.add("Please provide the client's last name!");
        }
        if (!StringUtils.hasLength(request.firstname())) {
            errors.add("Please provide the client's first name!");
        }
        if (!StringUtils.hasLength(request.phoneNbr())) {
            errors.add("Please provide the client's phone number!");
        }
        if (!StringUtils.hasLength(request.email()) || !EmailValidator.isValidEmail(request.email())) {
            errors.add("Please provide a valid email address!");
        }
        /* if (!StringUtils.hasLength(request.getDescription())) {
             errors.add("Please provide a description of the client!");
        }
        if (!StringUtils.hasLength(request.getSiren())) {
             errors.add("Please provide the client's SIREN number!");
        }
        if (!StringUtils.hasLength(request.getSiret())) {
             errors.add("Please provide the client's SIRET number!");
        } */
        errors.addAll(AdresseValidator.validate(request.address()));
        return errors;
    }
}
