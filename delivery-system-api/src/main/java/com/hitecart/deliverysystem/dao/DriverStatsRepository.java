package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.DriverStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DriverStatsRepository extends JpaRepository<DriverStats, Integer> {

    // DriverStatsRepository.java
    @Query("SELECT ds FROM DriverStats ds WHERE ds.chauffeur.id = :driverId")
    Optional<DriverStats> findByDriverId(@Param("driverId") Integer driverId);

}
