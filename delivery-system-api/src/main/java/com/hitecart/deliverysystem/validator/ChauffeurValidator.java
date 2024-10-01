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
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }
       if(!StringUtils.hasLength(dto.getNom())){
           errors.add("Veuillez renseigner le nom du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getPrenom())){
           errors.add("Veuillez renseigner le prenom du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getNumTel())){
           errors.add("Veuillez renseigner le numreo du telephone  du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getAdresseEmail())){
           errors.add("Veuillez renseigner l'adresse email du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getMotDePasse())){
           errors.add("Veuillez renseigner le mot de passe du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getNumSecu())){
           errors.add("Veuillez renseigner le numero de securité sociale du chauffeur !");
       }
       if(!StringUtils.hasLength(dto.getRib())){
           errors.add("Veuillez renseigner le RIB du chauffeur !");
       }
        if(!StringUtils.hasLength(dto.getTypePermi())){
            errors.add("Veuillez renseigner le Type de permis du chauffeur !");
        }
        if(dto.getDateNaissance() == null){
            errors.add("Veuillez renseigner la date de naissance du chauffeur !");
        }

        //errors.addAll(AdresseValidator.validate(dto.getAdresseDto()));
    return errors;
    }
}
