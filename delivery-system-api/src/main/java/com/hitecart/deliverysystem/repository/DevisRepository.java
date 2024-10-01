package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.Devis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Integer> {

    List<Devis> findAllByClientId(Integer id);

    Optional<Devis> findDevisByCode(String code);
}
