package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.SparePart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface SparePartRepository extends JpaRepository<SparePart, Integer> {
    Optional<SparePart> findByReference(String ref);

    List<SparePart> findAllByRepairId(Integer id);

    @Query("""
            SELECT sp
            FROM SparePart sp
            """)
    Page<SparePart> findAllSpareParts(Pageable pageable);
}
