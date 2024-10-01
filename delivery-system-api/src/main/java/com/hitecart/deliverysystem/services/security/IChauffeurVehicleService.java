package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.dto.ChauffeurVehicleDto;

import java.util.List;

public interface IChauffeurVehicleService {

    ChauffeurVehicleDto findByChauffeurId(Integer chauffeurId);
    List<ChauffeurVehicleDto> findByMissionVehicleId(Integer missionVehicleId);
    void deleteByChauffeurId(Integer chauffeurId);
    void deleteByMissionVehicleId(Integer missionVehicleId);

}
