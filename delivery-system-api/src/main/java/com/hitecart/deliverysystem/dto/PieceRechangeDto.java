package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.PieceRechange;
import com.hitecart.deliverysystem.model.PieceRechange;
import com.hitecart.deliverysystem.model.Reparation;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PieceRechangeDto {

    private Integer id;
    private String reference;
    private String nom;
    private BigDecimal prix;
    private ReparationDto reparationDto;

    public static PieceRechangeDto fromEntity(PieceRechange pieceRechange) {
        if(pieceRechange == null){
            return null;
        }
        return PieceRechangeDto.builder()
                .id(pieceRechange.getId())
                .reference(pieceRechange.getReference())
                .nom(pieceRechange.getNom())
                .prix(pieceRechange.getPrix())
                .reparationDto(ReparationDto.fromEntity( pieceRechange.getReparation()))
                .build();
    }

    public static PieceRechange toEntity(PieceRechangeDto dto) {
        if(dto==null){
            return null;
        }
        PieceRechange pieceRechange = new PieceRechange();
        pieceRechange.setId(dto.getId());
        pieceRechange.setNom(dto.getNom());
        pieceRechange.setPrix(dto.getPrix());
        pieceRechange.setReference(dto.getReference());
        pieceRechange.setReparation(ReparationDto.toEntity( dto.getReparationDto()));
        return pieceRechange;
    }
}
