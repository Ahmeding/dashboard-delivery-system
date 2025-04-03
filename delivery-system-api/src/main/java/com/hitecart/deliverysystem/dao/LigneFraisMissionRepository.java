package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.LigneFraisMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneFraisMissionRepository extends JpaRepository<LigneFraisMission,Integer> {
    List<LigneFraisMission> findByFraisMissionId(Integer idFMission);

    List<LigneFraisMission> findAllByFraisMissionId(Integer idFraisMission);
}
