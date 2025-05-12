package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.DeliveryDestination;
import com.hitecart.deliverysystem.model.FraisMission;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import com.hitecart.deliverysystem.model.Mission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime dateDebut;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime dateFin;
    private EtatMission etatMission;
    private List<EtapeDto> etapeDtos;
    private  List<FraisMissionDto> fraisMissionDtoList= new ArrayList<>();
    private List<MissionVehicleDto> missionVehiclesDtos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
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
                .commentaire(mission.getCommentaire())
                .volume(mission.getVolume())
                .dateDebut(mission.getDateDebut())
                .dateFin(mission.getDateFin())
                .etatMission(mission.getEtatMission())
                .kmDebut(mission.getKmDebut())
                .kmFin(mission.getKmFin())
                .fraisMissionDtoList(mission.getFraisMissionList()!=null?
                        mission.getFraisMissionList().stream()
                                .map(FraisMissionDto::fromEntity)
                                .collect(Collectors.toList()):null)
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
                .idDeliveryDestination(mission.getDeliveryDestination()!=null?mission.getDeliveryDestination().getId():null)
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
        mission.setDeliveryDestination(DeliveryDestination.builder().id(dto.idDeliveryDestination).build());
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
        return EtatMission.COMPLETED.equals(this.etatMission);
    }
}
