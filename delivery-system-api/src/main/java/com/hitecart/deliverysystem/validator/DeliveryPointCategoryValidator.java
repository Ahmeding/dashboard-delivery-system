package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.DeliveryPointCategoryDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPointCategoryValidator {
    public static List<String> validate(DeliveryPointCategoryDto dto){
        List<String> errors= new ArrayList<>();

        if(dto==null){
            errors.add("Veuillez renseigner le nom de la categorie !");
            errors.add("Veuillez renseigner La valeur des frais supplémentaires !");
        }
        if(dto.getName() == null || !StringUtils.hasLength(dto.getName())){
            errors.add("Veuillez renseigner le nom de la categorie !");
        }
        if(dto.getAdditionalFees()==null){
            errors.add("Veuillez renseigner La valeur des frais supplémentaires !");
        }
        return errors;
    }
}
