package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.basic.WeeklyPlanningDto;

import java.util.List;

public interface WeeklyPlanningService {
    List<WeeklyPlanningDto> findAll();
    WeeklyPlanningDto findById(Integer id);
    WeeklyPlanningDto save(WeeklyPlanningDto weeklyplanning);
    void deleteById(Integer id);
}
