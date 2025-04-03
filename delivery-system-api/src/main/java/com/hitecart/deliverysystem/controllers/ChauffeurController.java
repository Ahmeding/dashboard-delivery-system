package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.ChauffeurApi;
import com.hitecart.deliverysystem.dto.basic.ChauffeurDto;
import com.hitecart.deliverysystem.services.ChauffeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChauffeurController implements ChauffeurApi {

    private ChauffeurService chauffeurService;

    @Autowired
    public ChauffeurController(ChauffeurService chauffeurService) {
        this.chauffeurService = chauffeurService;
    }

    @Override
    public ResponseEntity<ChauffeurDto> save(ChauffeurDto dto) {
        return ResponseEntity.ok(chauffeurService.save(dto));
    }

    @Override
    public ResponseEntity<ChauffeurDto> findById(Integer id) {
        return ResponseEntity.ok(chauffeurService.findById(id));
    }

    @Override
    public ResponseEntity<ChauffeurDto> findByEmail(String email) {
        return ResponseEntity.ok(chauffeurService.findByEmail(email));
    }

    @Override
    public ResponseEntity<List<ChauffeurDto>> findAllIsActive(Boolean isActive) {
        return ResponseEntity.ok(chauffeurService.findAllIsActive(isActive));
    }


    @Override
    public ResponseEntity<List<ChauffeurDto>> findAll() {
        return ResponseEntity.ok(chauffeurService.findAll());
    }

    @Override
    public ResponseEntity<ChauffeurDto> updateIsActive(Integer id, Boolean isActive) {
        return ResponseEntity.ok(chauffeurService.updateIsActive(id,isActive));
    }

    @Override
    public ResponseEntity<PageResponse<ChauffeurDto>> findAllDrivers(int page, int size) {
        return ResponseEntity.ok(chauffeurService.findAllDrivers(page, size));
    }

    @Override
    public void delete(Integer id) {
            chauffeurService.delete(id);
    }
}
