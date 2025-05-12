package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.FraisMissionDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FraisMissionValidator {
    public static List<String> validate(FraisMissionDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
           errors.add("Veuillez renseigner le Titre du fraisMission !");
           errors.add("Veuillez saisir au moin un frais de mission!");
           return errors;
        }
       if(!StringUtils.hasLength(dto.getName())){
           errors.add("Veuillez renseigner le nom du fraisMission !");
       }
//       if(dto.getType()==null){
//           errors.add("Veuillez renseigner le type  du fraisMission !");
//       }
//       if(dto.getQuantite()==null){
//           errors.add("Veuillez renseigner la quatite du fraisMission !");
//       }

    return errors;
    }
}
