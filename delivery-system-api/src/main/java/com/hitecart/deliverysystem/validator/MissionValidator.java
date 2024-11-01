package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.MissionDto;
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
       if(dto.getVolume()==null){
           errors.add("Veuillez renseigner le volume demmandé pour cette mission !");
       }
        if(dto.getMissionVehiclesDtos() == null){
            errors.add("Veuillez renseigner le vehicule adequat pour cette mission !");
        }
    return errors;
    }
}
