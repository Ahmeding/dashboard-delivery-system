package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.ContratDto;
import com.hitecart.deliverysystem.model.enums.TypeContrat;

import java.util.ArrayList;
import java.util.List;

public class ContratValidator {
    public static List<String> validate(ContratDto dto){
        List<String> errors= new ArrayList<>();
        if(dto==null){
           errors.add("Veuillez renseigner la date de debut du contrat !");
           errors.add("Veuillez renseigner le type du contrat !");
           errors.add("Veuillez renseigner la date de fin du contrat !");
            return errors;
        }
       if(dto.getChauffeurDto()==null){
           errors.add("Veuillez renseigner le chauffeur concerné !");
       }
       if(dto.getTypeContrat()==null){
           errors.add("Veuillez renseigner le type du contrat !");
       }
       if(dto.getDateDebut()==null){
           errors.add("Veuillez renseigner la date de debut du contrat !");
       }
       if(dto.getTypeContrat().equals(TypeContrat.CDD) && dto.getDateFin()==null){
           errors.add("Veuillez renseigner la date de fin du contrat !");
       }
    return errors;
    }
}
