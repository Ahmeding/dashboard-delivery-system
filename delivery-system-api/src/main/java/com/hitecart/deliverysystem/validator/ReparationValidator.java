package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.ReparationDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReparationValidator {
    public static List<String> validate(ReparationDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){

           errors.add("Veuillez renseigner le cout de la reparation !");
           errors.add("Veuillez renseigner le type de la reparation !");
           errors.add("Veuillez renseigner le vehicule à reparer !");
           errors.add("Veuillez renseigner la date de debut de la reparation !");
           errors.add("Veuillez renseigner la date de fin de la reparation !");

          return errors;
        }
       if(dto.getCout()==null){
           errors.add("Veuillez renseigner le cout de la reparation !");
       }
       if(dto.getType()==null){
           errors.add("Veuillez renseigner le type de la reparation !");
       }
       if(dto.getVehiculeDto()==null){
           errors.add("Veuillez renseigner le vehicule à reparer !");
       }
       if(dto.getDateDebut()==null){
           errors.add("Veuillez renseigner la date de debut de la reparation !");
       }
       if(dto.getDateFin()==null){
           errors.add("Veuillez renseigner la date de fin de la reparation !");
       }
    return errors;
    }
}
