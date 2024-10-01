package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.PieceRechange;
import com.hitecart.deliverysystem.model.Reparation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface ReparationRepository extends JpaRepository<Reparation, Integer> {
    List<Reparation> findByDateDebut(Instant dateRep);

    List<Reparation> findAllByVehiculeId(Integer idVehicule);

}
