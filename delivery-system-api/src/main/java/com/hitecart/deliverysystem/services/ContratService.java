package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.ContratDto;

import java.util.List;

public interface ContratService {
    ContratDto save(ContratDto dto);
    ContratDto findById(Integer id);
    List<ContratDto> findAllByIdChauffeur(Integer idChauffeur);
    List<ContratDto> findAll();
    void delete(Integer id);
}
