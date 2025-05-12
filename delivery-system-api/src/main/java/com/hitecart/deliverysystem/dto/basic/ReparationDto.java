package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.TypeReparation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ReparationDto{


    private Integer id;
    private String reference;
    private TypeReparation type;
    private String commentaire;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDate dateProchainRep;
    private BigDecimal cout;
    @JsonIgnore
    private List<PieceRechangeDto> pieceRechangesDtos;

    private VehicleDto vehiculeDto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static ReparationDto fromEntity(Reparation reparation) {
        if(reparation == null){
            //throw new NullPointerException("Reparation null!");
            return null;
        }

        return ReparationDto.builder()
                .id(reparation.getId())
                .reference(reparation.getReference())
                .type(reparation.getType())
                .commentaire(reparation.getCommentaire())
                .dateDebut(reparation.getDateDebut())
                .dateFin(reparation.getDateFin())
                .dateProchainRep(reparation.getDateProchainRep())
                .cout(reparation.getCout())

                .createdAt(reparation.getCreatedAt())
                .updatedAt(reparation.getUpdatedAt())
                .createdBy(reparation.getCreatedBy())
                .lastModifiedBy(reparation.getLastModifiedBy())


                .vehiculeDto(VehicleDto.fromEntity(reparation.getVehicule()))
                .build();
    }

    public static Reparation toEntity(ReparationDto dto) {
        if(dto==null){
           throw new NullPointerException("ReparationDto null!");
           //return null;
        }
            Reparation reparation = new Reparation();
        reparation.setId(dto.getId());
                    reparation.setId(dto.getId());
                    reparation.setType(dto.getType());
                    reparation.setCommentaire(dto.getCommentaire());
                    reparation.setDateDebut(dto.getDateDebut());
                    reparation.setDateFin(dto.getDateFin());
                    reparation.setDateProchainRep(dto.getDateProchainRep());
                    reparation.setCout(dto.getCout());
                    reparation.setVehicule(VehicleDto.toEntity(dto.getVehiculeDto()));

        reparation.setCreatedAt(dto.getCreatedAt());
        reparation.setUpdatedAt(dto.getUpdatedAt());
        reparation.setCreatedBy(dto.getCreatedBy());
        reparation.setLastModifiedBy(dto.getLastModifiedBy());
                    return reparation;

    }
}
