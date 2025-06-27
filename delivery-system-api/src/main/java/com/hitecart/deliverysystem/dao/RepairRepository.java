package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Repair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface RepairRepository extends JpaRepository<Repair, Integer> {
    List<Repair> findByStartDate(Instant dateRep);

    List<Repair> findAllByVehicleId(Integer idVehicle);


    @Query("""
            SELECT r FROM Repair r
            """)
    Page<Repair> findAllRepairs(Pageable pageable);
}
