package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.basic.DayPlanDto;

import java.util.List;

public interface DayPlanService {
    List<DayPlanDto> findAllByWeeklyPlanningId(Integer weeklyPlanningId);
    List<DayPlanDto> findAll();
    DayPlanDto findById(Integer id);
    DayPlanDto save(DayPlanDto dto);
    void deleteById(Integer id);
}
