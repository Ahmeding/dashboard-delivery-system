package com.hitecart.deliverysystem.repository;

import java.util.Optional;
import com.hitecart.deliverysystem.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findClientByAdresseEmail(String email);
}
