package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import com.hitecart.deliverysystem.repository.DeliveryPointRepository;
import com.hitecart.deliverysystem.repository.MissionRepository;
import com.hitecart.deliverysystem.services.MissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MissionServiceImplTest {
 /*   @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuditorAware<Integer> auditorAware;
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private DeliveryPointRepository deliveryPointRepository;

    @Autowired
    private MissionService missionService;



    // Helper method to create test delivery points
    private DeliveryPoint createDeliveryPoint(RecurrencePattern pattern) {
        DeliveryPoint point = DeliveryPoint.builder()
                .name("Test Point")
                .recurrencePattern(pattern)
                .enabled(true)
                .build();
        return testEntityManager.persistAndFlush(point);
    }

    // --- DAILY RECURRENCE TESTS ---
    @Test
    void testDailyRecurrence() {
        // Given: Daily pattern Mon/Wed/Fri from today-3 days to today+7 days
        RecurrencePattern pattern = RecurrencePattern.builder()
                .recurrenceInterval(RecurrencePattern.RecurrenceInterval.DAILY)
                .daysOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                .startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        DeliveryPoint point = createDeliveryPoint(pattern);

        // When
        missionService.generateMissions();

        // Then: Should create missions for all matching days in date range
        long expectedDays = 10; // 3 days/week * 2 weeks (approx)
        assertThat(missionRepository.count()).isBetween(expectedDays - 2, expectedDays + 2);
    }

    // --- WEEKLY RECURRENCE TESTS ---
    @Test
    void testWeeklyRecurrence() {
        // Given: Every Tuesday starting 2 weeks ago, ending next week
        RecurrencePattern pattern = RecurrencePattern.builder()
                .recurrenceInterval(RecurrencePattern.RecurrenceInterval.WEEKLY)
                .daysOfWeek(Set.of(DayOfWeek.TUESDAY))
                .startDate(LocalDate.now().minusWeeks(2))
                .endDate(LocalDate.now().plusWeeks(1))
                .build();

        DeliveryPoint point = createDeliveryPoint(pattern);

        // When
        missionService.generateMissions();

        // Then: Should create 3 missions (past 2 weeks + current + next)
        assertThat(missionRepository.count()).isEqualTo(4);
    }

    // --- BIWEEKLY RECURRENCE TESTS ---
    @Test
    void testBiweeklyRecurrence() {
        // Given: Every other Friday starting 4 weeks ago
        RecurrencePattern pattern = RecurrencePattern.builder()
                .recurrenceInterval(RecurrencePattern.RecurrenceInterval.BIWEEKLY)
                .daysOfWeek(Set.of(DayOfWeek.FRIDAY))
                .startDate(LocalDate.now().minusWeeks(4))
                .build();

        DeliveryPoint point = createDeliveryPoint(pattern);

        // When
        missionService.generateMissions();

        // Then: Should create 3 missions (4 weeks = 2 intervals + current)
        assertThat(missionRepository.count()).isEqualTo(3);
    }

    // --- MONTHLY RECURRENCE TESTS ---
    @Test
    void testMonthlyRecurrenceSameWeek() {
        // Given: 3rd Wednesday of month + adjacent days
        LocalDate startDate = LocalDate.of(2024, 3, 20); // 3rd Wednesday
        RecurrencePattern pattern = RecurrencePattern.builder()
                .recurrenceInterval(RecurrencePattern.RecurrenceInterval.MONTHLY)
                .daysOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                .startDate(startDate)
                .endDate(startDate.plusMonths(2))
                .build();

        DeliveryPoint point = createDeliveryPoint(pattern);

        // When
        missionService.generateMissions();

        // Then: 3 missions/month * 3 months = 9 missions
        assertThat(missionRepository.count()).isEqualTo(9);
    }

    // --- EDGE CASE TESTS ---
    @Test
    void testEndDateInPast() {
        // Given: Pattern ended yesterday
        RecurrencePattern pattern = RecurrencePattern.builder()
                .recurrenceInterval(RecurrencePattern.RecurrenceInterval.DAILY)
                .daysOfWeek(Set.of(DayOfWeek.MONDAY))
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().minusDays(1))
                .build();

        DeliveryPoint point = createDeliveryPoint(pattern);

        // When
        missionService.generateMissions();

        // Then: No missions created
        assertThat(missionRepository.count()).isZero();
    }

    @Test
    void testDuplicatePrevention() {
        // Given: Existing mission for today
        RecurrencePattern pattern = RecurrencePattern.builder()
                .recurrenceInterval(RecurrencePattern.RecurrenceInterval.DAILY)
                .daysOfWeek(Set.of(DayOfWeek.values()))
                .startDate(LocalDate.now())
                .build();

        DeliveryPoint point = createDeliveryPoint(pattern);
        missionService.generateMissions(); // Create initial mission

        // When: Run again
        missionService.generateMissions();

        // Then: No duplicate created
        assertThat(missionRepository.count()).isEqualTo(1);
    }

    @Test
    void testDisabledDeliveryPoints() {
        // Given: Disabled delivery point
        DeliveryPoint point = createDeliveryPoint(RecurrencePattern.builder()
                .recurrenceInterval(RecurrencePattern.RecurrenceInterval.DAILY).build());
        point.setEnabled(false);
        deliveryPointRepository.save(point);

        // When
        missionService.generateMissions();

        // Then: No missions created
        assertThat(missionRepository.count()).isZero();
    }

    // --- HELPER METHOD TESTS ---
*//*    @Test
    void testGetWeekOfMonth() {
        assertThat(getWeekOfMonth(LocalDate.of(2024,3,1))).isEqualTo(1);  // 1st week
        assertThat(getWeekOfMonth(LocalDate.of(2024,3,7))).isEqualTo(1);  // 1st week
        assertThat(getWeekOfMonth(LocalDate.of(2024,3,8))).isEqualTo(2);  // 2nd week
        assertThat(getWeekOfMonth(LocalDate.of(2024,3,31))).isEqualTo(5); // 5th week
    }*//*

    @Test
    void testMatchesRecurrencePatternMonthly() {
        LocalDate patternDate = LocalDate.of(2024, 3, 20); // 3rd Wednesday
        RecurrencePattern pattern = RecurrencePattern.builder()
                .recurrenceInterval(RecurrencePattern.RecurrenceInterval.MONTHLY)
                .daysOfWeek(Set.of(DayOfWeek.WEDNESDAY))
                .startDate(patternDate)
                .build();
    }*/
        // Valid case (same week in next month)
       /* LocalDate aprilDate = LocalDate.of(2024,4,17); // 3rd Wednesday
        assertThat(matchesRecurrencePattern(buildPoint(pattern), aprilDate)).isTrue();

        // Invalid case (different week)
        LocalDate mayDate = LocalDate.of(2024,5,1); // 1st Wednesday
        assertThat(matchesRecurrencePattern(buildPoint(pattern), mayDate)).isFalse();
    }*/

/*
    @Test
    void save() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAllByDate() {
    }

    @Test
    void findAll() {
    }

    @Test
    void delete() {
    }

 */


    }

