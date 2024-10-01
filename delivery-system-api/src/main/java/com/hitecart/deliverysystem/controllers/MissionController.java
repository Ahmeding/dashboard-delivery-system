package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.MissionApi;
import com.hitecart.deliverysystem.dto.MissionDto;
import com.hitecart.deliverysystem.dto.MissionToSaveRequest;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import com.hitecart.deliverysystem.services.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController implements MissionApi {

    private final MissionService missionService;


    @Override
    public ResponseEntity<MissionDto> save(MissionDto dto) {

        return ResponseEntity.ok(missionService.save(dto));
    }

    @Override
    public ResponseEntity<MissionDto> updateEtatMission(Integer idMission, EtatMission etatMission) {

        return ResponseEntity.ok(missionService.updateEtatMission(idMission,etatMission));
    }

    @Override
    public ResponseEntity<MissionDto> updateKilometrage(String flag, Integer idMission, double kilometrage) {
        return ResponseEntity.ok(missionService.updateKilometrage(flag,idMission,kilometrage));
    }

    @Override
    public ResponseEntity<MissionDto> findById(Integer id) {

        return ResponseEntity.ok(missionService.findById(id));
    }

    @Override
    public ResponseEntity<List<MissionDto>> findAllByDate(Instant dateMission) {

        return ResponseEntity.ok(missionService.findAllByDate(dateMission));
    }

    @Override
    public ResponseEntity<List<MissionDto>> findAll() {

        return ResponseEntity.ok(missionService.findAll());
    }

    @Override
    public void delete(Integer id) {
        missionService.delete(id);
    }
}
