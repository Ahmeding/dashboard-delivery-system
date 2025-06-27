package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.utils.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DriverValidator {
    public static List<String> validate(DriverRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the driver's last name!");
            errors.add("Please provide the driver's first name!");
            errors.add("Please provide the driver's phone number!");
            errors.add("Please provide the driver's email address!");
            errors.add("Please provide the driver's password!");
            errors.add("Please provide the driver's social security number!");
            errors.add("Please provide the driver's bank account (RIB)!");
            errors.add("Please provide the driver's license type!");
            errors.add("Please provide the driver's date of birth!");
            errors.add("Please provide the driver's license number!");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(request.drivinglicenseNbr())) {
            errors.add("Please provide the driver's license number!");
        }
        if (!StringUtils.hasLength(request.lastname())) {
            errors.add("Please provide the driver's last name!");
        }
        if (!StringUtils.hasLength(request.firstname())) {
            errors.add("Please provide the driver's first name!");
        }
        if (!StringUtils.hasLength(request.phoneNbr())) {
            errors.add("Please provide the driver's phone number!");
        }
        if(!StringUtils.hasLength(request.email()) || !EmailValidator.isValidEmail(request.email())){
            errors.add("Please provide a valid email address!");
        }
        if (!StringUtils.hasLength(request.password())) {
            errors.add("Please provide the driver's password!");
        }
        if (!StringUtils.hasLength(request.secuNbr())) {
            errors.add("Please provide the driver's social security number!");
        }
        if (!StringUtils.hasLength(request.rib())) {
            errors.add("Please provide the driver's bank account (RIB)!");
        }
        if (request.drivinglicenseType() == null) {
            errors.add("Please provide the driver's license type!");
        }
        if (!StringUtils.hasLength(request.drivinglicenseNbr())) {
            errors.add("Please provide the driver's license number!");
        }
        if (request.birthDate() == null) {
            errors.add("Please provide the driver's date of birth!");
        }

        errors.addAll(AdresseValidator.validate(request.address()));
        return errors;

    }
}
