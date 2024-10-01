package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.MissionVehicle;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import com.hitecart.deliverysystem.model.Mission;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@ToString
public class MissionDto {

    private Integer id;
    private String titre;
    private String commentaire;
    private BigDecimal volume;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private BigDecimal prixHt;
    private BigDecimal tva;
    private BigDecimal prixTTC;
    private BigDecimal fraisChauffeur;
    private EtatMission etatMission;

    private List<EtapeDto> etapeDtos;

    /*private List<MissionChauffeurDto> missionChauffeurDtos;*/
    private List<MissionVehicleDto> missionVehiclesDtos;

    private double kmDebut;
    private double kmFin;

    public static MissionDto fromEntity(Mission mission) {
        if(mission==null){
            return null;
        }

        return MissionDto.builder()
                .id(mission.getId())
                .titre(mission.getTitre())
                .commentaire(mission.getCommentaire())
                .volume(mission.getVolume())
                .dateDebut(mission.getDateDebut())
                .dateFin(mission.getDateFin())
                .prixHt(mission.getPrixHt())
                .tva(mission.getTva())
                .prixTTC(mission.getPrixTTC())
                .fraisChauffeur(mission.getFraisChauffeur())
                .etatMission(mission.getEtatMission())
                .kmDebut(mission.getKmDebut())
                .kmFin(mission.getKmFin())
                .etapeDtos(
                        mission.getEtapes()!=null?
                                mission.getEtapes().stream()
                                        .map(EtapeDto::fromEntity)
                                        .collect(Collectors.toList()):null
                )

                .missionVehiclesDtos(
                        mission.getMissionVehicules() != null?
                                mission.getMissionVehicules().stream()
                                        .map(MissionVehicleDto::fromEntity)
                                        .collect(Collectors.toList()):null
                )
        .build();

    }

    public static Mission toEntity(MissionDto dto) {
        if(dto==null){
            return null;
        }

        Mission mission = new Mission();
        mission.setId(dto.getId());
        mission.setTitre(dto.getTitre());
        mission.setCommentaire(dto.getCommentaire());
        mission.setVolume(dto.getVolume());
        mission.setDateDebut(dto.getDateDebut());
        mission.setDateFin(dto.getDateFin());
        mission.setPrixHt(dto.getPrixHt());
        mission.setTva(dto.getTva());
        mission.setPrixTTC(dto.getPrixTTC());
        mission.setFraisChauffeur(dto.getFraisChauffeur());
        mission.setEtatMission(dto.getEtatMission());
        mission.setKmDebut(dto.getKmDebut());
        mission.setKmFin(dto.getKmFin());
        mission.setEtapes(
                dto.getEtapeDtos()!=null?
                        dto.getEtapeDtos().stream()
                                .map(EtapeDto::toEntity)
                                .collect(Collectors.toList()):null
        );

                mission.setMissionVehicules(
                        dto.getMissionVehiclesDtos() != null?
                                dto.getMissionVehiclesDtos().stream()
                                        .map(MissionVehicleDto::toEntity)
                                        .collect(Collectors.toList()):null
                );

        return mission;

    }

    public boolean isMissionAccomplished(){
        return EtatMission.ACCOMPLI.equals(this.etatMission);
    }
}
