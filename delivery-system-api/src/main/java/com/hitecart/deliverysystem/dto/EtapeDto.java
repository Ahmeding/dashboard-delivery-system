package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.Etape;
import com.hitecart.deliverysystem.model.Etape;
import com.hitecart.deliverysystem.model.Mission;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class EtapeDto {

    private Integer id;
    private String titre;
    private String commentaire;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime scheduledDeliveryTime ;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime actualDeliveryTime;
    private LocalDate dateDepart;
    private LocalDate dateArrivee;
    private String lieuDepart;
    private String lieuArrivee;
    private Integer missionId;

    public static EtapeDto fromEntity(Etape etape) {
        if(etape == null){
            return null;
        }
        return EtapeDto.builder()
                .id(etape.getId())
                .titre(etape.getTitre())
                .commentaire(etape.getCommentaire())
                .scheduledDeliveryTime(etape.getScheduledDeliveryTime())
                .actualDeliveryTime(etape.getActualDeliveryTime())
                .dateDepart(etape.getDateDepart())
                .dateArrivee(etape.getDateArrivee())
                .lieuDepart(etape.getLieuDepart())
                .lieuArrivee(etape.getLieuArrivee())
                .missionId(etape.getMission()!=null? etape.getMission().getId():null)
                .build();
    }

    public static Etape toEntity(EtapeDto dto) {
        if(dto==null){
            return null;
        }
        Etape etape = new Etape();
        etape.setId(dto.getId());
                etape.setTitre(dto.getTitre());
                etape.setCommentaire(dto.getCommentaire());
                etape.setScheduledDeliveryTime(dto.getScheduledDeliveryTime());
                etape.setActualDeliveryTime(dto.getActualDeliveryTime());
                etape.setDateDepart(dto.getDateDepart());
                etape.setDateArrivee(dto.getDateArrivee());
                etape.setLieuDepart(dto.getLieuDepart());
                etape.setLieuArrivee(dto.getLieuArrivee());
                //etape.setMission(MissionDto.toEntity(dto.getMissionDto()));
        return etape;
    }

}
