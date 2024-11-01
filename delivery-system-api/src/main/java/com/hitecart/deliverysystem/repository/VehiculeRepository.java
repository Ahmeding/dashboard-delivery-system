package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.Vehicule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer> {
    Optional<Vehicule> findByImmatriculation(String immatriculation);

    @Query("""
            SELECT vehicle
            From Vehicule vehicle
            """)
    Page<Vehicule> findAllVehicles(Pageable pageable);
}
