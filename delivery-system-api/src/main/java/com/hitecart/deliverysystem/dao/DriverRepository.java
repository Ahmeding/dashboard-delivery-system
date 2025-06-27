package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.enums.DriverExperience;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Integer> {

   Optional<Driver> findDriverByEmail(String email);
   List<Driver> findAllByEnabled(Boolean isActive);
   @Query("""
            SELECT driver
            FROM Driver driver
          """)
    Page<Driver> findAllDrivers(Pageable pageable);

    @Modifying
    @Query("UPDATE Driver d SET d.isAssigned =:isAssigned WHERE d.id IN :ids")
    void bulkUpdateAssignmentStatus(
            @Param("ids") List<Integer> ids,
            @Param("isAssigned") boolean isAssigned);

    @Query("SELECT c FROM Driver c WHERE " +
            "c.isAssigned = false AND " +
            "c.experienceLevel IN :requiredLevels AND " +
            "NOT EXISTS (SELECT cv FROM DriverVehicle cv WHERE cv.driver = c " +
            "AND cv.missionVehicle.mission.startDate < :endTime " +
            "AND cv.missionVehicle.mission.endDate > :startTime)")
    List<Driver> findAvailableDriversByExperience(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("requiredLevels") List<DriverExperience> requiredLevels
    );

    // ChauffeurRepository.java
    @EntityGraph(attributePaths = {"stats", "assignments.missionVehicle.mission"})
    List<Driver> findAll();

    Boolean existsByEmailIgnoreCase(String email);
    Boolean existsBySecuNbr(String secuNbr);
    Boolean existsByRib(String rib);
    Boolean existsByDrivinglicenseNbr(String drivinglicenseNbr);
    Boolean existsByPhoneNbr(String phoneNbr);
}
