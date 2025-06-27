package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Vehicle;
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
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Optional<Vehicle> findByRegistrationNbr(String registrationNb);

    @Query("""
            SELECT vehicle
            From Vehicle vehicle
            """)
    Page<Vehicle> findAllVehicles(Pageable pageable);
    @Modifying
    @Query("UPDATE Vehicle v set v.isAssigned=:isAssigned WHERE v.id IN :ids" )
    void bulkUpdateAssignmentStatus(
            @Param("ids") List<Integer> ids,
            @Param("isAssigned") boolean isAssigned);

    Boolean existsByVinNbr(String vinNbr);

    Boolean existsByRegistrationNbr(String registrationNbr);
}
