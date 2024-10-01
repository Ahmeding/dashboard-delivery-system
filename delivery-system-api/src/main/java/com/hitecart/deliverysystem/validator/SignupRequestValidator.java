package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.SignupRequest;
import com.hitecart.deliverysystem.dto.UtilisateurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SignupRequestValidator {
    public static List<String> validate(SignupRequest dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){

            errors.add("Veuillez renseigner le nom du utilisateur !");
            errors.add("Veuillez renseigner le prenom du utilisateur !");
            errors.add("Veuillez renseigner le numreo du telephone  du utilisateur !");
            errors.add("Veuillez renseigner l'adresse email du utilisateur !");
            errors.add("Veuillez renseigner le mot de passe du utilisateur !");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }
       if(!StringUtils.hasLength(dto.getLastName())){
           errors.add("Veuillez renseigner le nom du utilisateur !");
       }
       if(!StringUtils.hasLength(dto.getFirstName())){
           errors.add("Veuillez renseigner le prenom du utilisateur !");
       }
       if(!StringUtils.hasLength(dto.getEmail())){
           errors.add("Veuillez renseigner l'adresse email du utilisateur !");
       }
       if(!StringUtils.hasLength(dto.getPassword())){
           errors.add("Veuillez renseigner le mot de passe du utilisateur !");
       }
    return errors;
    }
}
