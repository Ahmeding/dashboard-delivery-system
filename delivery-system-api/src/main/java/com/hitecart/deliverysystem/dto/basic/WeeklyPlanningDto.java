package com.hitecart.deliverysystem.dto.basic;

import com.hitecart.deliverysystem.model.WeeklyPlanning;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class WeeklyPlanningDto {

    private Integer id;
    private String title;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private List<DayPlanDto> daysPlanDto;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;


    public static WeeklyPlanningDto fromEntity(WeeklyPlanning weeklyPlanning) {
        if(weeklyPlanning == null)
        {
            return null;
        }

        return WeeklyPlanningDto.builder()
                .id(weeklyPlanning.getId())
                .title(weeklyPlanning.getTitle())
                .weekStartDate(weeklyPlanning.getWeekStartDate())
                .weekEndDate(weeklyPlanning.getWeekEndDate())

                .createdAt(weeklyPlanning.getCreatedAt())
                .updatedAt(weeklyPlanning.getUpdatedAt())
                .createdBy(weeklyPlanning.getCreatedBy())
                .lastModifiedBy(weeklyPlanning.getLastModifiedBy())


                .build();
    }

    public static WeeklyPlanning toEntity(WeeklyPlanningDto dto) {
        if(dto == null)
        {
            return null;
        }
        return WeeklyPlanning.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .weekStartDate(dto.getWeekStartDate())
                .weekEndDate(dto.getWeekEndDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .lastModifiedBy(dto.getLastModifiedBy())

                .build();
    }
}
