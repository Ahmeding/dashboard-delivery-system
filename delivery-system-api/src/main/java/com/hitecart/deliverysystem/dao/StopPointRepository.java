package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.StopPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface StopPointRepository extends JpaRepository<StopPoint, Integer> {
    List<StopPoint> findByMissionId(Integer idMission);

    void deleteByMissionId(Integer missionId);

    @Query("""
            SELECT sp
            FROM StopPoint sp
            WHERE sp.mission.id = :missionId
            AND sp.title = :title
            AND sp.departureDate = :departureDate
            AND sp.arrivalDate = :arrivalDate
            AND sp.departureLocation = :departureLocation
            AND sp.arrivalLocation = :arrivalLocation
            AND sp.id <> :excludeId
            """)
    Optional<StopPoint> findByMissionIdByDateByLocationByName(
            @Param("missionId") Integer missionId,
            @Param("title") String title,
            @Param("departureDate") LocalDate departureDate,
            @Param("arrivalDate") LocalDate arrivalDate,
            @Param("departureLocation") String departureLocation,
            @Param("arrivalLocation") String arrivalLocation,
            @Param("excludeId") Integer excludeId);
}
