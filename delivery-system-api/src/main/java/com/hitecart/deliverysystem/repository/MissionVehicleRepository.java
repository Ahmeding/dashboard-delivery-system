package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.dto.MissionVehicleDto;
import com.hitecart.deliverysystem.model.MissionVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionVehicleRepository extends JpaRepository<MissionVehicle, Integer> {
    void deleteByMissionId(Integer missionId);

    List<MissionVehicle> findByVehiculeId(Integer vehicleId);

    List<MissionVehicle> findByMissionId(Integer missionId);
}
