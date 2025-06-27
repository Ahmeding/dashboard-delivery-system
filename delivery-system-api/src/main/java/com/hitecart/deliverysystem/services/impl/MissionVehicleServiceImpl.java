package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleResponse;
import com.hitecart.deliverysystem.dao.MissionVehicleRepository;
import com.hitecart.deliverysystem.mapper.MissionVehicleMapper;
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
    private final MissionVehicleMapper missionVehicleMapper;

    @Override
    public List<MissionVehicleResponse> findByVehicleId(Integer vehicleId) {

        return missionVehicleRepository.findByVehicleId(vehicleId)
                .stream()
                .map(missionVehicleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MissionVehicleResponse> findByMissionId(Integer missionId) {
        return  missionVehicleRepository
                .findByMissionId(missionId)
                .stream()
                .map(missionVehicleMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public Integer findUnavailableVehicles(Integer vehicleId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return missionVehicleRepository.findUnavailableVehicles(vehicleId,startDateTime,endDateTime);
    }
}
