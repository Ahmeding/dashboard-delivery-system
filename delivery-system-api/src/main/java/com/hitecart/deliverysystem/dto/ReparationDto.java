package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.TypeReparation;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReparationDto {

    private Integer id;
    private TypeReparation type;
    private String commentaire;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private LocalDateTime dateProchainRep;
    private BigDecimal cout;
    @JsonIgnore
    private List<PieceRechangeDto> pieceRechangesDtos;

    private VehiculeDto vehiculeDto;

    public static ReparationDto fromEntity(Reparation reparation) {
        if(reparation == null){
            //throw new NullPointerException("Reparation null!");
            return null;
        }

        return ReparationDto.builder()
                .id(reparation.getId())
                .type(reparation.getType())
                .commentaire(reparation.getCommentaire())
                .dateDebut(reparation.getDateDebut())
                .dateFin(reparation.getDateFin())
                .dateProchainRep(reparation.getDateProchainRep())
                .cout(reparation.getCout())

                .vehiculeDto(VehiculeDto.fromEntity(reparation.getVehicule()))
                .build();
    }

    public static Reparation toEntity(ReparationDto dto) {
        if(dto==null){
           throw new NullPointerException("ReparationDto null!");
           //return null;
        }
            Reparation reparation = new Reparation();
                    reparation.setId(dto.getId());
                    reparation.setType(dto.getType());
                    reparation.setCommentaire(dto.getCommentaire());
                    reparation.setDateDebut(dto.getDateDebut());
                    reparation.setDateFin(dto.getDateFin());
                    reparation.setDateProchainRep(dto.getDateProchainRep());
                    reparation.setCout(dto.getCout());
                    reparation.setVehicule(VehiculeDto.toEntity(dto.getVehiculeDto()));
                    return reparation;

    }
}
