package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.MissionVehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
public class MissionVehicleDto {


    //private MissionDto missionDto;
    private Integer id;
    private Integer missionId;
    private VehicleDto vehicleDto;
    private List<ChauffeurVehicleDto> chauffeursVehicleDto= new ArrayList<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime assignedAt;

    public static MissionVehicleDto fromEntity(MissionVehicle missionVehicule) {
        if(missionVehicule==null) {
            return null;
        }
        return MissionVehicleDto.builder()
                .id(missionVehicule.getId())
                .vehicleDto(VehicleDto.fromEntity(missionVehicule.getVehicule()!=null?missionVehicule.getVehicule():null))
                .missionId(missionVehicule.getMission()!=null ? missionVehicule.getMission().getId():null)
                .chauffeursVehicleDto(missionVehicule.getChauffeurVehiculeList()!= null?
                        missionVehicule.getChauffeurVehiculeList().stream()
                                .map(ChauffeurVehicleDto::fromEntity)
                                .collect(Collectors.toList()):null)
                .assignedAt(missionVehicule.getAssignedAt())
                .build();
    }


    public static MissionVehicle toEntity(MissionVehicleDto dto){
        if(dto==null){
            return null;
        }
        MissionVehicle missionVehicule = new MissionVehicle();
        missionVehicule.setId(dto.getId());
               missionVehicule.setVehicule(VehicleDto.toEntity(dto.getVehicleDto()));
               missionVehicule.setMission(Mission.builder().id(dto.getMissionId()).build());
                missionVehicule.setChauffeurVehiculeList(
                        dto.chauffeursVehicleDto!=null?
                        dto.chauffeursVehicleDto.stream()
                                .map(ChauffeurVehicleDto::toEntity)
                                .collect(Collectors.toList()):null
                                );
               missionVehicule.setAssignedAt(dto.getAssignedAt());
               return missionVehicule;
    }
}
