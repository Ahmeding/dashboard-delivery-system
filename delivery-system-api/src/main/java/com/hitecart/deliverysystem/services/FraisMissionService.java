package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.FraisMissionDto;

import java.math.BigDecimal;
import java.util.List;

public interface FraisMissionService {
    FraisMissionDto save(FraisMissionDto dto);
    FraisMissionDto updateFraisMission(FraisMissionDto dto);
    FraisMissionDto updateQuantite(Integer idMF, BigDecimal qte);
    FraisMissionDto findById(Integer id);
    FraisMissionDto findByIdMission(Integer idMission);
    List<FraisMissionDto> findAll();

    void delete(Integer id);

    PageResponse<FraisMissionDto> findAllFraisMission(int page, int size);
}
