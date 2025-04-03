package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.basic.DayPlanDto;

import java.util.ArrayList;
import java.util.List;

public class DayPlanValidator {

    public static List<String> validate(DayPlanDto dto){
        List<String> errors= new ArrayList<>();
        if(dto==null){
            errors.add("Veuillez renseigner le nom du jour  !");
            errors.add("Veuillez renseigner la date !");
            return errors;
        }
        if(dto.getName()==null){
            errors.add("Veuillez renseigner le nom du jour !");
        }
        if(dto.getDate()==null){
            errors.add("Veuillez renseigner la date !");
        }

        return errors;
    }
}
