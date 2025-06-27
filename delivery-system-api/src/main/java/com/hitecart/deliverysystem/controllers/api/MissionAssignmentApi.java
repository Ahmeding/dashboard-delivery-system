package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.mission.MissionAssignmentRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;
import com.hitecart.deliverysystem.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;
import static com.hitecart.deliverysystem.utils.Constants.CHECK_OVERLAPPING_DRIVERS_ENDPOINT;

@RequestMapping(Constants.MISSION_ENDPOINT)
@Tag(name = "Missions Assignment")
public interface MissionAssignmentApi {

    @GetMapping(value = GENER_MISSION_ENDPOINT)
    @Operation(operationId = "generateMissions")
    ResponseEntity<List<Integer>>  generateMissions();

    @Operation(operationId = "generateWeeklyAssignments")
    @GetMapping(value = GENER_ASSIGN_ENDPOINT)
    void generateWeeklyAssignments();

    @PostMapping(value =CHECK_OVERLAPPING_VEHICLES_ENDPOINT )
    @Operation(operationId = "checkOverlappingVehicles")
    ResponseEntity<Integer> checkOverlappingVehicles(@RequestBody MissionVehicleRequest missionVehicleRequest);

    @PostMapping(value =CHECK_OVERLAPPING_DRIVERS_ENDPOINT )
    @Operation(operationId = "checkOverlappingDrivers")
    ResponseEntity<?> checkOverlappingDrivers(@RequestBody MissionVehicleRequest missionVehicleRequest);

    @PostMapping(value =MANAGING_VEHICLES_DRIVERS_ENDPOINT )
    @Operation(operationId = "managingVehicleDriverAssigment")
    ResponseEntity<MissionResponse> managingVehicleDriverAssigment(
            @PathVariable("missionId") Integer missionId,
            @RequestBody MissionAssignmentRequest missionAssignmentRequest);






}
