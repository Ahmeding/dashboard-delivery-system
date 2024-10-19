package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.PieceRechange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface PieceRechangeRepository extends JpaRepository<PieceRechange, Integer> {
    Optional<PieceRechange> findByReference(String ref);

    List<PieceRechange> findAllByReparationId(Integer id);

    @Query("""
            SELECT piece
            FROM PieceRechange piece
            """)
    Page<PieceRechange> findAllPieceRechanges(Pageable pageable);
}
