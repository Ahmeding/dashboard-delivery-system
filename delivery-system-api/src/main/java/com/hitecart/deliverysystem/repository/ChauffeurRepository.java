package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.dto.ChauffeurDto;
import com.hitecart.deliverysystem.model.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface ChauffeurRepository extends JpaRepository<Chauffeur,Integer> {

   Optional<Chauffeur> findChauffeurByAdresseEmail(String email);

}
