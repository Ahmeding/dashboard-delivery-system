package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.dto.LigneFraisMissionDto;
import com.hitecart.deliverysystem.model.FraisMission;
import com.hitecart.deliverysystem.model.LigneFraisMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LigneFraisMissionRepository extends JpaRepository<LigneFraisMission,Integer> {
    List<LigneFraisMission> findByFraisMissionId(Integer idFMission);

    List<LigneFraisMission> findAllByFraisMissionId(Integer idFraisMission);
}
