package com.hitecart.deliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hitecart.deliverysystem.model.WeeklyPlanning;

public interface WeeklyPlanningRepository extends JpaRepository<WeeklyPlanning, Integer> {
}
