package com.hitecart.deliverysystem.dto.basic;

import com.hitecart.deliverysystem.model.MissionVehicle;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
public class MissionVehicleDto {


    //private MissionDto missionDto;
    private Integer id;
    private Integer missionId;
    private VehiculeDto vehicleDto;
    private List<ChauffeurVehicleDto> chauffeursVehicleDto;
    private LocalDateTime assignedAt;

    public static MissionVehicleDto fromEntity(MissionVehicle missionVehicule) {
        if(missionVehicule==null) {
            return null;
        }
        return MissionVehicleDto.builder()
                .id(missionVehicule.getId())
                .vehicleDto(VehiculeDto.fromEntity(missionVehicule.getVehicule()))
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
               missionVehicule.setVehicule(VehiculeDto.toEntity(dto.getVehicleDto()));
               //missionVehicule.setMission(dto.getMissionId());
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
