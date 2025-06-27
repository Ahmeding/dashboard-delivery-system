package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.DriverVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverVehicleRepository extends JpaRepository<DriverVehicle, Integer> {
    DriverVehicle findByDriverId(Integer chauffeurId);
    List<DriverVehicle> findByMissionVehicleId(Integer missionVehicleId);


    @Query("""
    SELECT dv
    FROM DriverVehicle dv
    INNER JOIN MissionVehicle mv ON dv.missionVehicle.id = mv.id
    INNER JOIN Mission m on mv.mission.id= m.id
    WHERE dv.driver.id = :driverId
    """)
    Optional<DriverVehicle> findByDriverIdCheckAssigned(@Param("driverId") Integer driverId);

    void deleteByMissionVehicleId(Integer missionVehicleId);

    @Query("""
        SELECT cv.driver.id
        FROM DriverVehicle cv
        JOIN MissionVehicle mv ON cv.missionVehicle.id= mv.id
        JOIN Mission m ON mv.mission.id = m.id
            WHERE cv.driver.id IN :chauffeurIds
            AND(:startDateTime < m.endDate AND :endDateTime > m.startDate)
        """)
    List<Integer> findUnavailableDrivers(
            List<Integer> chauffeurIds,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime);

    @Query("""
            SELECT cv FROM DriverVehicle cv
            WHERE cv.driver IN :drivers
            AND cv.missionVehicle.mission.startDate < :end
            AND cv.missionVehicle.mission.endDate > :start
            """)
    List<DriverVehicle> findChauffeurVehiclesByDriversAndDateRange(
            @Param("drivers") List<Driver> drivers,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end);
}
