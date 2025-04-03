package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Reparation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface ReparationRepository extends JpaRepository<Reparation, Integer> {
    List<Reparation> findByDateDebut(Instant dateRep);

    List<Reparation> findAllByVehiculeId(Integer idVehicule);

    @Query("""
            SELECT r FROM Reparation r
            """)
    Page<Reparation> findAllRepairs(Pageable pageable);
}
