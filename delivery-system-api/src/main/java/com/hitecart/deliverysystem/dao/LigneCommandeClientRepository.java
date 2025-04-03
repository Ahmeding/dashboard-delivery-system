package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.LigneCommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {

    List<LigneCommandeClient> findAllByComamndeClientId(Integer id);
    List<LigneCommandeClient> findAllByComamndeClientCode(String codeCmdClient);

}
