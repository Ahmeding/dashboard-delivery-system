package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.UtilisateurApi;
import com.hitecart.deliverysystem.dto.basic.RoleDto;
import com.hitecart.deliverysystem.dto.basic.UtilisateurDto;
import com.hitecart.deliverysystem.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class UtilisateurController implements UtilisateurApi {

    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    public ResponseEntity<UtilisateurDto> save(UtilisateurDto dto) {
        return ResponseEntity.ok(utilisateurService.save(dto));
    }

    @Override
    public ResponseEntity<UtilisateurDto> findById(Integer id) {
        return ResponseEntity.ok(utilisateurService.findById(id));
    }

    @Override
    public ResponseEntity<UtilisateurDto> findByEmail(String email) {
        return ResponseEntity.ok(utilisateurService.findByEmail(email));
    }

    @Override
    public ResponseEntity<List<UtilisateurDto>> findAll() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    @Override
    public void delete(Integer id) {
        utilisateurService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<UtilisateurDto>> findAllUtilisateurs(int page, int size) {
        return ResponseEntity.ok(utilisateurService.findAllUsers(page,size));
    }

    @Override
    public ResponseEntity<UtilisateurDto> updateUserRoles(Integer id, List<RoleDto> rolesDto) {

        UtilisateurDto updated = utilisateurService.updateUserRoles(id, rolesDto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<UtilisateurDto> updateUserPassword(Integer id, UtilisateurDto dto) {
        UtilisateurDto updated = utilisateurService.updateUserPassword(id, dto);
        return ResponseEntity.ok(updated);
    }
}
