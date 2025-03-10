package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.DayPlanDto;
import com.hitecart.deliverysystem.dto.WeeklyPlanningDto;

import java.util.ArrayList;
import java.util.List;

public class WeeklyPlanningValidator {


    public static List<String> validate(WeeklyPlanningDto dto){
        List<String> errors= new ArrayList<>();
        if(dto==null){
            errors.add("Veuillez renseigner le titre du weekly planning !");
            errors.add("Veuillez renseigner la date de debut!");
            errors.add("Veuillez renseigner la date de fin!");
            return errors;
        }
        if(dto.getTitle()==null){
            errors.add("Veuillez renseigner le Titre du jour !");
        }
        if(dto.getWeekStartDate()==null){
            errors.add("Veuillez renseigner la date de debut !");
        }
        if(dto.getWeekEndDate()==null){
            errors.add("Veuillez renseigner la date de fin !");
        }

        return errors;
    }
}
