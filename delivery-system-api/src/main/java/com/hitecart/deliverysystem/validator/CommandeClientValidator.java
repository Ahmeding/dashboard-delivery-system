package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.CommandeClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeClientValidator {
    public static List<String> validate(CommandeClientDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner le code de la commandeClient !");
           errors.add("Veuillez renseigner le client de la commandeClient !");
           errors.add("Veuillez renseigner la date de la commandeClient !");
        }
       if(!StringUtils.hasLength(dto.getCode())){
           errors.add("Veuillez renseigner le code de la commandeClient !");
       }
       if(dto.getClientDto()==null){
           errors.add("Veuillez renseigner le client de la commandeClient !");
       }
       if(dto.getDateCommnde()==null){
           errors.add("Veuillez renseigner la date de la commandeClient !");
       }
    return errors;
    }
}
