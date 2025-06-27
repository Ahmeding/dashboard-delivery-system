package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.deliverypointcontact.DeliveryPointContactRequest;
import com.hitecart.deliverysystem.utils.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPointContactValidator {

    public static List<String> validate(DeliveryPointContactRequest deliveryPointContactRequest) {
        List<String> errors = new ArrayList<>();

        if (deliveryPointContactRequest == null) {
            errors.add("Please provide the first name!");
            errors.add("Please provide the last name!");
            errors.add("Please provide the phone number!");
            errors.add("Please provide the email!");
            return errors;
        }

        if (!StringUtils.hasLength(deliveryPointContactRequest.firstname())) {
            errors.add("Please provide the first name!");
        }
        if (!StringUtils.hasLength(deliveryPointContactRequest.lastname())) {
            errors.add("Please provide the last name!");
        }
        if (!StringUtils.hasLength(deliveryPointContactRequest.phoneNbr())) {
            errors.add("Please provide the phone number!");
        }
        if(!StringUtils.hasLength(deliveryPointContactRequest.email()) || !EmailValidator.isValidEmail(deliveryPointContactRequest.email())){
            errors.add("Please provide a valid email address!");
        }

        return errors;

    }
}
