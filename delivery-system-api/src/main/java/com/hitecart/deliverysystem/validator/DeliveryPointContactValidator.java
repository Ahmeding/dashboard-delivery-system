package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.DeliveryPointContactDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPointContactValidator {

    public static List<String> validate(DeliveryPointContactDto contactDto) {
        List<String> errors = new ArrayList<>();

        if (contactDto == null) {
            errors.add("Veuillez renseigner le prenom1'");
            errors.add("Veuillez renseigner le nom'");
            errors.add("Veuillez renseigner le numero de telephone'");
            errors.add("Veuillez renseigner l'email'");
            return errors;
        }
        if (!StringUtils.hasLength(contactDto.getFirstname())) {
            errors.add("Veuillez renseigner prenom'");
        }
        if (!StringUtils.hasLength(contactDto.getLastname())) {
            errors.add("Veuillez renseigner le nom'");
        }
        if (!StringUtils.hasLength(contactDto.getPhoneNum())) {
            errors.add("Veuillez renseigner le numero de telephone'");
        }
        if (!StringUtils.hasLength(contactDto.getEmail())) {
            errors.add("Veuillez renseigner l'email''");
        }
        return errors;
    }
}
