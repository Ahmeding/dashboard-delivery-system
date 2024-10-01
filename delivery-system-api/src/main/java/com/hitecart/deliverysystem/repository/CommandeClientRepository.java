package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface CommandeClientRepository extends JpaRepository<CommandeClient,Integer> {

    List<CommandeClient> findAllByClientId(Integer id);

    Optional<CommandeClient> findCommandeClientByCode(String code);
}
