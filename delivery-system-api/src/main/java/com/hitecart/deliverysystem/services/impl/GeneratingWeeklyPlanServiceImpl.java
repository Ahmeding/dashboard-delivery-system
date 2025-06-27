package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.DeliveryPointRepository;
import com.hitecart.deliverysystem.dao.MissionRepository;
import com.hitecart.deliverysystem.dao.StopPointRepository;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import com.hitecart.deliverysystem.model.StopPoint;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import com.hitecart.deliverysystem.services.AutoGeneratingWeeklyPlanningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Service implementation for automatically generating weekly delivery missions.
 * This service processes enabled delivery points and creates missions based on their recurrence patterns.
 * Supports daily, weekly, biweekly, and monthly recurrence intervals.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GeneratingWeeklyPlanServiceImpl implements AutoGeneratingWeeklyPlanningService {

    private final DeliveryPointRepository deliveryPointRepository;
    private final MissionRepository missionRepository;
    private final StopPointRepository stopPointRepository;

    // Mission timing constants
    private static final int MISSION_START_HOUR = 18;
    private static final int MISSION_START_MINUTE = 0;
    private static final int MISSION_END_HOUR = 14;
    private static final int MISSION_END_MINUTE = 0;
    private static final BigDecimal DEFAULT_MISSION_VOLUME = new BigDecimal(10);

    /**
     * Scheduled job that runs daily at 2 AM to generate missions for the upcoming period.
     * Processes all enabled delivery points and creates missions based on their recurrence patterns.
     *
     * @return List of mission IDs that were processed (both new and existing)
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    @Override
    public List<Integer> generateMissions() {
        log.info("Starting automatic mission generation process");

        try {
            Set<Integer> processedMissionIds = new HashSet<>();
            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear();

            List<DeliveryPoint> enabledPoints = deliveryPointRepository.findAllByEnabled(true);
            log.info("Processing {} enabled delivery points", enabledPoints.size());

            for (DeliveryPoint point : enabledPoints) {
                try {
                    processedMissionIds.addAll(processDeliveryPoint(point, currentDate, currentYear));
                } catch (Exception e) {
                    log.error("Error processing delivery point {}: {}", point.getId(), e.getMessage(), e);
                    // Continue processing other points even if one fails
                }
            }

            log.info("Mission generation completed. Processed {} missions", processedMissionIds.size());
            return new ArrayList<>(processedMissionIds);

        } catch (Exception e) {
            log.error("Critical error during mission generation: {}", e.getMessage(), e);
            throw new RuntimeException("Mission generation failed", e);
        }
    }

    /**
     * Processes a single delivery point and generates missions based on its recurrence pattern.
     *
     * @param point The delivery point to process
     * @param currentDate Current date for calculations
     * @param currentYear Current year for end date calculations
     * @return Set of mission IDs that were processed for this point
     */
    private Set<Integer> processDeliveryPoint(DeliveryPoint point, LocalDate currentDate, int currentYear) {
        Set<Integer> missionIds = new HashSet<>();

        RecurrencePattern pattern = point.getRecurrencePattern();
        if (pattern == null) {
            log.debug("Skipping delivery point {} - no recurrence pattern defined", point.getId());
            return missionIds;
        }

        // Determine date range for generation
        LocalDate startDate = pattern.getStartDate().isAfter(currentDate) ?
                pattern.getStartDate() : currentDate;
        LocalDate endDate = pattern.getEndDate() != null ?
                pattern.getEndDate() :
                LocalDate.of(currentYear, 12, 31);

        // Validate pattern configuration
        Set<DayOfWeek> patternDaysOfWeek = pattern.getDaysOfWeek();
        if (patternDaysOfWeek.isEmpty()) {
            log.warn("Delivery point {} has empty days of week configuration", point.getId());
            return missionIds;
        }

        if (!endDate.isAfter(currentDate)) {
            log.debug("Skipping delivery point {} - end date {} is not in the future", point.getId(), endDate);
            return missionIds;
        }

        log.debug("Generating missions for delivery point {} from {} to {}", point.getId(), startDate, endDate);
        missionIds.addAll(generateMissionsForPoint(point, startDate, endDate));

        return missionIds;
    }

    /**
     * Generates missions for a specific delivery point within the given date range.
     * Iterates through each date and creates missions for dates that match the recurrence pattern.
     *
     * @param point The delivery point to generate missions for
     * @param startDate Start date for generation
     * @param endDate End date for generation
     * @return List of mission IDs generated for this point
     */
    private List<Integer> generateMissionsForPoint(DeliveryPoint point, LocalDate startDate, LocalDate endDate) {
        List<Integer> generatedMissionIds = new ArrayList<>();
        log.debug("Generating missions for delivery point {} in date range {} to {}",
                point.getId(), startDate, endDate);

        // Process each date in the range
        startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
            try {
                if (matchesRecurrencePattern(point, date)) {
                    log.debug("Date {} matches recurrence pattern for delivery point {}", date, point.getId());
                    Integer missionId = createMissionIfNeeded(point, date);
                    if (missionId != null) {
                        generatedMissionIds.add(missionId);
                    }
                }
            } catch (Exception e) {
                log.error("Error processing date {} for delivery point {}: {}",
                        date, point.getId(), e.getMessage(), e);
            }
        });

        log.debug("Generated {} missions for delivery point {}", generatedMissionIds.size(), point.getId());
        return generatedMissionIds;
    }

    /**
     * Determines if a given date matches the delivery point's recurrence pattern.
     * Validates date range, day of week, and recurrence interval.
     *
     * @param point The delivery point with recurrence pattern
     * @param date The date to check against the pattern
     * @return true if the date matches the pattern, false otherwise
     */
    private boolean matchesRecurrencePattern(DeliveryPoint point, LocalDate date) {
        RecurrencePattern pattern = point.getRecurrencePattern();

        // Validate date is within pattern bounds
        if (date.isBefore(pattern.getStartDate()) ||
                (pattern.getEndDate() != null && date.isAfter(pattern.getEndDate()))) {
            log.trace("Date {} is outside pattern bounds for delivery point {}", date, point.getId());
            return false;
        }

        // Check if date matches configured days of week
        if (!pattern.getDaysOfWeek().contains(date.getDayOfWeek())) {
            log.trace("Date {} day of week doesn't match pattern for delivery point {}", date, point.getId());
            return false;
        }

        // Check recurrence interval
        boolean matches = switch (pattern.getRecurrenceInterval()) {
            case DAILY -> true; // Every day matching daysOfWeek
            case WEEKLY -> ChronoUnit.WEEKS.between(pattern.getStartDate(), date) % 1 == 0;
            case BIWEEKLY -> ChronoUnit.WEEKS.between(pattern.getStartDate(), date) % 2 == 0;
            case MONTHLY -> matchesMonthlyPattern(pattern, date);
        };

        log.trace("Date {} {} recurrence pattern for delivery point {}",
                date, matches ? "matches" : "does not match", point.getId());
        return matches;
    }

    /**
     * Checks if a date matches the monthly recurrence pattern.
     * Ensures the date falls on the same week of the month as the pattern start date.
     *
     * @param pattern The recurrence pattern
     * @param date The date to check
     * @return true if the date matches the monthly pattern
     */
    private boolean matchesMonthlyPattern(RecurrencePattern pattern, LocalDate date) {
        int targetWeek = getWeekOfMonth(pattern.getStartDate());
        LocalDate nthDay = date.withDayOfMonth(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(targetWeek, date.getDayOfWeek()));
        return nthDay.equals(date);
    }

    /**
     * Calculates which week of the month a date falls in (1-5).
     *
     * @param date The date to calculate the week for
     * @return The week number (1-5)
     */
    private int getWeekOfMonth(LocalDate date) {
        return ((date.getDayOfMonth() - 1) / 7) + 1;
    }

    /**
     * Creates a new Mission entity with the specified parameters.
     *
     * @param deliveryPoint The delivery point for the mission
     * @param missionStart The mission start date and time
     * @param missionEnd The mission end date and time
     * @return A new Mission entity (not yet persisted)
     */
    private Mission createNewMission(DeliveryPoint deliveryPoint, LocalDateTime missionStart, LocalDateTime missionEnd) {
        return Mission.builder()
                .title("Mission " + deliveryPoint.getAddress().getCity())
                .deliveryDestination(deliveryPoint.getDeliveryDestination())
                .startDate(missionStart)
                .endDate(missionEnd)
                .volume(DEFAULT_MISSION_VOLUME)
                .missionStatus(MissionStatus.PLANNED)
                .build();
    }

    /**
     * Creates and persists a new StopPoint entity associated with the mission.
     *
     * @param mission The parent mission
     * @param deliveryPoint The delivery point information
     * @param missionStart The mission start date and time
     * @param missionEnd The mission end date and time
     */
    private void createNewStopPoint(Mission mission, DeliveryPoint deliveryPoint,
                                    LocalDateTime missionStart, LocalDateTime missionEnd) {
        StopPoint stopPoint = StopPoint.builder()
                .mission(mission)
                .title(deliveryPoint.getName() + " " + deliveryPoint.getAddress().getCity())
                .comment(buildStopPointComment(deliveryPoint, missionStart))
                .departureDate(missionStart.toLocalDate())
                .departureLocation(deliveryPoint.getDeliveryDestination().getDeliveryService().getPickUpLocation())
                .arrivalDate(missionEnd.toLocalDate())
                .scheduledDeliveryTime(deliveryPoint.getSchedule())
                .arrivalLocation(deliveryPoint.getName() + "-" + deliveryPoint.getAddress().getCity())
                .build();

        stopPointRepository.save(stopPoint);
        log.debug("Created stop point for mission {} and delivery point {}", mission.getId(), deliveryPoint.getId());
    }

    /**
     * Builds a descriptive comment for the stop point.
     *
     * @param deliveryPoint The delivery point
     * @param missionStart The mission start time
     * @return A formatted comment string
     */
    private String buildStopPointComment(DeliveryPoint deliveryPoint, LocalDateTime missionStart) {
        return deliveryPoint.getDeliveryDestination().getDeliveryService().getDestinationCity() + " - " +
                " start on " + missionStart +
                " will be delivered at " + deliveryPoint.getSchedule();
    }

    /**
     * Creates a mission if one doesn't already exist for the given delivery point and date.
     * If a mission already exists, returns its ID. If not, creates a new mission with associated stop point.
     *
     * @param deliveryPoint The delivery point to create a mission for
     * @param date The date for the mission
     * @return The mission ID (either existing or newly created), or null if creation failed
     */
    private Integer createMissionIfNeeded(DeliveryPoint deliveryPoint, LocalDate date) {
        log.debug("Checking if mission needed for delivery point {} on date {}", deliveryPoint.getId(), date);

        try {
            // Calculate mission timing
            LocalDateTime missionStart = date.atTime(MISSION_START_HOUR, MISSION_START_MINUTE);
            LocalDateTime missionEnd = date.plusDays(1).atTime(MISSION_END_HOUR, MISSION_END_MINUTE);

            // Check for existing mission
            Optional<Mission> existingMission = missionRepository.findByDeliveryDestinationAndTimeRange(
                    deliveryPoint.getDeliveryDestination(),
                    missionStart,
                    missionEnd);

            if (existingMission.isPresent()) {
                log.debug("Found existing mission {} for delivery point {} on date {}",
                        existingMission.get().getId(), deliveryPoint.getId(), date);
                return existingMission.get().getId();
            } else {
                log.debug("Creating new mission for delivery point {} on date {}", deliveryPoint.getId(), date);
                return createAndSaveNewMission(deliveryPoint, missionStart, missionEnd);
            }
        } catch (Exception e) {
            log.error("Failed to create mission for delivery point {} on date {}: {}",
                    deliveryPoint.getId(), date, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Creates and saves a new mission with its associated stop point.
     * Ensures proper order of operations for data integrity.
     *
     * @param deliveryPoint The delivery point
     * @param missionStart The mission start time
     * @param missionEnd The mission end time
     * @return The ID of the newly created mission
     */
    private Integer createAndSaveNewMission(DeliveryPoint deliveryPoint,
                                            LocalDateTime missionStart,
                                            LocalDateTime missionEnd) {
        // Create mission entity
        Mission missionToSave = createNewMission(deliveryPoint, missionStart, missionEnd);

        // Save mission first to get ID
        Mission savedMission = missionRepository.save(missionToSave);
        log.debug("Saved new mission with ID {}", savedMission.getId());

        // Create stop point with saved mission
        createNewStopPoint(savedMission, deliveryPoint, missionStart, missionEnd);

        return savedMission.getId();
    }
}
