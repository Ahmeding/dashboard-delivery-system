package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.ChauffeurVehicleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IChauffeurVehicleService {

    ChauffeurVehicleDto findByChauffeurId(Integer chauffeurId);
    List<ChauffeurVehicleDto> findByMissionVehicleId(Integer missionVehicleId);
    void deleteByChauffeurId(Integer chauffeurId);
    void deleteByMissionVehicleId(Integer missionVehicleId);
    List<Integer> findUnavailableDrivers(List<Integer> chauffeurIds,
                                         LocalDateTime startDateTime,
                                         LocalDateTime endDateTime);
}
