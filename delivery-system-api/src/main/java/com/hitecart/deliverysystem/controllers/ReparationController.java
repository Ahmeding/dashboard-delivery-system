package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.ReparationApi;
import com.hitecart.deliverysystem.dto.ReparationDto;
import com.hitecart.deliverysystem.services.ReparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;


@RestController
public class ReparationController implements ReparationApi {

    private final ReparationService reparationService;

    @Autowired
    public ReparationController(ReparationService reparationService) {
        this.reparationService = reparationService;
    }

    @Override
    public ResponseEntity<ReparationDto> save(ReparationDto dto) {
        return ResponseEntity.ok(reparationService.save(dto));
    }

    @Override
    public ResponseEntity<ReparationDto> findById(Integer id) {
        return ResponseEntity.ok(reparationService.findById(id));
    }

    @Override
    public ResponseEntity<List<ReparationDto>> findByDate(Instant dateRep) {
        return ResponseEntity.ok(reparationService.findByDate(dateRep));
    }

    @Override
    public ResponseEntity<List<ReparationDto>> findAll() {
        return ResponseEntity.ok(reparationService.findAll());
    }

    @Override
    public void delete(Integer id) {
        reparationService.delete(id);
    }
}
