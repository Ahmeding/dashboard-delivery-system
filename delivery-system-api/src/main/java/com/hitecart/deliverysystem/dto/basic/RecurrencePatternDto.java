package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
@Data
@Builder
public class RecurrencePatternDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum RecurrenceIntervalDto {
        DAILY, WEEKLY, BIWEEKLY, MONTHLY
    }
    private RecurrenceIntervalDto recurrenceInterval;
    private Set<DayOfWeek> daysOfWeek; // Delivery days (e.g. MONDAY, FRIDAY)
    private LocalDate startDate; // First delivery date
    private LocalDate endDate;   // Optional end date

    public static RecurrencePatternDto fromEntity(RecurrencePattern entity) {
        if(entity == null) return null;

        return RecurrencePatternDto.builder()
                .recurrenceInterval(entity.getRecurrenceInterval()!= null? RecurrenceIntervalDto.valueOf(entity.getRecurrenceInterval().name()) : null )
                .daysOfWeek(entity.getDaysOfWeek())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    public static RecurrencePattern toEntity(RecurrencePatternDto dto) {
        if(dto == null) return null;

        return RecurrencePattern.builder()
                .recurrenceInterval(dto.getRecurrenceInterval()!=null?
                        RecurrencePattern.
                        RecurrenceInterval
                                .valueOf(dto.getRecurrenceInterval().name()) : null )
                .daysOfWeek(dto.getDaysOfWeek())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
