package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.DevisApi;
import com.hitecart.deliverysystem.dto.DevisDto;
import com.hitecart.deliverysystem.services.DevisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DevisController implements DevisApi {

    private DevisService devisService;

    public DevisController(DevisService devisService) {
        this.devisService = devisService;
    }

    @Override
    public ResponseEntity<DevisDto> save(DevisDto dto) {
        return ResponseEntity.ok(devisService.save(dto));
    }

    @Override
    public ResponseEntity<DevisDto> findById(Integer id) {
        return ResponseEntity.ok(devisService.findById(id));
    }

    @Override
    public ResponseEntity<DevisDto> findByCode(String code) {
        return ResponseEntity.ok(devisService.findByCode(code));
    }

    @Override
    public ResponseEntity<List<DevisDto>> findAll() {
        return ResponseEntity.ok(devisService.findAll());
    }

    @Override
    public void delete(Integer id) {
            devisService.delete(id);
    }
}
