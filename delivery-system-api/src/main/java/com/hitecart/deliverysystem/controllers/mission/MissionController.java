package com.hitecart.deliverysystem.controllers.mission;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.MissionApi;
import com.hitecart.deliverysystem.dto.mission.MissionAssignmentRequest;
import com.hitecart.deliverysystem.dto.mission.MissionRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.mission.MissionStartEndRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.model.enums.KmFlag;
import com.hitecart.deliverysystem.services.*;
import com.hitecart.deliverysystem.services.impl.MissionServiceImpl;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MissionController implements MissionApi {

    private final MissionService missionService;

    @Override
    public ResponseEntity<MissionResponse> saveMission(MissionRequest missionRequest) {

        return ResponseEntity.ok(missionService.save(missionRequest));
    }

    @Override
    public ResponseEntity<MissionResponse> updateMissionInfo(Integer missionId,MissionRequest missionRequest) {
        return ResponseEntity.ok(missionService.updateMissionInfo(missionId,missionRequest));
    }

    @Override
    public ResponseEntity<MissionResponse> updateExpensesMission(Integer idMission, List<MissionExpenseRequest> missionExpenseRequests) {
        return ResponseEntity.ok(missionService.updateExpensesMission(idMission, missionExpenseRequests));
    }

    @Override
    public ResponseEntity<MissionResponse> updateStopPointMission(Integer idMission, List<StopPointRequest> stopPointRequests) {
        return ResponseEntity.ok(missionService.updateMissionStopPoints(idMission, stopPointRequests));
    }

    @Override
    public ResponseEntity<MissionResponse> findMissionById(Integer id) {

        return ResponseEntity.ok(missionService.findById(id));
    }

    @Override
    public ResponseEntity<List<MissionResponse>> findAllMissionByDate(Instant dateMission) {

        return ResponseEntity.ok(missionService.findAllByDate(dateMission));
    }

    @Override
    public ResponseEntity<List<MissionResponse>> findAllMission() {

        return ResponseEntity.ok(missionService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteMission(Integer id) {
        try {
            missionService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Mission deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Mission not found with ID: " + id
                    ));
        }
    }


    @Override
    public ResponseEntity<PageResponse<MissionResponse>> findAllMissionsPageable(int page, int size) {
        return ResponseEntity.ok(missionService.findAllMissions(page,size));
    }


    @Override
    public ResponseEntity<PageResponse<MissionResponse>> findAllMissionsByDriverIdPageable(int page, int size, Authentication connectedUser) {
        return ResponseEntity.ok(missionService.findAllMissionsByDriverId(page,size,connectedUser));
    }

    @Override
    public ResponseEntity<List<MissionResponse>> findAllMissionsByConnectedDriver(Authentication connectedUser) {
        return ResponseEntity.ok(missionService.findAllMissionsByDriver(connectedUser));
    }


}
