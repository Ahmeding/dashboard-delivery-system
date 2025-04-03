package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.EtapeDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EtapeValidator {
    public static List<String> validate(EtapeDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner le lieu de depart  de l' etape !");
           errors.add("Veuillez renseigner la date d'arrivee de l' etape !");
           errors.add("Veuillez renseigner la date de deepart de l' etape !");
           errors.add("Veuillez renseigner l'heure de la livraison prevu de l' etape !");
            return errors;
        }
       if(!StringUtils.hasLength(dto.getTitre())){
           errors.add("Veuillez renseigner le titre de l'etape !");
       }
       if(!StringUtils.hasLength(dto.getLieuArrivee())){
           errors.add("Veuillez renseigner le lieu d'arriver de l'etape !");
       }
       if(!StringUtils.hasLength(dto.getLieuDepart())){
           errors.add("Veuillez renseigner le lieu de depart  de l' etape !");
       }
       if(dto.getDateArrivee()==null){
           errors.add("Veuillez renseigner la date d'arrivee de l' etape !");
       }
       if(dto.getDateDepart()==null){
           errors.add("Veuillez renseigner la date de depart de l' etape !");
       }
       if(dto.getScheduledDeliveryTime()==null){
           errors.add("Veuillez renseigner l'heure de la livraison prevu de l' etape !");
       }
    return errors;
    }
}
