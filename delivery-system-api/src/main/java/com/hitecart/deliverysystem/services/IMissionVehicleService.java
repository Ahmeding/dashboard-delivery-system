package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.MissionVehicleDto;
import com.hitecart.deliverysystem.model.MissionVehicle;

import java.util.List;

public interface IMissionVehicleService {
    List<MissionVehicleDto> findByVehicleId(Integer vehicleId);
    List<MissionVehicleDto> findByMissionId(Integer missionId);
}
