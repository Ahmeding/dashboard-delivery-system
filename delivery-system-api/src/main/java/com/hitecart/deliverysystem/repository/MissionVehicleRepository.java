package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.dto.MissionVehicleDto;
import com.hitecart.deliverysystem.model.MissionVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MissionVehicleRepository extends JpaRepository<MissionVehicle, Integer> {
    void deleteByMissionId(Integer missionId);

    List<MissionVehicle> findByVehiculeId(Integer vehicleId);

    List<MissionVehicle> findByMissionId(Integer missionId);

    @Query("""
            SELECT mv.vehicule.id
            FROM MissionVehicle mv
            JOIN Mission m ON mv.mission.id = m.id
                WHERE mv.vehicule.id IN :vehicleIds
                 AND (:startDateTime < m.dateFin AND :endDateTime > m.dateDebut )
            """)
    List<Integer> findUnavailableVehicles(
            @Param("vehicleIds") List<Integer> vehicleIds,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
}
