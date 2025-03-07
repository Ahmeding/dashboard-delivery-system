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
import com.hitecart.deliverysystem.services.*;
import com.hitecart.deliverysystem.services.IChauffeurVehicleService;
import com.hitecart.deliverysystem.specification.MissionSpecification;
import com.hitecart.deliverysystem.validator.MissionValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.View;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
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
    private final IDeliveryDestinationService deliveryDestinationService;

   private List<Integer> extractVehicleIds(MissionDto dto){
       if (dto.getMissionVehiclesDtos() == null) {
           return Collections.emptyList();
       }
       return dto.getMissionVehiclesDtos().stream()
               .map(MissionVehicleDto::getVehicleDto)
               .map(VehiculeDto::getId)
               .filter(Objects::nonNull)
               .distinct()
               .collect(Collectors.toList());
   }
    private List<Integer> extractDriverIds(MissionDto dto){
        return dto.getMissionVehiclesDtos().stream()
                .flatMap(mv->mv.getChauffeursVehicleDto().stream())
                .map(ChauffeurVehicleDto::getChauffeurDto)
                .map(ChauffeurDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
   }

    private void updateVehicleAndDriverStatus(MissionDto dto,boolean isAssigned){
        List<Integer> vehicleIds = extractVehicleIds(dto);
        List<Integer> driverIds = extractDriverIds(dto);

        vehiculeRepository.bulkUpdateAssignmentStatus(vehicleIds, isAssigned);
        chauffeurRepository.bulkUpdateAssignmentStatus(driverIds, isAssigned);
    }

    private void createMissionRelationships(MissionDto dto, Mission savedMission){

       List<MissionVehicle> missionVehicles = new ArrayList<>();
       List<ChauffeurVehicle> chauffeurVehicles  = new ArrayList<>();
       List<Etape> etapes = new ArrayList<>();


            dto.getEtapeDtos().forEach(etp -> {
                Etape etape = createEtape(etp, savedMission);
                etapes.add(etape);
            });


            dto.getMissionVehiclesDtos().parallelStream().forEach(mvDto -> {
                MissionVehicle missionVehicle = createMissionVehicle(mvDto, savedMission);
                missionVehicles.add(missionVehicle);

                if(!mvDto.getChauffeursVehicleDto().isEmpty() && mvDto.getChauffeursVehicleDto().size()<=2 ){
                    mvDto.getChauffeursVehicleDto().parallelStream().forEach(cvDto -> {
                        chauffeurVehicles.add(createChauffeurVehicle(cvDto, missionVehicle));
                    });
                }
            });
            missionVehicleRepository.saveAll(missionVehicles);
            List<ChauffeurVehicle> savedChauffeurVehicles =
                    chauffeurVehicleRepository.saveAll(chauffeurVehicles);
            etapeRepository.saveAll(etapes);
            sendNotificationsAsync(savedChauffeurVehicles, savedMission.getId());

    }

    private Etape createEtape(EtapeDto etp, Mission savedMission) {
       Etape etape = EtapeDto.toEntity(etp);
       etape.setMission(savedMission);
       return etape;
    }

    @Async
    protected void sendNotificationsAsync(List<ChauffeurVehicle> chauffeurVehicles, Integer missionId) {
        chauffeurVehicles.parallelStream().forEach(cv -> {
            notificationService.sendNotification(
                    cv.getChauffeur().getId(),
                    Notification.builder()
                            .title("New Mission Assignment")
                            .message("You've been assigned to mission #" + missionId)
                            .status(NotificationStatus.SEND)
                            .build()
            );
        });
    }

    private ChauffeurVehicle createChauffeurVehicle(ChauffeurVehicleDto cvDto, MissionVehicle missionVehicle) {
        return ChauffeurVehicle.builder()
                .missionVehicle(missionVehicle)
                .chauffeur(chauffeurRepository.getReferenceById(cvDto.getChauffeurDto().getId()))
                .assignedAt(LocalDateTime.now())
                .build();
   }

    private MissionVehicle createMissionVehicle(MissionVehicleDto mvDto, Mission savedMission) {
        return MissionVehicle.builder()
                .mission(savedMission)
                .vehicule(vehiculeRepository.getReferenceById(mvDto.getVehicleDto().getId()))
                .assignedAt(LocalDateTime.now())
                .build();
    }

    private void checkMissionAccomplishedStatus(MissionDto dto) {
        if (dto.getId() != null && dto.isMissionAccomplished()) {
            throw new InvalidOperationException("Cannot modify an accomplished mission",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
    }

    private void validateMissionDto(MissionDto dto){
        if (dto == null) {
            throw new InvalidOperationException("Mission DTO cannot be null",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        if (CollectionUtils.isEmpty(dto.getEtapeDtos())) {
            throw new InvalidOperationException("At least one Delivery Point must be specified",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }
        if (CollectionUtils.isEmpty(dto.getMissionVehiclesDtos())) {
            throw new InvalidOperationException("At least one vehicle must be specified",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }

        dto.getMissionVehiclesDtos().forEach(mv -> {
            if (CollectionUtils.isEmpty(mv.getChauffeursVehicleDto())) {
                throw new InvalidOperationException("Each vehicle must have at least one driver",
                        ErrorCodes.MISSION_NOT_VALID_OPERATION);
            }
        });
    }

    private void checkOverlappingMissions(MissionDto dto) {
        List<Integer> vehicleIds = extractVehicleIds(dto);
        List<Integer> driverIds = extractDriverIds(dto);

        if (!missionRepository.findConflictingMissions(
                dto.getDateDebut(),
                dto.getDateFin(),
                vehicleIds,
                driverIds).isEmpty()) {
            throw new InvalidOperationException("Vehicles/drivers have overlapping missions",
                    ErrorCodes.MISSION_OVERLAP);
        }
    }

    private Mission createAndSaveMission(MissionDto dto) {
        DeliveryDestinationDto destination = deliveryDestinationService.
                findById(dto.getIdDeliveryDestination());

        Mission mission = MissionDto.toEntity(dto);
        mission.setEtatMission(EtatMission.EN_COURS);
        mission.setDeliveryDestination(DeliveryDestinationDto.toEntity(destination));
        return missionRepository.save(mission);
    }

    @Transactional
    @Override
    public MissionDto save(MissionDto dto) {
        // Validate DTO Early
        validateMissionDto(dto);
        checkMissionAccomplishedStatus(dto);
        checkDto(dto, "Mission Not valid",
                "Mission Not Valide", ErrorCodes.MISSION_NOT_VALID_OPERATION);
        // Check availability (vehicles/drivers + dates)
        checkOverlappingMissions(dto);

        // Save mission
        Mission missionToSave = createAndSaveMission(dto);
        missionToSave.setEtatMission(EtatMission.EN_COURS);
        // batch update vehicule/ drivers
        updateVehicleAndDriverStatus(dto,true);
        // Batch create relationships
        createMissionRelationships(dto, missionToSave);
       // save mission-vehicle history and chauffeur_vehicle

/*        if (!dto.getMissionVehiclesDtos().isEmpty()) {
            for (MissionVehicleDto mvehiculeDto : dto.getMissionVehiclesDtos()) {

                    MissionVehicle savedMissionVehicle = new MissionVehicle();
                    final VehiculeDto vehiculeDto = checkVehicle(VehiculeDto.toEntity(mvehiculeDto.getVehicleDto()));

                    vehiculeDto.setAssigned(true);
                    vehiculeRepository.save(VehiculeDto.toEntity(vehiculeDto));
                    savedMissionVehicle.setVehicule(VehiculeDto.toEntity(vehiculeDto));
                    savedMissionVehicle.setMission(savedMission);
                    savedMissionVehicle.setAssignedAt(LocalDateTime.now());
                    missionVehicleRepository.save(savedMissionVehicle);

                    if(!mvehiculeDto.getChauffeursVehicleDto().isEmpty()){
                        for(ChauffeurVehicleDto chauffeurByVehicleDto  : mvehiculeDto.getChauffeursVehicleDto()){

                            ChauffeurVehicle chauffeurVehicleToSave = new ChauffeurVehicle();
                            ChauffeurDto chauffeurDto = checkDriver(ChauffeurDto.toEntity(chauffeurByVehicleDto.getChauffeurDto()));
                            chauffeurDto.setAssigned(true);
                            chauffeurRepository.save(ChauffeurDto.toEntity(chauffeurDto));
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
                    }else{
                        throw new InvalidOperationException("At least one driver  must be specified when creating a mission!",
                                ErrorCodes.MISSION_NOT_VALID_OPERATION);
                    }
            }
        }else{
            throw new InvalidOperationException("At least one Vehicle with a driver(s) must be specified when creating a mission!",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION);
        }*/


        return MissionDto.fromEntity(missionToSave);

    }

    private ChauffeurDto checkDriver(Chauffeur driver) {
    return ChauffeurDto.fromEntity( chauffeurRepository.findById(driver.getId()).orElseThrow(()->
            new EntityNotFoundException("Driver not found with id: " + driver.getId(),
                    ErrorCodes.CHAUFFEUR_NOT_FOUND)));
    }

    private VehiculeDto checkVehicle(Vehicule vehicule) {
        return VehiculeDto.fromEntity(vehiculeRepository.findById(vehicule.getId()).orElseThrow(()->
               new EntityNotFoundException("Vehicle not found with id: " + vehicule.getId(),
                       ErrorCodes.VEHICULE_NOT_FOUND)));

    }

    private void checkOverlapping(MissionDto dto) {
        checkOverlappingVehicles(dto);
        checkOverlappingDrivers(dto);
    }


    @Override
    public List<Integer> checkOverlappingVehicles(MissionDto dto) {
        List<Integer> vehiclesIds= dto.getMissionVehiclesDtos().stream()
                .map(dtoMV->dtoMV.getVehicleDto().getId())
                .collect(Collectors.toList());

        log.error(vehiclesIds.size()+" vehiclesIds");
        List<Integer> unavailableVehiclesIds = missionVehicleService.findUnavailableVehicles(vehiclesIds,dto.getDateDebut(),dto.getDateFin());
        log.error(unavailableVehiclesIds.size()+" unavailableVehiclesIds");

/*        if(!unavailableVehiclesIds.isEmpty()){
            List<String> errorsUnavailableVs = unavailableVehiclesIds.stream().map(String::valueOf).toList();

            throw new InvalidOperationException("Some vehicles are already assigned to another mission.",
                    ErrorCodes.MISSION_NOT_VALID_OPERATION,errorsUnavailableVs);
        }*/
        return unavailableVehiclesIds;
    }
    @Override
    public List<Integer> checkOverlappingDrivers(MissionDto dto) {

        List<Integer> driverIds= dto.getMissionVehiclesDtos()
                .stream().flatMap(dtoMV->dtoMV.getChauffeursVehicleDto().stream())
                .map(cvDto->cvDto.getChauffeurDto().getId())
                .collect(Collectors.toList());
            log.error( driverIds.size()+" driverIds");
        List<Integer> unavailableDriverIds =
                chauffeurVehicleService.findUnavailableDrivers(driverIds,dto.getDateDebut(),dto.getDateFin());
            log.error( unavailableDriverIds.size()+" unavailableDriverIds");

       return unavailableDriverIds;
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
    public List<MissionResponse> findAll() {
        return missionRepository.findAll().stream()
                //.map(MissionDto::fromEntity)
                .map(this::updatedMissionResponse)
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
    }    private void setCreatedByUserResponse(MissionResponse response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private MissionDto updatedResponse(Mission mission) {
        MissionDto mDto=MissionDto.fromEntity(mission);
        setCreatedByUser(mDto);
        return mDto;
    }
    private MissionResponse updatedMissionResponse(Mission mission) {
        MissionResponse mDto=MissionResponse.fromEntity(mission);
        setCreatedByUserResponse(mDto);
        return mDto;
    }
}
