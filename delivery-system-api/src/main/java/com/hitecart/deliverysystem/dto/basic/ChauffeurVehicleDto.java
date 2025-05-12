package com.hitecart.deliverysystem.dto.basic;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.ChauffeurVehicle;
import com.hitecart.deliverysystem.model.MissionVehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChauffeurVehicleDto {
    private Integer id;
    private Integer chauffeurId;
    private ChauffeurDto chauffeurDto;
    private Integer missionVehicleId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
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
                 chauffeurVehicle.setMissionVehicle(MissionVehicle.builder().id(dto.missionVehicleId).build());
                 chauffeurVehicle.setChauffeur(ChauffeurDto.toEntity(dto.getChauffeurDto()));
                chauffeurVehicle.setAssignedAt(chauffeurVehicle.getAssignedAt());
                return chauffeurVehicle;
    }
}
