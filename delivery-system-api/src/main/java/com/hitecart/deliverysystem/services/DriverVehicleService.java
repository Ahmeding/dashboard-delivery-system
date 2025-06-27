package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface DriverVehicleService {

    DriverVehicleResponse findByChauffeurId(Integer chauffeurId);
    List<DriverVehicleResponse> findByMissionVehicleId(Integer missionVehicleId);
    void deleteByChauffeurId(Integer chauffeurId);
    void deleteByMissionVehicleId(Integer missionVehicleId);
    List<Integer> findUnavailableDrivers(List<Integer> chauffeurIds,
                                         LocalDateTime startDateTime,
                                         LocalDateTime endDateTime);
}
