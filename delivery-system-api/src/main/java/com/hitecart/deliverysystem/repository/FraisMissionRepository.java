package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.FraisMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface FraisMissionRepository extends JpaRepository<FraisMission,Integer> {
    FraisMission findByMissionId(Integer idMission);
}
