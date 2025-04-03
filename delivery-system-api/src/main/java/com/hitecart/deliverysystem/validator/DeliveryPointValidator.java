package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.DeliveryPointDto;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPointValidator {
    public static List<String> validate(DeliveryPointDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
            errors.add("Veuillez renseigner le nom de la ville !");
            errors.add("Veuillez renseigner le client !");
            errors.add("Veuillez renseigner le livraison !");
            errors.add("Veuillez renseigner le type deu point de livraison!");
            errors.addAll(AdresseValidator.validate(null));
        }

        if(dto.getDelivDestinationId()==null){
            errors.add("Veuillez renseigner la destination !");
        }
        if(dto.getSchedule()==null){
            errors.add("Veuillez renseigner l'heure de livraison!");
        }
        if(dto.getDeliveryPointType()==null){
            errors.add("Veuillez renseigner le type deu point de livraison!");
        }
        errors.addAll(AdresseValidator.validate(dto.getAddressDto()));
        errors.addAll(DeliveryPointContactValidator.validate(dto.getContact()));
        return errors;
    }
}
