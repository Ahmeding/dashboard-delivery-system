package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.PieceRechangeApi;
import com.hitecart.deliverysystem.dto.PieceRechangeDto;
import com.hitecart.deliverysystem.services.PieceRechangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PieceRechangeController implements PieceRechangeApi {

    private PieceRechangeService pieceRechangeService;

    @Autowired
    public PieceRechangeController(PieceRechangeService pieceRechangeService) {
        this.pieceRechangeService = pieceRechangeService;
    }

    @Override
    public ResponseEntity<PieceRechangeDto> save(PieceRechangeDto dto) {
        return ResponseEntity.ok(pieceRechangeService.save(dto));
    }

    @Override
    public ResponseEntity<PieceRechangeDto> findById(Integer id) {
        return ResponseEntity.ok(pieceRechangeService.findById(id));
    }

    @Override
    public ResponseEntity<PieceRechangeDto> findByReference(String ref) {
        return ResponseEntity.ok(pieceRechangeService.findByReference(ref));
    }

    @Override
    public ResponseEntity<List<PieceRechangeDto>> findAll() {
        return ResponseEntity.ok(pieceRechangeService.findAll());
    }

    @Override
    public void delete(Integer id) {
        pieceRechangeService.delete(id);
    }
}
