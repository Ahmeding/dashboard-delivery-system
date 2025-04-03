package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.LigneCommandeClientDto;

import java.util.ArrayList;
import java.util.List;

public class LigneCommandeClientValidator {
    public static List<String> validate(LigneCommandeClientDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
            errors.add("Veuillez renseigner la quantite dans cette ligne de commande client !");
            errors.add("Veuillez renseigner le prix unitaire dans cette ligne de commande client !");
            return errors;
        }

        if(dto.getQuantite()==null){
            errors.add("Veuillez renseigner la quantite dans cette ligne de commande client !");
        }
        if(dto.getPrixUnitaire()==null){
            errors.add("Veuillez renseigner le prix unitaire dans cette ligne de commande client !");
        }
        return errors;
    }
}
