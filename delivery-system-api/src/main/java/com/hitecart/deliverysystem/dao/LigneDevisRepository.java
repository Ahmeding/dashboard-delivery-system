package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.LigneDevis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface LigneDevisRepository extends JpaRepository<LigneDevis, Integer> {
    List<LigneDevis> findAllByDevisId(Integer id);
}
