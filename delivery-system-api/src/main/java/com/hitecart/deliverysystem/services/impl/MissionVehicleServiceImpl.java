package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.basic.MissionVehicleDto;
import com.hitecart.deliverysystem.dao.MissionVehicleRepository;
import com.hitecart.deliverysystem.services.IMissionVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionVehicleServiceImpl implements IMissionVehicleService {
    private final MissionVehicleRepository missionVehicleRepository;

    @Override
    public List<MissionVehicleDto> findByVehicleId(Integer vehicleId) {

        return missionVehicleRepository.findByVehiculeId(vehicleId)
                .stream()
                .map(MissionVehicleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<MissionVehicleDto> findByMissionId(Integer missionId) {
        return  missionVehicleRepository
                .findByMissionId(missionId)
                .stream()
                .map(MissionVehicleDto::fromEntity)
                .collect(Collectors.toList());

    }

    @Override
    public List<Integer> findUnavailableVehicles(List<Integer> vehicleIds, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return missionVehicleRepository.findUnavailableVehicles(vehicleIds,startDateTime,endDateTime);
    }
}
