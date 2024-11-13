package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.ClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {
    public static List<String> validate(ClientDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner le nom du client !");
           errors.add("Veuillez renseigner le prenom du client !");
           errors.add("Veuillez renseigner le numreo du telephone  du client !");
           errors.add("Veuillez renseigner l'adresse email du client !");
           errors.add("Veuillez renseigner l'adresse email du client !");
/*           errors.add("Veuillez renseigner une description du client !");
           errors.add("Veuillez renseigner la siren du client !");
           errors.add("Veuillez renseigner la siret du client !");*/
           errors.addAll(AdresseValidator.validate(null));
        }
       if(!StringUtils.hasLength(dto.getLastname())){
           errors.add("Veuillez renseigner le nom du client !");

       }
       if(!StringUtils.hasLength(dto.getFirstname())){
           errors.add("Veuillez renseigner le prenom du client !");
       }
       if(!StringUtils.hasLength(dto.getPhoneNum())){
           errors.add("Veuillez renseigner le numreo du telephone  du client !");
       }
       if(!StringUtils.hasLength(dto.getEmail())){
           errors.add("Veuillez renseigner l'adresse email du client !");
       }
      /* if(!StringUtils.hasLength(dto.getDescription())){
           errors.add("Veuillez renseigner une description du client !");
       }
       if(!StringUtils.hasLength(dto.getSiren())){
           errors.add("Veuillez renseigner la siren du client !");
       }
       if(!StringUtils.hasLength(dto.getSiret())){
           errors.add("Veuillez renseigner la siret du client !");
       }*/
           errors.addAll(AdresseValidator.validate(dto.getAddress()));
    return errors;
    }
}
