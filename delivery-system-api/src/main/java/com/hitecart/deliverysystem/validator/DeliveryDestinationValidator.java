package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.DeliveryDestinationDto;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDestinationValidator {

    public static List<String> validate(DeliveryDestinationDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Veuillez renseigner le client");
            errors.add("Veuillez renseigner le service");
            errors.add("Veuillez renseigner les points de livraison");
            errors.add("Veuillez renseigner l'email'");
            return errors;
        }
        if (dto.getServiceDeliveryId()== null) {
            errors.add("Veuillez renseigner le service'");
        }
        if (dto.getDeliveryPointsDto()== null || dto.getDeliveryPointsDto().isEmpty()) {
            errors.add("Veuillez renseigner les points de livraison");
        }
        return errors;
    }
}
