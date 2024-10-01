package com.hitecart.deliverysystem.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class MissionToSaveRequest {
    MissionDto missionDto;
    List<ChauffeurDto> chauffeurDtos;
    List<VehiculeDto> vehiculeDtos;
}
