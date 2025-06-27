package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface IMissionVehicleService {
    List<MissionVehicleResponse> findByVehicleId(Integer vehicleId);
    List<MissionVehicleResponse> findByMissionId(Integer missionId);
    Integer findUnavailableVehicles(Integer vehicleId,
                                                    LocalDateTime startDateTime,
                                                    LocalDateTime endDateTime);
}
