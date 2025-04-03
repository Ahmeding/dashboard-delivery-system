package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Vehicule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer> {
    Optional<Vehicule> findByImmatriculation(String immatriculation);

    @Query("""
            SELECT vehicle
            From Vehicule vehicle
            """)
    Page<Vehicule> findAllVehicles(Pageable pageable);
    @Modifying
    @Query("UPDATE Vehicule v set v.isAssigned=:isAssigned WHERE v.id IN :ids" )
    void bulkUpdateAssignmentStatus(
            @Param("ids") List<Integer> ids,
            @Param("isAssigned") boolean isAssigned);
}
