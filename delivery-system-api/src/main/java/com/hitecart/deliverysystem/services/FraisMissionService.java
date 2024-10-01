package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.FraisMissionDto;
import com.hitecart.deliverysystem.dto.LigneFraisMissionDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

public interface FraisMissionService {
    FraisMissionDto save(FraisMissionDto dto);
    FraisMissionDto updateFraisMission(FraisMissionDto dto);
    List<LigneFraisMissionDto> findAllLignesFraisMissionByFraisMissionId(Integer idFraisMission);
    FraisMissionDto updateQuantite(Integer idMF, Integer idLFM, BigDecimal qte);
    FraisMissionDto findById(Integer id);
    FraisMissionDto findByIdMission(Integer idMission);
    List<FraisMissionDto> findAll();

    FraisMissionDto deleteLigneFraisMission(Integer idFraisMission, Integer idLigneFraisMission);
    void delete(Integer id);
}
