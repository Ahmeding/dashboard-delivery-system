package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.VehicleDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VehiculeValidator {
    public static List<String> validate(VehicleDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner l'immatriculation du vehicule !");
           errors.add("Veuillez renseigner la marque du vehicule !");
           errors.add("Veuillez renseigner le modele du vehicule !");
           errors.add("Veuillez renseigner le numero VIN du vehicule !");
           errors.add("Veuillez renseigner le date du premier mise en circulation du vehicule !");
           errors.add("Veuillez renseigner le kilometrage du vehicule !");
           errors.add("Veuillez renseigner le type du carburant du vehicule !");
           errors.add("Veuillez renseigner le volume du vehicule !");
        return errors;
        }
       if(!StringUtils.hasLength(dto.getImmatriculation())){
           errors.add("Veuillez renseigner l'immatriculation du vehicule !");
       }
       if(!StringUtils.hasLength(dto.getMarque())){
           errors.add("Veuillez renseigner la marque du vehicule !");
       }
       if(!StringUtils.hasLength(dto.getModele())){
           errors.add("Veuillez renseigner le modele du vehicule !");
       }
       if(!StringUtils.hasLength(dto.getNumVin())){
           errors.add("Veuillez renseigner le numero VIN du vehicule !");
       }
       if(dto.getDatePMC()==null){
           errors.add("Veuillez renseigner le date du premier mise en circulation du vehicule !");
       }
       if(dto.getKilometrage()==null){
           errors.add("Veuillez renseigner le kilometrage du vehicule !");
       }
       if(dto.getTypecarburant()==null){
           errors.add("Veuillez renseigner le type du carburant du vehicule !");
       }
        if(dto.getVolume()==null){
            errors.add("Veuillez renseigner le volume du vehicule !");
        }

    return errors;
    }
}
