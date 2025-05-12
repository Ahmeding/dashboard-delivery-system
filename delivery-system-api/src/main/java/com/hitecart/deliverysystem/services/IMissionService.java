package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.EtapeDto;
import com.hitecart.deliverysystem.dto.basic.FraisMissionDto;
import com.hitecart.deliverysystem.dto.basic.MissionDto;
import com.hitecart.deliverysystem.dto.basic.MissionResponse;
import com.hitecart.deliverysystem.model.Etape;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import com.hitecart.deliverysystem.model.enums.KmFlag;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.List;

public interface IMissionService {
    MissionDto save(MissionDto dto);
    MissionDto update(Integer id,MissionDto dto);
    MissionDto updateKilometrage(KmFlag flag, Integer idMission,Integer vehicleId, double kmDebut);
    MissionDto updateEtatMission(Integer idMission, EtatMission etatMission) ;
    MissionDto updateExpensesMission(Integer idMission,  List<FraisMissionDto> expensesDto) ;
    MissionDto updateEtapeMission(Integer idMission,  List<EtapeDto> etapesDto) ;
    MissionDto findById(Integer id);
    List<MissionDto> findAllByDate(Instant dateMission);
    List<MissionResponse> findAll();
    void delete(Integer id);

    void generateMissions();
    void generateWeeklyAssignments();
    List<Integer> checkOverlappingVehicles(MissionDto dto);
    List<Integer> checkOverlappingDrivers(MissionDto dto);
    PageResponse<MissionDto> findAllMissions(int page, int size);
    PageResponse<MissionDto> findAllMissionsByDriverId(int page, int size, Authentication connectedUser);

    List<MissionResponse> findAllMissionsByDriver(Authentication connectedUser);
}
