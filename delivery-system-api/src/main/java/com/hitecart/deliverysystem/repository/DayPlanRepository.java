package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.DayPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DayPlanRepository extends JpaRepository<DayPlan, Integer> {
    List<DayPlan> findAllByWeeklyPlanningId(Integer weeklyPlanningId);
}
