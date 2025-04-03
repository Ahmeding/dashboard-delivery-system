package com.hitecart.deliverysystem.dto.basic;

import com.hitecart.deliverysystem.model.PieceRechange;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PieceRechangeDto{


    private Integer id;
    private String reference;
    private String nom;
    private BigDecimal prix;
    private ReparationDto reparationDto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
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

                .createdAt(pieceRechange.getCreatedAt())
                .updatedAt(pieceRechange.getUpdatedAt())
                .createdBy(pieceRechange.getCreatedBy())
                .lastModifiedBy(pieceRechange.getLastModifiedBy())
                .build();
    }

    public static PieceRechange toEntity(PieceRechangeDto dto) {
        if(dto==null){
            return null;
        }
        PieceRechange pieceRechange = new PieceRechange();
        pieceRechange.setId(dto.getId());
        pieceRechange.setReference(dto.getReference());
        pieceRechange.setNom(dto.getNom());
        pieceRechange.setPrix(dto.getPrix());
        pieceRechange.setReference(dto.getReference());
        pieceRechange.setReparation(ReparationDto.toEntity( dto.getReparationDto()));

        pieceRechange.setCreatedAt(dto.getCreatedAt());
        pieceRechange.setUpdatedAt(dto.getUpdatedAt());
        pieceRechange.setCreatedBy(dto.getCreatedBy());
        pieceRechange.setLastModifiedBy(dto.getLastModifiedBy());
        return pieceRechange;
    }
}
