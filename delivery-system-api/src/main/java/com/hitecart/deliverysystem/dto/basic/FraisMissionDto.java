package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.FraisMission;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.enums.TypeFraisMission;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
public class FraisMissionDto{


    private Integer id;
    private String reference;
    private String name;
    private TypeFraisMission type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime expenseDate;
    private BigDecimal quantity;
    private BigDecimal cost;
    private String comment;
    private Integer missionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;


    public static FraisMissionDto fromEntity(FraisMission fraisMission){
        if(fraisMission ==null){
            return null;
        }
        return FraisMissionDto.builder()
                .id(fraisMission.getId())
                .reference(fraisMission.getReference())
                .name(fraisMission.getName())

                .type(fraisMission.getType())
                .expenseDate(fraisMission.getExpenseDate())
                .quantity(fraisMission.getQuantity())
                .cost(fraisMission.getCost())
                .comment(fraisMission.getComment())
                .missionId(fraisMission.getMission()!=null?fraisMission.getMission().getId():null)

                .createdAt(fraisMission.getCreatedAt())
                .updatedAt(fraisMission.getUpdatedAt())
                .createdBy(fraisMission.getCreatedBy())
                .lastModifiedBy(fraisMission.getLastModifiedBy())
                .build();
    }

    public static FraisMission toEntity(FraisMissionDto dto){
        if(dto==null){
            return null;
        }
        FraisMission fraisMission = new FraisMission();
                fraisMission.setId(dto.getId());
                fraisMission.setReference(dto.getReference());
                fraisMission.setName(dto.getName());
                fraisMission.setComment(dto.getComment());
                fraisMission.setType(dto.getType());
                fraisMission.setExpenseDate(dto.getExpenseDate());
                fraisMission.setQuantity(dto.getQuantity());
                fraisMission.setCost(dto.getCost());
                fraisMission.setMission(Mission.builder().id(dto.missionId).build());

        fraisMission.setCreatedAt(dto.getCreatedAt());
        fraisMission.setUpdatedAt(dto.getUpdatedAt());
        fraisMission.setCreatedBy(dto.getCreatedBy());
        fraisMission.setLastModifiedBy(dto.getLastModifiedBy());
         return fraisMission;
    }
}
