package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Chauffeur;
import com.hitecart.deliverysystem.model.enums.DriverExperience;
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
public interface IChauffeurRepository extends JpaRepository<Chauffeur,Integer> {

   Optional<Chauffeur> findChauffeurByEmail(String email);
   List<Chauffeur> findAllByEnabled(Boolean isActive);
   @Query("""
            SELECT chauffeur
            FROM Chauffeur chauffeur
          """)
    Page<Chauffeur> findAllChauffeurs(Pageable pageable);

    Chauffeur save(Chauffeur requestChauffeur);

    @Modifying
    @Query("UPDATE Chauffeur c SET c.isAssigned =:isAssigned WHERE c.id IN :ids")
    void bulkUpdateAssignmentStatus(
            @Param("ids") List<Integer> ids,
            @Param("isAssigned") boolean isAssigned);

    @Query("SELECT c FROM Chauffeur c WHERE " +
            "c.isAssigned = false AND " +
            "c.experienceLevel IN :requiredLevels AND " +
            "NOT EXISTS (SELECT cv FROM ChauffeurVehicle cv WHERE cv.chauffeur = c " +
            "AND cv.missionVehicle.mission.dateDebut < :endTime " +
            "AND cv.missionVehicle.mission.dateFin > :startTime)")
    List<Chauffeur> findAvailableDriversByExperience(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("requiredLevels") List<DriverExperience> requiredLevels
    );

    // ChauffeurRepository.java
    @EntityGraph(attributePaths = {"stats", "assignments.missionVehicle.mission"})
    List<Chauffeur> findAll();

}
