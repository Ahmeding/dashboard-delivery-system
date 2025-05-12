package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.PieceRechange;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
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
