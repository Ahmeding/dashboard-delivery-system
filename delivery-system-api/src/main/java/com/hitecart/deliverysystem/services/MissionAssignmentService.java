package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.mission.MissionAssignmentRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;

import java.util.List;

public interface MissionAssignmentService {
    List<Integer> checkOverlappingDrivers(MissionVehicleRequest missionVehicleRequest);
    Integer checkOverlappingVehicles(MissionVehicleRequest missionVehicleRequest);
    MissionResponse managingVehicleDriverAssigment(Integer id, MissionAssignmentRequest missionAssignmentRequest);
}
