package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.MissionVehicle;
import com.hitecart.deliverysystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MissionVehicleRepository extends JpaRepository<MissionVehicle, Integer> {
    void deleteByMissionId(Integer missionId);

    List<MissionVehicle> findByVehicleId(Integer vehicleId);
    Optional<MissionVehicle> findByMissionIdAndVehicleId(Integer missionId, Integer vehicleId);
    List<MissionVehicle> findByMissionId(Integer missionId);

    @Query("""
            SELECT mv.vehicle.id
            FROM MissionVehicle mv
            JOIN Mission m ON mv.mission.id = m.id
                WHERE mv.vehicle.id = :vehicleIds
                 AND (:startDateTime < m.endDate AND :endDateTime > m.startDate )
            """)
    Integer findUnavailableVehicles(
            @Param("vehicleIds") Integer vehicleId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);


    @Query("SELECT mv FROM MissionVehicle mv " +
            "WHERE mv.vehicle IN :vehicles " +
            "AND mv.mission.endDate > :start " +
            "AND mv.mission.startDate < :end")
    List<MissionVehicle> findByVehiculesAndTime(
            @Param("vehicles") List<Vehicle> vehicles,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end);
}
