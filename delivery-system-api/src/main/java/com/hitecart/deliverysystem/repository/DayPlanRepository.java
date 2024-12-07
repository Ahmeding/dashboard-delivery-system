package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.DayPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayPlanRepository extends JpaRepository<DayPlan, Integer> {
}
