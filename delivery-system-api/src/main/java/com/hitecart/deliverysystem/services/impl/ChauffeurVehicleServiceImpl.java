package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.ChauffeurVehicleDto;
import com.hitecart.deliverysystem.model.ChauffeurVehicle;
import com.hitecart.deliverysystem.repository.ChauffeurVehicleRepository;
import com.hitecart.deliverysystem.services.security.IChauffeurVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChauffeurVehicleServiceImpl implements IChauffeurVehicleService {

    private final ChauffeurVehicleRepository chauffeurVehicleRepository;

    @Override
    public ChauffeurVehicleDto findByChauffeurId(Integer chauffeurId) {
        return ChauffeurVehicleDto.fromEntity(chauffeurVehicleRepository.findByChauffeurId(chauffeurId));
    }

    @Override
    public List<ChauffeurVehicleDto> findByMissionVehicleId(Integer missionVehicleId) {
        List<ChauffeurVehicle> chauffeurVehicles =
                chauffeurVehicleRepository.findByMissionVehicleId(missionVehicleId);

        return chauffeurVehicles.stream().map(ChauffeurVehicleDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteByChauffeurId(Integer chauffeurId) {

    }

    @Override
    public void deleteByMissionVehicleId(Integer missionVehicleId) {
        chauffeurVehicleRepository.deleteByMissionVehicleId(missionVehicleId);

    }
}
