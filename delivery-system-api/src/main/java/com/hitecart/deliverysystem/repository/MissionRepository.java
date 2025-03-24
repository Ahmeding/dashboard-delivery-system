package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.DeliveryDestination;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}
