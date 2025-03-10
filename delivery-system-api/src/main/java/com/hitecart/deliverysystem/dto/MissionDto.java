package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.MissionVehicle;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import com.hitecart.deliverysystem.model.Mission;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
public class MissionDto {


    private Integer id;
    private String reference;
    private String titre;
    private String commentaire;
    private BigDecimal volume;
    private Integer idDayPlan;
    private Integer idDeliveryDestination;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private EtatMission etatMission;
    private List<EtapeDto> etapeDtos;
    private List<MissionVehicleDto> missionVehiclesDtos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    private double kmDebut;
    private double kmFin;

    public static MissionDto fromEntity(Mission mission) {
        if(mission==null){
            return null;
        }

        return MissionDto.builder()
                .id(mission.getId())
                .reference(mission.getReference())
                .titre(mission.getTitre())
                .idDayPlan(mission.getDayPlan()!=null?mission.getDayPlan().getId():null)
                    .idDeliveryDestination(mission.getDeliveryDestination()!=null?
                            mission.getDeliveryDestination().getId():null)
                .commentaire(mission.getCommentaire())
                .volume(mission.getVolume())
                .dateDebut(mission.getDateDebut())
                .dateFin(mission.getDateFin())
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

                .createdAt(mission.getCreatedAt())
                .updatedAt(mission.getUpdatedAt())
                .createdBy(mission.getCreatedBy())
                .lastModifiedBy(mission.getLastModifiedBy())
        .build();

    }

    public static Mission toEntity(MissionDto dto) {
        if(dto==null){
            return null;
        }

        Mission mission = new Mission();
        mission.setId(dto.getId());
        mission.setReference(dto.getReference());
        mission.setTitre(dto.getTitre());
        mission.setCommentaire(dto.getCommentaire());
        mission.setVolume(dto.getVolume());
        mission.setDateDebut(dto.getDateDebut());
        mission.setDateFin(dto.getDateFin());
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

        mission.setCreatedAt(dto.getCreatedAt());
        mission.setUpdatedAt(dto.getUpdatedAt());
        mission.setCreatedBy(dto.getCreatedBy());
        mission.setLastModifiedBy(dto.getLastModifiedBy());
        return mission;

    }

    public boolean isMissionAccomplished(){
        return EtatMission.ACCOMPLI.equals(this.etatMission);
    }
}
