package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.MissionDto;
import com.hitecart.deliverysystem.dto.MissionToSaveRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MissionValidator {
    public static List<String> validate(MissionDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){

           errors.add("Veuillez renseigner le titre de la mission !");
           errors.add("Veuillez renseigner la date de debut de la mission !");
           errors.add("Veuillez renseigner le volume demmandé pour cette mission !");
           errors.add("Veuillez renseigner le Prix Ht de la mission !");
           errors.add("Veuillez renseigner le Tva de la mission !");
           errors.add("Veuillez renseigner le Prix TTC de la mission !");
           errors.add("Veuillez renseigner les frais du chauffeur de la mission !");
           errors.add("Veuillez renseigner le(s) etape(s) pour cette mission !");
           errors.add("Veuillez renseigner un chauffeur pour cette mission !");
           errors.add("Veuillez renseigner le vehicule adequat pour cette mission !");
           return errors;
        }
       if(!StringUtils.hasLength(dto.getTitre())){
           errors.add("Veuillez renseigner le titre de la mission !");
       }
       if(dto.getDateDebut()==null){
           errors.add("Veuillez renseigner la date de debut de la mission !");
       }
       if(dto.getFraisChauffeur()==null){
           errors.add("Veuillez renseigner les frais du chauffeur de la mission !");
       }
       if(dto.getVolume()==null){
           errors.add("Veuillez renseigner le volume demmandé pour cette mission !");
       }
       if(dto.getPrixHt()==null){
           errors.add("Veuillez renseigner le Prix Ht de la mission !");
       }
        if(dto.getTva()==null){
            errors.add("Veuillez renseigner le Tva de la mission !");
        }
        if(dto.getPrixTTC()==null){
            errors.add("Veuillez renseigner le Prix TTC de la mission !");
        }
        if(dto.getMissionVehiclesDtos() == null){
            errors.add("Veuillez renseigner le vehicule adequat pour cette mission !");
        }
/*        if(dto.getMissionChauffeurDtos() == null){
            errors.add("Veuillez renseigner un chauffeur pour cette mission !");
        }*/
//        if(dto.getEtapeDtos() == null){
//            errors.add("Veuillez renseigner le(s) etape(s) pour cette mission !");
//        }
    return errors;
    }
}
