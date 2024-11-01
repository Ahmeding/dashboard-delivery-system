package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.EntrepriseDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseValidator {
    public static List<String> validate(EntrepriseDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner le raison social de l' entreprise !");
           errors.add("Veuillez renseigner le numero du telephone de l' entreprise !");
           errors.add("Veuillez renseigner l'adresse email de l' entreprise !");
           errors.add("Veuillez renseigner le numero fiscal de l' entreprise !");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }
       if(!StringUtils.hasLength(dto.getRaisonSocial())){
           errors.add("Veuillez renseigner le raison social de l' entreprise !");
       }
       if(!StringUtils.hasLength(dto.getNumtel())){
           errors.add("Veuillez renseigner le numero du telephone de l' entreprise !");
       }
       if(!StringUtils.hasLength(dto.getAdresseEmail())){
           errors.add("Veuillez renseigner l'adresse email de l' entreprise !");
       }
       if(!StringUtils.hasLength(dto.getNumFiscla())){
           errors.add("Veuillez renseigner le numero fiscal de l' entreprise !");
       }
        errors.addAll(AdresseValidator.validate(dto.getAdresseDto()));
    return errors;
    }
}
