package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.DevisDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DevisValidator {
    public static List<String> validate(DevisDto dto){
        List<String> errors= new ArrayList<>();
        if(dto==null){
            errors.add("Veuillez renseigner le code du Devis !");
            errors.add("Veuillez renseigner le client du Devis !");
            errors.add("Veuillez renseigner la date du Devis !");
            return errors;
        }
        if(!StringUtils.hasLength(dto.getCode())){
            errors.add("Veuillez renseigner le code du Devis !");
        }
        if(dto.getClientId()==null){
            errors.add("Veuillez renseigner le client du Devis !");
        }
        if(dto.getDateDevis()==null){
            errors.add("Veuillez renseigner la date du Devis !");
        }
        return errors;
    }
}
