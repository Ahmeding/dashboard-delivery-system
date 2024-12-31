package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.ChauffeurVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChauffeurVehicleRepository extends JpaRepository<ChauffeurVehicle, Integer> {
    ChauffeurVehicle findByChauffeurId(Integer chauffeurId);
    List<ChauffeurVehicle> findByMissionVehicleId(Integer missionVehicleId);
    void deleteByMissionVehicleId(Integer missionVehicleId);

    @Query("""
        SELECT cv.chauffeur.id
        FROM ChauffeurVehicle cv
        JOIN MissionVehicle mv ON cv.missionVehicle.id= mv.id
        JOIN Mission m ON mv.mission.id = m.id
            WHERE cv.chauffeur.id IN :chauffeurIds
            AND(:startDateTime <= m.dateFin AND :endDateTime >= m.dateDebut)
        """)
    List<Integer> findUnavailableDrivers(
            List<Integer> chauffeurIds,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime);
}
