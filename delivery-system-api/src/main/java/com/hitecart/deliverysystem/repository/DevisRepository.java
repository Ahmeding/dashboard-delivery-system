package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.Devis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Integer> {

    List<Devis> findAllByClientId(Integer id);

    Optional<Devis> findDevisByCode(String code);

    @Query("""
            SELECT devis
            FROM Devis devis
            """)
    Page<Devis> findAllDevis(Pageable pageable);
}
