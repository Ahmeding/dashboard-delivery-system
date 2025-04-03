package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.MissionDto;
import com.hitecart.deliverysystem.dto.basic.MissionResponse;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.List;

public interface MissionService {
    MissionDto save(MissionDto dto);
    MissionDto updateKilometrage(String flag,Integer idMission, double kmDebut);
    MissionDto updateEtatMission(Integer idMission, EtatMission etatMission) ;
//    MissionDto deleteEtape(Integer idMission, Integer idEtape);
//    MissionDto deleteChauffeur(Integer idMission, Integer idChauffeur);
//    MissionDto deleteVehicule(Integer idMission, Integer idVehicule);
//    MissionDto deleteFraisMission(Integer idMission, Integer idFM);
//    List<Etape> findAllEtapeByMissionId(Integer idMission);
//    FraisMissionDto updateX(Integer idMF, Integer idLFM, BigDecimal qte);
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
}
