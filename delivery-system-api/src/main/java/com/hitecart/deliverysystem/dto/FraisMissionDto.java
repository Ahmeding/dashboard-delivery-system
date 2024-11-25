package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.FraisMission;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
public class FraisMissionDto{


    private Integer id;
    private String reference;
    private String nom;
    private String commentaire;
    private MissionDto missionDto;
    private List<LigneFraisMissionDto> lignesFraisMissionDto;
    private LocalDateTime createdAt;
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
                .nom(fraisMission.getNom())
                .commentaire(fraisMission.getCommentaire())
                .missionDto(MissionDto.fromEntity(fraisMission.getMission()))
                .lignesFraisMissionDto(
                        fraisMission.getLignesFM()!=null?
                                fraisMission.getLignesFM().stream().
                                        map(LigneFraisMissionDto::fromEntity)
                                        .collect(Collectors.toList()) : null
                )

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
                fraisMission.setNom(dto.getNom());
                fraisMission.setCommentaire(dto.getCommentaire());
                fraisMission.setMission(MissionDto.toEntity(dto.getMissionDto()));
                fraisMission.setLignesFM(dto.getLignesFraisMissionDto()!=null?
                        dto.getLignesFraisMissionDto().stream()
                                .map(LigneFraisMissionDto::toEntity)
                                .collect(Collectors.toList()) : null);

        fraisMission.setCreatedAt(dto.getCreatedAt());
        fraisMission.setUpdatedAt(dto.getUpdatedAt());
        fraisMission.setCreatedBy(dto.getCreatedBy());
        fraisMission.setLastModifiedBy(dto.getLastModifiedBy());
         return fraisMission;
    }
}
