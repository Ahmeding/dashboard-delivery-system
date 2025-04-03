package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.EntrepriseDto;

import java.util.List;

public interface EntrepriseService {
    EntrepriseDto save(EntrepriseDto dto);
    EntrepriseDto findById(Integer id);
    EntrepriseDto findByRaisonSocial(String raisonSocial);
    List<EntrepriseDto> findAll();
    void delete(Integer id);

    PageResponse<EntrepriseDto> findAllEntreprises(int page, int size);
}
