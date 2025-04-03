package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.LigneFraisMissionDto;

import java.util.ArrayList;
import java.util.List;

public class LigneFraisMissionValidator {
    public static List<String> validate(LigneFraisMissionDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner le type du frais mission !");
           errors.add("Veuillez renseigner la quantite du frais mission !");
           errors.add("Veuillez renseigner la date du frais de mission !");
           errors.add("Veuillez renseigner le cout du frais de mission !");
           return errors;
        }
       if(dto.getType()!=null){
           errors.add("Veuillez renseigner le type du frais mission !");
       }
       if(dto.getQuantite()==null){
           errors.add("Veuillez renseigner la quatite du frais mission !");
       }

       if(dto.getDateAchat()==null){
           errors.add("Veuillez renseigner la date du frais de mission !");
       }

       if(dto.getCout()==null){
           errors.add("Veuillez renseigner le cout du frais de mission !");
       }

    return errors;
    }
}
