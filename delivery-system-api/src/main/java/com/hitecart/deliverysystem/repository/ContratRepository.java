package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.Contrat;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Integer> {

        List<Contrat> findAllByChauffeur_Id(Integer idChauffeur);
}
