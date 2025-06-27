package com.hitecart.deliverysystem.controllers.mission;

import com.hitecart.deliverysystem.controllers.api.MissionAssignmentApi;
import com.hitecart.deliverysystem.dto.mission.MissionAssignmentRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;
import com.hitecart.deliverysystem.services.AutoGenerateMissionAssigmentService;
import com.hitecart.deliverysystem.services.AutoGeneratingWeeklyPlanningService;
import com.hitecart.deliverysystem.services.MissionAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionAssignmentController implements MissionAssignmentApi {
    private final MissionAssignmentService missionAssignmentService;
    private final AutoGeneratingWeeklyPlanningService autoGeneratingWeeklyPlanningService;
    private final AutoGenerateMissionAssigmentService autoGenerateMissionAssigmentService;


    @Override
    public ResponseEntity<Integer> checkOverlappingVehicles(MissionVehicleRequest missionVehicleRequest) {
        return ResponseEntity.ok(missionAssignmentService.checkOverlappingVehicles(missionVehicleRequest));
    }

    @Override
    public ResponseEntity<?> checkOverlappingDrivers(MissionVehicleRequest missionVehicleRequest) {

        return ResponseEntity.ok( missionAssignmentService.checkOverlappingDrivers(missionVehicleRequest));

    }

    @Override
    public ResponseEntity<MissionResponse> managingVehicleDriverAssigment(Integer missionId, MissionAssignmentRequest missionAssignmentRequest) {
        return ResponseEntity.ok( missionAssignmentService.managingVehicleDriverAssigment(missionId,missionAssignmentRequest));
    }

    @Override
    public ResponseEntity<List<Integer>> generateMissions() {
        return ResponseEntity.ok( autoGeneratingWeeklyPlanningService.generateMissions());
    }

    @Override
    public void generateWeeklyAssignments() {
        autoGenerateMissionAssigmentService.generateWeeklyAssignments();
    }

}
