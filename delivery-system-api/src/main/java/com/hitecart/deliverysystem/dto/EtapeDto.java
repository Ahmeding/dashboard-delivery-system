package com.hitecart.deliverysystem.dto;

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
import java.time.LocalDateTime;

@Data
@Builder
public class EtapeDto {
    private Integer id;
    private String titre;
    private String commentaire;
    private LocalDateTime heureLivPrevu;
    private LocalDateTime heureLivReel;
    private LocalDateTime dateDepart;
    private LocalDateTime dateArrivee;
    private String lieuDepart;
    private String lieuArrivee;
    //private MissionDto missionDto;
    private Integer missionId;

    public static EtapeDto fromEntity(Etape etape) {
        if(etape == null){
            return null;
        }
        return EtapeDto.builder()
                .id(etape.getId())
                .titre(etape.getTitre())
                .commentaire(etape.getCommentaire())
                .heureLivPrevu(etape.getHeureLivPrevu())
                .heureLivReel(etape.getHeureLivReel())
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
                etape.setHeureLivPrevu(dto.getHeureLivPrevu());
                etape.setHeureLivReel(dto.getHeureLivReel());
                etape.setDateDepart(dto.getDateDepart());
                etape.setDateArrivee(dto.getDateArrivee());
                etape.setLieuDepart(dto.getLieuDepart());
                etape.setLieuArrivee(dto.getLieuArrivee());
                //etape.setMission(MissionDto.toEntity(dto.getMissionDto()));

        return etape;
    }

}
