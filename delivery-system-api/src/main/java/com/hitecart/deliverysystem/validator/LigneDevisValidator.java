package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.LigneDevisDto;

import java.util.ArrayList;
import java.util.List;

public class LigneDevisValidator {
    public static List<String> validate(LigneDevisDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner la quantite dans cette ligne du devis !");
           errors.add("Veuillez renseigner le prix unitaire dans cette ligne du devis !");
            return errors;
        }

       if(dto.getQuantite()==null){
           errors.add("Veuillez renseigner la quantite dans cette ligne du devis !");
       }
       if(dto.getPrixUnitaire()==null){
           errors.add("Veuillez renseigner le prix unitaire dans cette ligne du devis !");
       }
    return errors;
    }
}
