package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseResponse;

import java.math.BigDecimal;
import java.util.List;

public interface MissionExpenseService {
    MissionExpenseResponse save(MissionExpenseRequest missionExpenseRequest);
    MissionExpenseResponse updateMissionExpenseInfo(Integer expenseId, MissionExpenseRequest missionExpenseRequest);
    MissionExpenseResponse updateQuantite(Integer idMF, BigDecimal qte);
    MissionExpenseResponse findById(Integer id);
    MissionExpenseResponse findByMissionId(Integer idMission);
    List<MissionExpenseResponse> findAll();

    void delete(Integer id);

    PageResponse<MissionExpenseResponse> findAllFraisMission(int page, int size);
}
