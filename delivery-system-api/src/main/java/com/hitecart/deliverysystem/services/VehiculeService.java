package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.VehiculeDto;

import java.util.List;

public interface VehiculeService {
    VehiculeDto save(VehiculeDto dto);
    VehiculeDto findById(Integer id);
    VehiculeDto findByImmatriculation(String immat);
    List<VehiculeDto> findAll();
    void delete(Integer id);
}
