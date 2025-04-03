package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Contrat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Integer> {

        List<Contrat> findAllByChauffeur_Id(Integer idChauffeur);

        @Query("""
                SELECT contrat
                FROM Contrat contrat
            """)
    Page<Contrat> findAllContracts(Pageable pageable);
}
