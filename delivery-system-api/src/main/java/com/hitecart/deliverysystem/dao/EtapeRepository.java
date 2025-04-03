package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Etape;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface EtapeRepository extends JpaRepository<Etape, Integer> {
    List<Etape> findByMissionId(Integer idMission);

    void deleteByMissionId(Integer missionId);
}
