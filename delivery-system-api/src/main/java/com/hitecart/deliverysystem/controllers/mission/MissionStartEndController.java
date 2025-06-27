package com.hitecart.deliverysystem.controllers.mission;

import com.hitecart.deliverysystem.controllers.api.MissionStartEndApi;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.mission.MissionStartEndRequest;
import com.hitecart.deliverysystem.dto.mission.MissionStatusUpdateRequest;
import com.hitecart.deliverysystem.model.enums.KmFlag;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import com.hitecart.deliverysystem.services.MissionService;
import com.hitecart.deliverysystem.services.MissionStartEndService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MissionStartEndController implements MissionStartEndApi {

    private final MissionStartEndService missionStartEndService;


    @Override
    public ResponseEntity<MissionResponse> updateMissionStatus(Integer missionId, MissionStatusUpdateRequest missionStatusUpdateRequest) {

        return ResponseEntity.ok(missionStartEndService.updateMissionStatus( missionId, missionStatusUpdateRequest));
    }

    @Override
    public ResponseEntity<MissionResponse> updateMissionMileage(Integer missionId, MissionStartEndRequest missionStartEndRequest) {
        return ResponseEntity.ok(missionStartEndService.updateMileage( missionId, missionStartEndRequest));
    }

}
