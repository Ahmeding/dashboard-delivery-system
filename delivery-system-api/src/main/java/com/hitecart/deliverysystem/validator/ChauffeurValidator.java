package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.AdresseDto;
import com.hitecart.deliverysystem.dto.ChauffeurDto;
import com.hitecart.deliverysystem.dto.ContratDto;
import com.hitecart.deliverysystem.dto.MissionDto;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurValidator {
    public static List<String> validate(ChauffeurDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
            errors.add("Veuillez renseigner le nom du chauffeur !");
            errors.add("Veuillez renseigner le prenom du chauffeur !");
            errors.add("Veuillez renseigner le numreo du telephone  du chauffeur !");
            errors.add("Veuillez renseigner l'adresse email du chauffeur !");
            errors.add("Veuillez renseigner le mot de passe du chauffeur !");
            errors.add("Veuillez renseigner le numero de securité sociale du chauffeur !");
            errors.add("Veuillez renseigner le RIB du chauffeur !");
            errors.add("Veuillez renseigner le Type de permis du chauffeur !");
            errors.add("Veuillez renseigner la date de naissance du chauffeur !");
            errors.add("Veuillez renseigner le numero de permis de conduire du chauffeur !");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }
       if(!StringUtils.hasLength(dto.getDrivinglicenseNbr())){
           errors.add("Veuillez renseigner le numero de permis de conduire du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getLastname())){
           errors.add("Veuillez renseigner le nom du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getFirstname())){
           errors.add("Veuillez renseigner le prenom du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getPhoneNum())){
           errors.add("Veuillez renseigner le numreo du telephone  du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getEmail())){
           errors.add("Veuillez renseigner l'adresse email du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getPassword())){
           errors.add("Veuillez renseigner le mot de passe du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getNumSecu())){
           errors.add("Veuillez renseigner le numero de securité sociale du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getRib())){
           errors.add("Veuillez renseigner le RIB du chauffeur !");
       }
        if(dto.getDrivingLicenseType()==null){
            errors.add("Veuillez renseigner le Type de permis du chauffeur !");
        }
        if(!StringUtils.hasLength(dto.getDrivinglicenseNbr())){
            errors.add("Veuillez renseigner le numero de permis du chauffeur !");
        }
        if(dto.getDateOfBirth() == null){
            errors.add("Veuillez renseigner la date de naissance du chauffeur !");
        }

        //errors.addAll(AdresseValidator.validate(dto.getAdresseDto()));
    return errors;
    }
}
