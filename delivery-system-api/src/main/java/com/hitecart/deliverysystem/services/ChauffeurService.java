package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.ChauffeurDto;

import java.util.List;

public interface ChauffeurService {
    ChauffeurDto save(ChauffeurDto dto);
    ChauffeurDto findById(Integer id);
    ChauffeurDto findByEmail(String email);
    List<ChauffeurDto> findAll();
    void delete(Integer id);
}
