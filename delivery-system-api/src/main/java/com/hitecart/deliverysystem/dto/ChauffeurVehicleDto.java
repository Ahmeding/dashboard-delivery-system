package com.hitecart.deliverysystem.dto;


import com.hitecart.deliverysystem.model.Chauffeur;
import com.hitecart.deliverysystem.model.ChauffeurVehicle;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChauffeurVehicleDto {
    private Integer id;
    private Integer chauffeurId;
    private ChauffeurDto chauffeurDto;
    private Integer missionVehicleId;
    private LocalDateTime assignedAt;

    public static ChauffeurVehicleDto fromEntity(ChauffeurVehicle chauffeurVehicle) {
        if(chauffeurVehicle == null){
            return null;
        }

        return ChauffeurVehicleDto.builder()
                .id(chauffeurVehicle.getId())
                .chauffeurId(chauffeurVehicle.getChauffeur()!=null?chauffeurVehicle.getChauffeur().getId():null)
                .chauffeurDto(chauffeurVehicle.getChauffeur()!=null?
                                ChauffeurDto.fromEntity(chauffeurVehicle.getChauffeur())
                                :null)
                .missionVehicleId(chauffeurVehicle.getMissionVehicle()!= null?chauffeurVehicle.getMissionVehicle().getId() : null)
                .assignedAt(chauffeurVehicle.getAssignedAt())
                .build();
    }

    public static ChauffeurVehicle toEntity(ChauffeurVehicleDto dto) {
        if(dto == null){
            return null;
        }

         ChauffeurVehicle chauffeurVehicle = new ChauffeurVehicle();
                 chauffeurVehicle.setId(dto.getId());
                 chauffeurVehicle.setChauffeur(ChauffeurDto.toEntity(dto.getChauffeurDto()));
                chauffeurVehicle.setAssignedAt(chauffeurVehicle.getAssignedAt());
                return chauffeurVehicle;
    }
}
