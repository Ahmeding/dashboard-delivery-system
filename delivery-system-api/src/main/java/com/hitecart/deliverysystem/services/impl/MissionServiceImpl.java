package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;
import com.hitecart.deliverysystem.repository.*;
import com.hitecart.deliverysystem.services.IMissionVehicleService;
import com.hitecart.deliverysystem.services.INotificationService;
import com.hitecart.deliverysystem.services.MissionService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.services.security.IChauffeurVehicleService;
import com.hitecart.deliverysystem.specification.MissionSpecification;
import com.hitecart.deliverysystem.validator.MissionValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MissionServiceImpl implements MissionService {

    private final IChauffeurVehicleService chauffeurVehicleService;
    private final IMissionVehicleService missionVehicleService;
    private final ChauffeurVehicleRepository chauffeurVehicleRepository;
    private final MissionVehicleRepository missionVehicleRepository;
    private final MissionRepository missionRepository;
    private final EtapeRepository etapeRepository;
    private final INotificationService notificationService;
    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final View error;
    private final UtilisateurService utilisateurServiceImpl;


    @Override
    public MissionDto save(MissionDto dto) {
        checkDto(dto, "Mission n'est pas valide",
                "la Mission n'est pas valide", ErrorCodes.MISSION_NOT_VALID_OPERATION);


        if (dto.getId() != null && dto.isMissionAccomplished()) {
            throw new InvalidOperationException("Impossible de modifier la mission lorsqu'elle est accompli",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }

        Mission savedMission = MissionDto.toEntity(dto);
        savedMission = missionRepository.save(savedMission);
            List<Etape> savedetapes = new ArrayList<>();

            // save etaps
        if (!dto.getEtapeDtos().isEmpty()) {
            Mission finalSavedMission = savedMission;
            dto.getEtapeDtos().forEach(etp -> {
                Etape etapeToSave = EtapeDto.toEntity(etp);
                etapeToSave.setMission(finalSavedMission);
                savedetapes .add(etapeRepository.save(etapeToSave));
            });
        }



        // save mission-vehicle history and chauffeur_vehicle

        if (!dto.getMissionVehiclesDtos().isEmpty()) {
            for (MissionVehicleDto mvehiculeDto : dto.getMissionVehiclesDtos()) {
                    MissionVehicle savedMissionVehicle = new MissionVehicle();
                    Vehicule vehicule= VehiculeDto.toEntity(mvehiculeDto.getVehicleDto());

                    savedMissionVehicle.setVehicule(vehicule);
                    savedMissionVehicle.setMission(savedMission);
                    savedMissionVehicle.setAssignedAt(LocalDateTime.now());
                    missionVehicleRepository.save(savedMissionVehicle);


                for(ChauffeurVehicleDto chauffeurByVehicleDto  : mvehiculeDto.getChauffeursVehicleDto()){

                    ChauffeurVehicle chauffeurVehicleToSave = new ChauffeurVehicle();

                    chauffeurVehicleToSave.setChauffeur(ChauffeurDto.toEntity(chauffeurByVehicleDto.getChauffeurDto()));
                    chauffeurVehicleToSave.setMissionVehicle(savedMissionVehicle);
                    chauffeurVehicleToSave.setAssignedAt(LocalDateTime.now());
                    notificationService.sendNotification(
                            chauffeurVehicleToSave.getChauffeur().getId(),
                            Notification.builder()
                                    .title("new mission")
                                    .message("new mission message")
                                    .status(NotificationStatus.SEND)
                                    .build());
                    chauffeurVehicleRepository.save(chauffeurVehicleToSave);

                }
            }
        }


        MissionDto savedMissionDto= MissionDto.fromEntity(savedMission);
        return savedMissionDto;

    }

    @Override
    public MissionDto updateKilometrage(String flag, Integer idMission, double kilometrage) {

        CheckParmasUtil.checkID(idMission,
                "Le parametre ID Mission est null",
                "Impossible de modifier une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        CheckParmasUtil.checkDouble(kilometrage,
                "Le parametre kilometrage de Debut de Mission est null",
                "Impossible de modifier une mission avec kilometrage null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);

        CheckParmasUtil.checkDoubleGTZero(kilometrage,
                "Le parametre kilometrage de Debut de Mission est egale zero",
                "Impossible de modifier une mission avec kilometrage egale à zero",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);


        Optional<Mission> missionOptional = missionRepository.findById(idMission);
        if (missionOptional == null) {
            new EntityNotFoundException("Aucune Mission avec l'id :"
                    + idMission + "n'a ete trouvé dans la base deonnée", ErrorCodes.MISSION_NOT_FOUND);
        }

        Mission mission = missionOptional.get();
        if (flag == "KM_DEBUT") {
            mission.setKmDebut(kilometrage);
        } else {
            mission.setKmFin(kilometrage);
        }
        missionRepository.save(mission);

        return MissionDto.fromEntity(mission);
    }

    @Override
    public MissionDto updateEtatMission(Integer idMission, EtatMission etatMission) {
        CheckParmasUtil.checkID(idMission,
                "Le parametre ID Mission est null",
                "Impossible de modifier une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        CheckParmasUtil.checkStringParma(etatMission.toString(),
                "Le parametre etatMission Mission est null",
                "Impossible de modifier une mission avec un etatMission null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);

        MissionDto missionDto = findById(idMission);
        if (missionDto.isMissionAccomplished()) {
            throw new InvalidOperationException(
                    "Impossible de modifier la commande lorsqu'elle est accompli",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        missionDto.setEtatMission(etatMission);

        return MissionDto.fromEntity(missionRepository.save(MissionDto.toEntity(missionDto)));
    }

    @Override
    public MissionDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Mission est null",
                "Impossible de trouver une mission avec un ID null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);
        Optional<Mission> missionOp = missionRepository.findById(id);

        MissionDto missionDto= Optional.of(MissionDto.fromEntity(missionOp.get()))
                .orElseThrow(
                        () -> new EntityNotFoundException("Aucune Mission avec l'id :"
                                + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.MISSION_NOT_FOUND)
                );

        setCreatedByUser(missionDto);
        return missionDto;
    }

    @Override
    public List<MissionDto> findAllByDate(Instant dateMission) {
        CheckParmasUtil.checkDate(dateMission,
                "ERREUR: Le parametre date_mission est null",
                "Impossible de trouver la liste des missions par date  avec une date null",
                ErrorCodes.MISSION_NOT_VALID_OPERATION);


        return missionRepository.findAllByDateDebut(dateMission).stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MissionDto> findAll() {
        return missionRepository.findAll().stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "l'ID Mission  à supprimer est null",
                "Impossible de supprimer une Mission  avec un ID null", ErrorCodes.MISSION_NOT_VALID_OPERATION);
        Mission mission = missionRepository.findById(id).get();
        if (id != null && MissionDto.fromEntity(mission).isMissionAccomplished()) {
            throw new InvalidOperationException("Impossible de Supprimer la mission lorsqu'elle est accompli",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        // First, delete related records in the mission_vehicle table

        etapeRepository.deleteByMissionId(id);

        List<MissionVehicleDto> missionVehicleDtos = new ArrayList<>();
        missionVehicleDtos= missionVehicleService.findByMissionId(id);
        for (MissionVehicleDto missionVehicleDto : missionVehicleDtos) {
                chauffeurVehicleService.deleteByMissionVehicleId(missionVehicleDto.getId());
        }
        missionVehicleRepository.deleteByMissionId(id);
        missionRepository.deleteById(id);
    }

    @Override
    public PageResponse<MissionDto> findAllMissions(int page, int size) {

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Mission> missions = missionRepository.findAllMissions(pageable);

        List<MissionDto> missionsDto = missions.stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                missionsDto,
                missions.getNumber(),
                missions.getSize(),
                missions.getTotalElements(),
                missions.getTotalPages(),
                missions.isFirst(),
                missions.isLast()
        );
    }

    @Override
    public PageResponse<MissionDto> findAllMissionsByDriverId(int page, int size, Authentication connectedUser) {
            Utilisateur user = ((Utilisateur) connectedUser.getPrincipal());
            Pageable pageable = PageRequest.of(page,size,Sort.by("createdAt").descending());
            Page<Mission> missions = missionRepository.findAll(MissionSpecification.withChauffeurId(user.getId()),pageable);

        List<MissionDto> missionsDto = missions.stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                missionsDto,
                missions.getNumber(),
                missions.getSize(),
                missions.getTotalElements(),
                missions.getTotalPages(),
                missions.isFirst(),
                missions.isLast()
        );
    }

    private void checkDto(MissionDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = MissionValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error(msgLog, dto);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }


    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(MissionDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private MissionDto updatedResponse(Mission mission) {
        MissionDto mDto=MissionDto.fromEntity(mission);
        setCreatedByUser(mDto);
        return mDto;
    }
}
