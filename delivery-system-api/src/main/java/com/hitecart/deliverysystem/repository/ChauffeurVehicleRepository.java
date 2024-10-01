package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.ChauffeurVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChauffeurVehicleRepository extends JpaRepository<ChauffeurVehicle, Integer> {
    ChauffeurVehicle findByChauffeurId(Integer chauffeurId);
    List<ChauffeurVehicle> findByMissionVehicleId(Integer missionVehicleId);
    void deleteByMissionVehicleId(Integer missionVehicleId);
}
