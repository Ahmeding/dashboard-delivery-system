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

    List<Mission> findAllByStartDate(Instant dateMission);

    @Query("""
            SELECT mission
            FROM Mission mission
            """)
    Page<Mission> findAllMissions(Pageable pageable);

    @Query("""
                SELECT DISTINCT m FROM Mission m
                WHERE (m.startDate < :end AND m.endDate > :start)
                AND (
                    m.id IN (
                        SELECT mv.mission.id FROM MissionVehicle mv
                        WHERE mv.vehicle.id IN :vehicleIds
                    )
                    OR
                    m.id IN (
                        SELECT cv.missionVehicle.mission.id FROM DriverVehicle cv
                        WHERE cv.driver.id IN :driverIds
                    )
                )
            """)
    List<Mission> findConflictingMissions(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("vehicleIds") List<Integer> vehicleIds,
            @Param("driverIds") List<Integer> driverIds
    );

    boolean existsByDeliveryDestinationAndStartDate(DeliveryDestination deliveryDestination, LocalDateTime date);

    @Query("""
            SELECT m
            FROM Mission m
            WHERE m.deliveryDestination = :destination
            AND(
            (m.startDate <= :end AND m.endDate >= :start)OR
            (m.startDate BETWEEN :start AND :end)
            )
            """)
    Optional<Mission> findByDeliveryDestinationAndTimeRange(
            @Param("destination") DeliveryDestination destination,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
            SELECT m FROM Mission m
            WHERE m.startDate < :weekEnd
             AND m.endDate > :weekStart
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
            WHERE m.startDate < :weekEnd
             AND m.endDate > :weekStart
             AND NOT EXISTS (
                    SELECT mv FROM MissionVehicle mv
                    WHERE mv.mission = m
                    AND mv.assignedAt >= :weekStart
                    AND mv.assignedAt <= :weekEnd
                )
            """)
    @EntityGraph(attributePaths = {"deliveryDestination.deliveryService", "missionVehicles"})
    List<Mission> findByDateRange(
            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd);

    // Custom repository method
    @Query("""
                SELECT m.id as missionId, m.reference as missionReference,
                       m.startDate as startTime, m.endDate as endTime,
                       v.id as vehicleId
                FROM Mission m
                JOIN m.missionVehicles mv
                JOIN mv.vehicle v
                WHERE m.startDate BETWEEN :start AND :end
            """)
    List<MissionAssignmentDto> findMissionAssignments(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);

    @Query("SELECT m FROM Mission m WHERE m.startDate BETWEEN :start AND :end")
    List<MissionView> findMissionsByDateRange(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);


    @EntityGraph(attributePaths = "missionVehicles")
    @Query("""
                    SELECT DISTINCT m FROM Mission m
                    WHERE m.id = :id
            """)
    Optional<Mission> findByIdWithMissionVehiclesAndDrivers(@Param("id") Integer id);

    @Query("""
            SELECT m
            FROM Mission m
            WHERE m.deliveryDestination.id = :deliveryDestId
            """)
    List<Mission> findAllByDeliveryDestinationId(@Param("deliveryDestId") Integer deliveryDestId);

    @Query("""
            SELECT m
            FROM Mission m
            WHERE m.deliveryDestination.id = :deliveryDestId
            AND m.startDate = :startDate
            AND m.endDate = :endDate
            AND m.id <> :excludeId
            """)
    Optional<Mission> findAllByDeliveryDestinationIdAndStartDateAndEndDate(
            @Param("deliveryDestId") Integer deliveryDestId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("excludeId")Integer excludeId);

    List<Mission> findByStartDateBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd);
}

