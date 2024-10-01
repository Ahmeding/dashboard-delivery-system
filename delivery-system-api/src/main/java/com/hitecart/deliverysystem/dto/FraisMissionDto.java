package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.FraisMission;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FraisMissionDto {

    private Integer id;
    private String nom;
    private String commentaire;
    private MissionDto missionDto;
    private List<LigneFraisMissionDto> lignesFraisMissionDto;

    public static FraisMissionDto fromEntity(FraisMission fraisMission){
        if(fraisMission ==null){
            return null;
        }
        return FraisMissionDto.builder()
                .id(fraisMission.getId())
                .nom(fraisMission.getNom())
                .commentaire(fraisMission.getCommentaire())
                .missionDto(MissionDto.fromEntity(fraisMission.getMission()))
                .lignesFraisMissionDto(
                        fraisMission.getLignesFM()!=null?
                                fraisMission.getLignesFM().stream().
                                        map(LigneFraisMissionDto::fromEntity)
                                        .collect(Collectors.toList()) : null
                )
                .build();
    }

    public static FraisMission toEntity(FraisMissionDto dto){
        if(dto==null){
            return null;
        }
        FraisMission fraisMission = new FraisMission();
                fraisMission.setId(dto.getId());
                fraisMission.setNom(dto.getNom());
                fraisMission.setCommentaire(dto.getCommentaire());
                fraisMission.setMission(MissionDto.toEntity(dto.getMissionDto()));
                fraisMission.setLignesFM(dto.getLignesFraisMissionDto()!=null?
                        dto.getLignesFraisMissionDto().stream()
                                .map(LigneFraisMissionDto::toEntity)
                                .collect(Collectors.toList()) : null);
         return fraisMission;
    }
}
