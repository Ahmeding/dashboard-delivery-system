package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.PieceRechangeDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PieceRechangeValidator {
    public static List<String> validate(PieceRechangeDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner le nom de la piece de rechange !");
           errors.add("Veuillez renseigner la reference  de la piece de rechange !");
           errors.add("Veuillez renseigner le prix de la piece de rechange !");
            return errors;
        }
       if(!StringUtils.hasLength(dto.getNom())){
           errors.add("Veuillez renseigner le nom de la piece de rechange !");
       }
       if(!StringUtils.hasLength(dto.getReference())){
           errors.add("Veuillez renseigner la reference  de la piece de rechange !");
       }
       if(dto.getPrix()==null){
           errors.add("Veuillez renseigner le prix de la piece de rechange !");
       }
    return errors;
    }
}
