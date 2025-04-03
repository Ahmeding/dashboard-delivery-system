package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.ContratApi;
import com.hitecart.deliverysystem.dto.basic.ContratDto;
import com.hitecart.deliverysystem.services.ContratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class ContratController implements ContratApi {

    private ContratService contratService;
    @Autowired
    public ContratController(ContratService contratService) {
        this.contratService = contratService;
    }

    @Override
    public ResponseEntity<ContratDto> save(ContratDto dto) {
        return ResponseEntity.ok(contratService.save(dto));
    }

    @Override
    public ResponseEntity<ContratDto> findById(Integer id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @Override
    public ResponseEntity<List<ContratDto>> findAllByIdChauffeur(Integer idChauffeur) {
        return ResponseEntity.ok(contratService.findAllByIdChauffeur(idChauffeur));
    }

    @Override
    public ResponseEntity<List<ContratDto>> findAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @Override
    public void delete(Integer id) {
        contratService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<ContratDto>> findAllContrats(int page, int size) {
        return ResponseEntity.ok(contratService.findAllContracts(page,size));
    }
}
