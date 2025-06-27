package com.hitecart.deliverysystem.controllers.mission;

import com.hitecart.deliverysystem.controllers.api.StopPointApi;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointResponse;
import com.hitecart.deliverysystem.services.StopPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StopPointController implements StopPointApi {

    private final StopPointService stopPointService;

    @Override
    public ResponseEntity<StopPointResponse> saveStopPoint(StopPointRequest stopPointRequest) {
        return ResponseEntity.ok(stopPointService.save(stopPointRequest));
    }

    @Override
    public ResponseEntity<StopPointResponse> updateStopPointInfo(Integer stopPointId, StopPointRequest request) {
        return ResponseEntity.ok(stopPointService.updateStopPointInfo(stopPointId,request));
    }

    @Override
    public ResponseEntity<StopPointResponse> findStopPointById(Integer id) {
        return ResponseEntity.ok(stopPointService.findById(id));
    }

    @Override
    public ResponseEntity<List<StopPointResponse>> findStopPointByMissionId(Integer missionId) {
        return ResponseEntity.ok(stopPointService.findByMissionId(missionId));
    }

    @Override
    public ResponseEntity<List<StopPointResponse>> findAllStopPoint() {
        return ResponseEntity.ok(stopPointService.findAll());
    }

    @Override
    public ResponseEntity<?> deleteStopPoint(Integer id) {
        try {
            stopPointService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Stop point deleted successfully"
                    ));
        }
        catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Stop point not found with ID: " + id
                    ));
        }
    }
}
