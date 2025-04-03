package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.EntrepriseApi;
import com.hitecart.deliverysystem.dto.basic.EntrepriseDto;
import com.hitecart.deliverysystem.services.EntrepriseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class EntrepriseController implements EntrepriseApi {

    private EntrepriseService entrepriseService;

    public EntrepriseController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @Override
    public ResponseEntity<EntrepriseDto> save(EntrepriseDto dto) {
        return ResponseEntity.ok(entrepriseService.save(dto));
    }

    @Override
    public ResponseEntity<EntrepriseDto> findById(Integer id) {
        return ResponseEntity.ok(entrepriseService.findById(id));
    }

    @Override
    public ResponseEntity<EntrepriseDto> findByRaisonSocial(String raisonSocial) {
        return ResponseEntity.ok(entrepriseService.findByRaisonSocial(raisonSocial));
    }

    @Override
    public ResponseEntity<List<EntrepriseDto>> findAll() {
        return ResponseEntity.ok(entrepriseService.findAll());
    }

    @Override
    public void delete(Integer id) {
            entrepriseService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<EntrepriseDto>> findAllEntreprises(int page, int size) {
        return ResponseEntity.ok(entrepriseService.findAllEntreprises(page,size));
    }
}
