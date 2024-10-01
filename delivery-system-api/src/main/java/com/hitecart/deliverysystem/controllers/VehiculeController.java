package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.VehiculeApi;
import com.hitecart.deliverysystem.dto.VehiculeDto;
import com.hitecart.deliverysystem.services.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehiculeController implements VehiculeApi {


    private final VehiculeService vehiculeService;
    @Autowired
    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @Override
    public ResponseEntity<VehiculeDto> save(VehiculeDto dto) {

        System.out.println("----------save---------->"+ dto);
        return ResponseEntity.ok(vehiculeService.save(dto));
    }

    @Override
    public ResponseEntity<VehiculeDto> findById(Integer id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @Override
    public ResponseEntity<VehiculeDto> findByImmatriculation(String immat) {
        return ResponseEntity.ok(vehiculeService.findByImmatriculation(immat));
    }

    @Override
    public ResponseEntity<List<VehiculeDto>> findAll() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @Override
    public void delete(Integer id) {
        vehiculeService.delete(id);
    }
}
