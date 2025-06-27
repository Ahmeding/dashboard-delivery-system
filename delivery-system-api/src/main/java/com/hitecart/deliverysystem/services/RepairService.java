package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.repair.RepairRequest;
import com.hitecart.deliverysystem.dto.repair.RepairResponse;
import com.hitecart.deliverysystem.dto.sparepart.SparePartResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface RepairService {
    RepairResponse save(RepairRequest dto);
    RepairResponse updateRepairInfo(Integer repairId ,RepairRequest repairRequest);
    RepairResponse findById(Integer id);
    List<RepairResponse> findByDate(Instant dateRep);
    List<RepairResponse> findAll();

    void updateReparationCost(String flag, Integer repairId, SparePartResponse sparePartRequest, BigDecimal delta);
    void delete(Integer id);

    PageResponse<RepairResponse> findAllRepairs(int page, int size);
}
