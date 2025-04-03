package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.*;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;
import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.services.*;
import com.hitecart.deliverysystem.services.IChauffeurVehicleService;
import com.hitecart.deliverysystem.specification.MissionSpecification;
import com.hitecart.deliverysystem.utils.AssignmentResult;
import com.hitecart.deliverysystem.utils.TimeSlot;
import com.hitecart.deliverysystem.validator.MissionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.View;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MissionServiceImpl implements MissionService {

    private final IChauffeurVehicleService chauffeurVehicleService;
    private final AvailabilityManager availabilityManager;
    private final IMissionVehicleService missionVehicleService;
    private final IChauffeurVehicleRepository chauffeurVehicleRepository;
    private final MissionVehicleRepository missionVehicleRepository;
    private final MissionRepository missionRepository;
    private final EtapeRepository etapeRepository;
    private final INotificationService notificationService;
    private final IChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final View error;
    private final UtilisateurService utilisateurServiceImpl;
    private final IDeliveryDestinationService deliveryDestinationService;
    private final DeliveryPointRepository deliveryPointRepository;


    // =============================
    //
    // =============================


    @Transactional
    public void generateWeeklyAssignments() {
        // 1. Define the week (Monday to Sunday)
        LocalDateTime weekStart = LocalDateTime.now().with(DayOfWeek.SUNDAY).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime weekEnd = weekStart.plusDays(6);

        // 2. Fetch missions for the week
        List<Mission> missions = missionRepository.findMissionsWithVehiclesAndDrivers(weekStart, weekEnd);
        if (missions.isEmpty()) {
            log.info("No missions this week!");
            return;
        }

        // 3. Sort missions by longest duration first
        missions.sort(Comparator.comparingLong(
                (Mission m) -> Duration.between( m.getDateDebut(), m.getDateFin()).toMinutes()
        ).reversed());

        // 4. Fetch all drivers and vehicles
        List<Chauffeur> drivers = chauffeurRepository.findAll();
        List<Vehicule> vehicles = vehiculeRepository.findAll();

        // 5. Initialize availability and mission counters
        AvailabilityManager availabilityManager = new AvailabilityManager(chauffeurVehicleRepository, missionVehicleRepository);
        //Map<Chauffeur, List<TimeSlot>> driverAvailability = availabilityManager.initializeDriverAvailability(drivers, weekStart, weekEnd);
        Map<Vehicule, List<TimeSlot>> vehicleAvailability = availabilityManager.initializeVehicleAvailability(vehicles, weekStart, weekEnd);

        // 6. Initialize mission counter for each driver (NEW)
        Map<Chauffeur, Integer> chauffeurMissionCount = drivers.stream()
                .collect(Collectors.toMap(driver -> driver, driver -> 0));

        // 7. Shuffle drivers weekly for fairness
        long weekNumber = ChronoUnit.WEEKS.between(LocalDate.of(2023, 1, 1).atStartOfDay(), weekStart);
         Collections.shuffle(drivers, new Random(weekNumber));

        // 8. Prepare assignment tracking
        List<AssignmentResult> assignments = new ArrayList<>();
        List<Mission> unassignedMissions = new ArrayList<>();

        for (Mission mission : missions) {
            TimeSlot missionSlot = new TimeSlot(mission.getDateDebut(),
                    mission.getDateFin());


            // 9. Find suitable vehicle
            Map<Vehicule, Integer> vehicleAssignmentCounts = new HashMap<>(); // Track weekly assignments per vehicle
            Vehicule vehicle = findSuitableVehicle(mission, vehicleAvailability,
                    vehicleAssignmentCounts);
      // 10. Find available driver with fewest missions (UPDATED)
            //Chauffeur driver = findAvailableDriver(missionSlot, driverAvailability, chauffeurMissionCount);
      /*     if (vehicle == null || driver == null) {
                unassignedMissions.add(mission);
                continue;
            }*/
            // 11. Create and track assignment
           // AssignmentResult assignment = createAssignment(mission, vehicle, driver);
            //assignments.add(assignment);

            // 12. Update availability and mission count (NEW)
          /*  updateTimeSlots(vehicleAvailability, vehicle, missionSlot);
            updateTimeSlots(driverAvailability, driver, missionSlot);
            chauffeurMissionCount.put(driver, chauffeurMissionCount.get(driver) + 1);*/ // Increment count
        }


        // 13. Save and log results
        saveAssignmentsInBatch(assignments);
        logResults(assignments, unassignedMissions);
    }
    private AssignmentResult createAssignment(Mission mission, Vehicule vehicle, List<Chauffeur> drivers) {
        // Build a temporary "AssignmentResult" object to group mission, vehicle, and driver
        return new AssignmentResult(mission, vehicle, drivers);
    }
    private void saveAssignmentsInBatch(List<AssignmentResult> assignments) {
        // Convert AssignmentResult objects to database entities
        List<MissionVehicle> missionVehicles = new ArrayList<>();
        List<ChauffeurVehicle> chauffeurVehicles = new ArrayList<>();

        for (AssignmentResult assignment : assignments) {
            // Create MissionVehicle link (mission + vehicle)
            MissionVehicle mv = MissionVehicle.builder()
                    .mission(assignment.getMission())
                    .vehicule(assignment.getVehicle())
                    .assignedAt(LocalDateTime.now())
                    .build();
            missionVehicles.add(mv);

            // Create ChauffeurVehicle link (driver + mission)
            ChauffeurVehicle cv = ChauffeurVehicle.builder()
                    .missionVehicle(mv)  // Link to the MissionVehicle
                    //.chauffeur(assignment.getDrivers())
                    .assignedAt(LocalDateTime.now())
                    .build();
            chauffeurVehicles.add(cv);
        }

        // Batch save to database
        missionVehicleRepository.saveAll(missionVehicles);
        chauffeurVehicleRepository.saveAll(chauffeurVehicles);
    }
    private void logResults(List<AssignmentResult> assignments, List<Mission> unassignedMissions) {
        // Log success stats
        log.info("Successfully assigned {} missions:", assignments.size());
        assignments.forEach(assignment ->
                log.debug("Mission ID {}: Driver {}, Vehicle {}",
                        assignment.getMission().getId(),
                        assignment.getDrivers().size(),
                        assignment.getVehicle().getId())
        );

        // Log failures
        if (!unassignedMissions.isEmpty()) {
            log.warn("Could not assign {} missions:", unassignedMissions.size());
            unassignedMissions.forEach(mission ->
                    log.warn("Unassigned Mission ID {} ({} to {})",
                            mission.getId(),
                            mission.getDateDebut(),
                            mission.getDateFin())
            );
        }
    }
    // Updated driver selection method
    private Chauffeur findAvailableDriver(TimeSlot slot,
                                          Map<Chauffeur, List<TimeSlot>> availability,
                                          Map<Chauffeur, Integer> missionCounts) {
        return availability.entrySet().stream()
                .filter(entry -> entry.getValue().stream()
                        .anyMatch(available -> available.covers(slot))
                )
                .min(Comparator.comparingInt(
                        entry -> missionCounts.getOrDefault(entry.getKey(), 0)
                ))  // Prefer driver with least missions
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    // Improved vehicle selection logic
    private Vehicule findSuitableVehicle(Mission mission,
                                         Map<Vehicule, List<TimeSlot>> availability,
                                         Map<Vehicule, Integer> assignmentCounts) {
        // Create a TimeSlot for the mission
        TimeSlot missionSlot = new TimeSlot(mission.getDateDebut(), mission.getDateFin());
        return availability.entrySet().stream()
                .filter(entry -> {
                    boolean hasCapacity = entry.getKey().getVolume().compareTo(mission.getVolume()) >= 0;
                    boolean isAvailable = entry.getValue().stream()
                            .anyMatch(slot -> slot.covers(missionSlot));
                    return hasCapacity && isAvailable;
                })
                .min(Comparator.comparing(
                        e -> e.getKey().getVolume(),  // Prefer smallest vehicle
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    // Type-safe availability update
    private <T> void updateTimeSlots(Map<T, List<TimeSlot>> availability, T resource, TimeSlot bookedSlot) {
        availability.computeIfPresent(resource, (k, slots) ->
                slots.stream()
                        .flatMap(slot -> slot.subtract(bookedSlot).stream())
                        .collect(Collectors.toList())
        );
    }
    private void rotateDrivers(List<Chauffeur> drivers, LocalDateTime weekStart) {
        long weekNumber = ChronoUnit.WEEKS.between(LocalDateTime.of(2023, 1, 1, 0, 0), weekStart);
        Collections.rotate(drivers, (int) (weekNumber % drivers.size()));
    }
    private Vehicule assignVehicle(Mission mission, Map<Vehicule, List<TimeSlot>> availability) {
        TimeSlot missionSlot = new TimeSlot(mission.getDateDebut(), mission.getDateFin());
        return availability.entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(slot -> slot.covers(missionSlot)))
                .map(Map.Entry::getKey)
                .filter(v -> v.getVolume().compareTo(mission.getVolume()) >= 0)
                .min(Comparator.comparing(Vehicule::getVolume)) // Smallest suitable vehicle
                .orElse(null);
    }
    private Chauffeur assignDriver(Map<Chauffeur, List<TimeSlot>> availability,
                                   Map<Chauffeur, Integer> missionCounts,
                                   TimeSlot missionSlot) {
        return availability.entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(slot -> slot.covers(missionSlot)))
                .map(Map.Entry::getKey)
                .min(Comparator.comparing(missionCounts::get)) // Driver with fewest missions
                .orElse(null);
    }


    private void updateAssignmentStatus(List<MissionVehicle> mvs, List<ChauffeurVehicle> cvs, boolean isAssigned) {
        List<Integer> vehicleIds = mvs.stream().map(mv -> mv.getVehicule().getId()).collect(Collectors.toList());
        List<Integer> driverIds = cvs.stream().map(cv -> cv.getChauffeur().getId()).collect(Collectors.toList());
        vehiculeRepository.bulkUpdateAssignmentStatus(vehicleIds, isAssigned);
        chauffeurRepository.bulkUpdateAssignmentStatus(driverIds, isAssigned);
    }
    // =============================
    //
    // =============================

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void generateMissions() {
        log.info("Created mission -generateMissions-" );

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        deliveryPointRepository.findAllByEnabled(true).forEach(point -> {

            RecurrencePattern pattern = point.getRecurrencePattern();
           if(pattern !=null){
               // Determine date range for generation
               LocalDate startDate = pattern.getStartDate().isAfter(currentDate)?
                       pattern.getStartDate() : currentDate;
               LocalDate endDate = pattern.getEndDate()!=null?
                       pattern.getEndDate() :
                       LocalDate.of(currentYear,12,31);
               // only generate if end date is in the future
               Set<DayOfWeek> patternDayOfWeek = point.getRecurrencePattern().getDaysOfWeek();
               if (!patternDayOfWeek.isEmpty() && endDate.isAfter(currentDate)) {
                   generateForPoint(point, startDate, endDate);
               }
           }
           });

    }

    private void generateForPoint(DeliveryPoint point, LocalDate start, LocalDate end) {
        log.error("Created mission -generateForPoint-" );
        LocalDate patternStart= point.getRecurrencePattern().getStartDate();
        int targetWeek = getWeekOfMonth(patternStart);

        start.datesUntil(end.plusDays(1)).forEach(date -> {
            if(matchesRecurrencePattern(point, date)){
                log.error("Created mission -matchesRecurrencePattern(point, date)-" );
                createMissionIfNeeded(point, date);
            }
        });
    }
    private boolean matchesRecurrencePattern(DeliveryPoint point, LocalDate date) {
        RecurrencePattern pattern = point.getRecurrencePattern();

        // Basic date range validation
        if(date.isBefore(pattern.getStartDate())||
                (pattern.getEndDate()!=null &&
                        date.isAfter(pattern.getEndDate()))){
            log.error("Created mission -return false-" );
            return false;
        }

        // Must match at least one day of week
        if (!pattern.getDaysOfWeek().contains(date.getDayOfWeek())) {
            return false;
        }

        return switch (pattern.getRecurrenceInterval()) {
            // DAILY: Every day matching daysOfWeek (e.g. Mon/Wed/Fri)
            case DAILY -> true;
            // WEEKLY: Every week on these days
            case WEEKLY ->
                    ChronoUnit.WEEKS.between(pattern.getStartDate(), date) % 1 == 0;
            // BIWEEKLY: Every 2 weeks on these days
            case BIWEEKLY ->
                    ChronoUnit.WEEKS.between(pattern.getStartDate(), date) % 2 == 0;
            // MONTHLY: Same week of month as start date
            case MONTHLY -> {
                int targetWeek = getWeekOfMonth(pattern.getStartDate());
                LocalDate nthDay = date.withDayOfMonth(1)
                        .with(TemporalAdjusters.dayOfWeekInMonth(targetWeek
                        ,date.getDayOfWeek()));
                yield nthDay.equals(date);

/*                LocalDate patternStart = pattern.getStartDate();
                int targetWeek = ((patternStart.getDayOfMonth() - 1) / 7) + 1;
                LocalDate weekStart = date.withDayOfMonth(1)
                        .with(TemporalAdjusters.dayOfWeekInMonth(targetWeek, patternStart.getDayOfWeek()));
                yield date.isAfter(weekStart.minusDays(1)) &&
                        date.isBefore(weekStart.plusDays(7));*/
            }
        };

    }
    // Get the week of the month (1st, 2nd, 3rd, etc.)
    private int getWeekOfMonth(LocalDate date) {
        return ((date.getDayOfMonth() - 1) / 7) + 1;
    }

    // Get the first day of the target week in a given month
    private LocalDate getWeekStartInMonth(LocalDate baseDate, int targetWeek, DayOfWeek startDay) {
        return baseDate
                .withDayOfMonth(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(targetWeek, startDay));
    }

    private void createMissionIfNeeded(DeliveryPoint point, LocalDate date) {
        log.info("Created mission -createMissionIfNeeded-" );

        // Mission timing (18:00 start, next day 14:00 end)
        LocalDateTime missionStart = date.atTime(18, 0);
        LocalDateTime missionEnd = date.plusDays(1).atTime(14, 0);

        boolean exists = missionRepository.existsByDeliveryDestinationAndTimeRange(
                point.getDeliveryDestination(),
                missionStart,
                missionEnd);


        if (!exists) {

            Mission newMission = Mission.builder()
                    .titre("Mission " + point.getAddress().getVille())
                    .deliveryDestination(point.getDeliveryDestination())
                    .dateDebut(missionStart)
                    .dateFin(missionEnd)
                    .volume(new BigDecimal(10))
                    .etatMission(EtatMission.PLANNED)
                    .build();
            missionRepository.save(newMission);
            log.info("Created mission for {} on {}", point.getName(), date);
        }
    }


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
        mission.setEtatMission(EtatMission.PLANNED);
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
        missionToSave.setEtatMission(EtatMission.PLANNED);
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
