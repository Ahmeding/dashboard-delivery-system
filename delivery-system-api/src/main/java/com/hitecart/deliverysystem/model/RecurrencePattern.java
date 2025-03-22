package com.hitecart.deliverysystem.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Embeddable // Can be embedded in other entities
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurrencePattern {

    public enum RecurrenceInterval {
        DAILY, WEEKLY, BIWEEKLY, MONTHLY
    }

    @Enumerated(EnumType.STRING)
    private RecurrenceInterval recurrenceInterval;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek; // Delivery days (e.g. MONDAY, FRIDAY)

    private LocalDate startDate; // First delivery date
    private LocalDate endDate;   // Optional end date

    // ========================
    // Core Logic: Check if date matches pattern
    // ========================
    public boolean matches(LocalDate date) {
        if (date.isBefore(startDate)) return false;
        if (endDate != null && date.isAfter(endDate)) return false;
        if (!daysOfWeek.contains(date.getDayOfWeek())) return false;

        return switch (recurrenceInterval) {
            case DAILY -> true;
            case WEEKLY -> true; // Already checked day of week
            case BIWEEKLY -> isBiweeklyMatch(date);
            case MONTHLY -> date.getDayOfMonth() == startDate.getDayOfMonth();
        };
    }

    private boolean isBiweeklyMatch(LocalDate date) {
        long weeksBetween = ChronoUnit.WEEKS.between(startDate, date);
        return weeksBetween % 2 == 0; // Every 2 weeks
    }
}
