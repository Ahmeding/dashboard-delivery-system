package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.mission.MissionRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.mission.MissionStartEndRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import com.hitecart.deliverysystem.model.enums.KmFlag;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.List;

public interface MissionService {
    MissionResponse save(MissionRequest dto);
    MissionResponse updateMissionInfo(Integer missionId,MissionRequest dto);
    MissionResponse updateExpensesMission(Integer idMission, List<MissionExpenseRequest> expenses) ;
    MissionResponse updateMissionStopPoints(Integer idMission, List<StopPointRequest> stopPointRequests) ;
    MissionResponse findById(Integer id);
    List<MissionResponse> findAllByDate(Instant dateMission);
    List<MissionResponse> findAll();
    void delete(Integer id);
    PageResponse<MissionResponse> findAllMissions(int page, int size);
    PageResponse<MissionResponse> findAllMissionsByDriverId(int page, int size, Authentication connectedUser);
    List<MissionResponse> findAllMissionsByDriver(Authentication connectedUser);
}
