package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.ChauffeurDto;

import java.util.List;

public interface ChauffeurService {
    ChauffeurDto save(ChauffeurDto dto);
    ChauffeurDto findById(Integer id);
    ChauffeurDto findByEmail(String email);
    List<ChauffeurDto> findAll();
    void delete(Integer id);
    ChauffeurDto updateIsActive(Integer id, Boolean isActive);
    List<ChauffeurDto> findAllIsActive(Boolean isActive);

    PageResponse<ChauffeurDto> findAllDrivers(int page, int size);
}
