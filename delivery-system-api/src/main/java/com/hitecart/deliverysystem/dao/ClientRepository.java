package com.hitecart.deliverysystem.dao;

import java.util.Optional;
import com.hitecart.deliverysystem.model.Client;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findClientByEmail(String email);

    @Query("""
            SELECT client
            FROM Client client
        """)
    Page<Client> findAllClients(Pageable pageable);

    Boolean existsBySiren( String siren);

    Boolean existsBySiret(String siret);

    @Query("""
            SELECT c
            FROM Client c
            WHERE c.email = :email
            AND c.id <> :excludeId
        """)
    Optional<Client> findByEmail(
            @Param("email") String email,
            @Param("excludeId") Integer excludeId);
}
