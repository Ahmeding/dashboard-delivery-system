package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleResponse;
import com.hitecart.deliverysystem.mapper.DriverVehicleMapper;
import com.hitecart.deliverysystem.model.DriverVehicle;
import com.hitecart.deliverysystem.dao.DriverVehicleRepository;
import com.hitecart.deliverysystem.services.DriverVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DriverVehicleServiceImpl implements DriverVehicleService {

    private final DriverVehicleRepository driverVehicleRepository;
    private final DriverVehicleMapper driverVehicleMapper;

    @Override
    public DriverVehicleResponse findByChauffeurId(Integer chauffeurId) {
        return driverVehicleMapper.toResponse(driverVehicleRepository.findByDriverId(chauffeurId));
    }

    @Override
    public List<DriverVehicleResponse> findByMissionVehicleId(Integer missionVehicleId) {
        List<DriverVehicle> driverVehicles =
                driverVehicleRepository.findByMissionVehicleId(missionVehicleId);

        return driverVehicles.stream().map(driverVehicleMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteByChauffeurId(Integer chauffeurId) {

    }

    @Override
    public void deleteByMissionVehicleId(Integer missionVehicleId) {

        driverVehicleRepository.deleteByMissionVehicleId(missionVehicleId);

    }

    @Override
    public List<Integer> findUnavailableDrivers(List<Integer> chauffeurIds, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return driverVehicleRepository.findUnavailableDrivers(chauffeurIds,startDateTime,endDateTime);
    }
}
