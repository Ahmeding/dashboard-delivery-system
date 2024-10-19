package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {

    List<Mission> findAllByDateDebut(Instant dateMission);

    @Query("""
            SELECT mission
            FROM Mission mission
            """)
    Page<Mission> findAllMissions(Pageable pageable);
}
