package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.dto.MissionAssignmentDto;
import com.hitecart.deliverysystem.dto.MissionView;
import com.hitecart.deliverysystem.model.DeliveryDestination;
import com.hitecart.deliverysystem.model.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer>, JpaSpecificationExecutor<Mission> {

    List<Mission> findAllByDateDebut(Instant dateMission);

    @Query("""
            SELECT mission
            FROM Mission mission
            """)
    Page<Mission> findAllMissions(Pageable pageable);

    @Query("""
        SELECT DISTINCT m FROM Mission m
        WHERE (m.dateDebut < :end AND m.dateFin > :start)
        AND (
            m.id IN (
                SELECT mv.mission.id FROM MissionVehicle mv 
                WHERE mv.vehicule.id IN :vehicleIds
            )
            OR 
            m.id IN (
                SELECT cv.missionVehicle.mission.id FROM ChauffeurVehicle cv 
                WHERE cv.chauffeur.id IN :driverIds
            )
        )
        
    """)
    List<Mission> findConflictingMissions(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("vehicleIds") List<Integer> vehicleIds,
            @Param("driverIds") List<Integer> driverIds
    );

    boolean existsByDeliveryDestinationAndDateDebut(DeliveryDestination deliveryDestination, LocalDateTime date);

    @Query("""
            SELECT count(m) >0
            FROM Mission m
            WHERE m.deliveryDestination = :destination
            AND(
            (m.dateDebut <= :end AND m.dateFin >= :start)OR
            (m.dateDebut BETWEEN :start AND :end)
            )
            """)
    boolean existsByDeliveryDestinationAndTimeRange(
            @Param("destination")DeliveryDestination destination,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
    SELECT m FROM Mission m
    WHERE m.dateDebut < :weekEnd
     AND m.dateFin > :weekStart
     AND NOT EXISTS (
            SELECT mv FROM MissionVehicle mv
            WHERE mv.mission = m
            AND mv.assignedAt >= :weekStart
            AND mv.assignedAt <= :weekEnd
        )
    """)

    List<Mission> findMissionsWithVehiclesAndDrivers(
            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd);

    @Query("""
    SELECT m FROM Mission m
    WHERE m.dateDebut < :weekEnd
     AND m.dateFin > :weekStart
     AND NOT EXISTS (
            SELECT mv FROM MissionVehicle mv
            WHERE mv.mission = m
            AND mv.assignedAt >= :weekStart
            AND mv.assignedAt <= :weekEnd
        )
    """)
    @EntityGraph(attributePaths = {"deliveryDestination.serviceDelivery", "missionVehicules"})
    List<Mission> findByDateRange(
            @Param("weekStart")LocalDateTime weekStart,
            @Param("weekEnd")LocalDateTime weekEnd);

    // Custom repository method
    @Query("""
    SELECT m.id as missionId, m.reference as missionReference, 
           m.dateDebut as startTime, m.dateFin as endTime,
           v.id as vehicleId
    FROM Mission m
    JOIN m.missionVehicules mv
    JOIN mv.vehicule v
    WHERE m.dateDebut BETWEEN :start AND :end
""")
    List<MissionAssignmentDto> findMissionAssignments(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);

    @Query("SELECT m FROM Mission m WHERE m.dateDebut BETWEEN :start AND :end")
    List<MissionView> findMissionsByDateRange(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);



    @EntityGraph(attributePaths = "missionVehicules")
    @Query("""
        SELECT DISTINCT m FROM Mission m
        WHERE m.id = :id
""")
    Optional<Mission> findByIdWithMissionVehiclesAndChauffeurs(@Param("id") Integer id);
}

