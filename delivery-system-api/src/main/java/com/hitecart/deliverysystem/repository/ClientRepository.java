package com.hitecart.deliverysystem.repository;

import java.util.Optional;
import com.hitecart.deliverysystem.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findClientByAdresseEmail(String email);

    @Query("""
            SELECT client
            FROM Client client
        """)
    Page<Client> findAllClients(Pageable pageable);
}
