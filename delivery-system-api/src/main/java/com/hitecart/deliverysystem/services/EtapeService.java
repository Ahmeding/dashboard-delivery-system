package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.EtapeDto;

import java.util.List;

public interface EtapeService {
    EtapeDto save(EtapeDto dto);
    EtapeDto findById(Integer id);
    List<EtapeDto> findByIdMission(Integer idMission);
    List<EtapeDto> findAll();
    void delete(Integer id);
}
