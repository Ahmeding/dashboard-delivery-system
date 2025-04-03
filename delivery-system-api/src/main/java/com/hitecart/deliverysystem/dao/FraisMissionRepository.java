package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.FraisMission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FraisMissionRepository extends JpaRepository<FraisMission,Integer> {
    FraisMission findByMissionId(Integer idMission);

    @Query("""
            SELECT fraisMission
            FROM FraisMission fraisMission
            """)
    Page<FraisMission> findAllFraisMission(Pageable pageable);
}
