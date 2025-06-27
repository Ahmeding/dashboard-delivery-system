package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointResponse;

import java.util.List;

public interface StopPointService {
    StopPointResponse save(StopPointRequest request);
    StopPointResponse updateStopPointInfo(Integer stopPointId ,StopPointRequest request);
    StopPointResponse findById(Integer id);
    List<StopPointResponse> findByMissionId(Integer idMission);
    List<StopPointResponse> findAll();
    void delete(Integer id);
}
