package com.hitecart.deliverysystem.dto.basic;



import com.hitecart.deliverysystem.model.DayPlan;
import com.hitecart.deliverysystem.model.enums.DayName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class DayPlanDto {
    private Integer id;
    private DayName name;
    private LocalDate date;
    private Integer idWeeklyPlanning;
    private List<MissionDto> missionsDto;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static DayPlanDto fromEntity(DayPlan dayPlan) {
        if(dayPlan ==null){
            return null;
        }
        return DayPlanDto.builder()
                .id(dayPlan.getId())
                .name(dayPlan.getName())
                .date(dayPlan.getDate())
                .createdAt(dayPlan.getCreatedAt())
                .updatedAt(dayPlan.getUpdatedAt())
                .createdBy(dayPlan.getCreatedBy())
                .lastModifiedBy(dayPlan.getLastModifiedBy())
                .build();
    }

    public static DayPlan toEntity(DayPlanDto dto) {
        if(dto==null){
            return null;
        }
       return  DayPlan.builder()
               .id(dto.getId())
               .name(dto.getName())
               .date(dto.getDate())
               .createdAt(dto.getCreatedAt())
        .updatedAt(dto.getUpdatedAt())
        .createdBy(dto.getCreatedBy())
               .lastModifiedBy(dto.getLastModifiedBy())
               .build();
    }

}
