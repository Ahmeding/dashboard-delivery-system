package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.DayPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayPlanRepository extends JpaRepository<DayPlan, Integer> {
    //List<DayPlan> findAllByWeeklyPlanningId(Integer weeklyPlanningId);
}
