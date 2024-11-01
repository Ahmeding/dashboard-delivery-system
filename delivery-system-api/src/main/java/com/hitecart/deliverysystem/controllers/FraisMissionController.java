package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.FraisMissionApi;
import com.hitecart.deliverysystem.dto.FraisMissionDto;
import com.hitecart.deliverysystem.dto.LigneFraisMissionDto;
import com.hitecart.deliverysystem.services.FraisMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
@RestController
public class FraisMissionController implements FraisMissionApi {

    private FraisMissionService fraisMissionService;

    @Autowired
    public FraisMissionController(FraisMissionService fraisMissionService) {
        this.fraisMissionService = fraisMissionService;
    }

    @Override
    public ResponseEntity<FraisMissionDto> save(FraisMissionDto dto) {
        return ResponseEntity.ok( fraisMissionService.save(dto));
    }

    @Override
    public ResponseEntity<FraisMissionDto> updateFraisMission(FraisMissionDto dto) {
        return ResponseEntity.ok(fraisMissionService.updateFraisMission(dto));
    }

    @Override
    public ResponseEntity<FraisMissionDto> updateQuantite(Integer idMF, Integer idLigneFM, BigDecimal qte) {
        return ResponseEntity.ok(fraisMissionService.updateQuantite(idMF,idLigneFM,qte));
    }

    @Override
    public ResponseEntity<FraisMissionDto> deleteLigneFraisMission(Integer idFraisMission, Integer idLigneFraisMission) {
        return ResponseEntity.ok(fraisMissionService.deleteLigneFraisMission(idFraisMission,idLigneFraisMission));
    }

    @Override
    public ResponseEntity<FraisMissionDto> findById(Integer id) {
        return ResponseEntity.ok(fraisMissionService.findById(id));
    }

    @Override
    public ResponseEntity<FraisMissionDto> findByIdMission(Integer idMission) {
        return ResponseEntity.ok(fraisMissionService.findByIdMission(idMission));
    }

    @Override
    public ResponseEntity<List<LigneFraisMissionDto>> findAllLignesFraisMissionByFraisMissionId(Integer idFraisMission) {
        return ResponseEntity.ok(fraisMissionService.findAllLignesFraisMissionByFraisMissionId(idFraisMission));
    }

    @Override
    public ResponseEntity<List<FraisMissionDto>> findAll() {
        return ResponseEntity.ok(fraisMissionService.findAll());
    }

    @Override
    public void delete(Integer id) {
            fraisMissionService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<FraisMissionDto>> findAllFraisMissions(int page, int size) {
        return ResponseEntity.ok(fraisMissionService.findAllFraisMission(page,size));
    }
}
