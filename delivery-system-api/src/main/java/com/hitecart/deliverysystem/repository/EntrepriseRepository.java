package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.Entreprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {

    Optional<Entreprise> findEntrepriseByRaisonSocial(String raisonSocial);

    @Query("""
            SELECT entreprise
            FROM Entreprise entreprise
            """)
    Page<Entreprise> findAllEntreprises(Pageable pageable);
}
