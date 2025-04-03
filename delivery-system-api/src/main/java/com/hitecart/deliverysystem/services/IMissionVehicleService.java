package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.basic.MissionVehicleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IMissionVehicleService {
    List<MissionVehicleDto> findByVehicleId(Integer vehicleId);
    List<MissionVehicleDto> findByMissionId(Integer missionId);
    List<Integer> findUnavailableVehicles(List<Integer> vehicleIds,
                                                    LocalDateTime startDateTime,
                                                    LocalDateTime endDateTime);
}
