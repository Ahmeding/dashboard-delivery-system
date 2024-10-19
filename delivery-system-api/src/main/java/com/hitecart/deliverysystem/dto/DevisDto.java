package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.LigneDevis;
import com.hitecart.deliverysystem.model.enums.QuoteStatus;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
public class DevisDto {

    private String code;
    private LocalDateTime dateDevis;
    private Integer clientId;
    private List<LigneDevisDto> ligneDevisDtos;
    private QuoteStatus etatDevis;
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;

    public static DevisDto fromEntity(Devis devis){
        if(devis==null){
            return null;
        }
        return DevisDto.builder()
                . id(devis.getId())
                .code(devis.getCode())
                .dateDevis(devis.getDateDevis())
                .clientId(devis.getClient()!=null?devis.getClient().getId():null)
                /*.idclient(devis.getClient()!=null?devis.getClient().getId():null)*/
                .ligneDevisDtos(
                        devis.getLigneDevis()!=null?
                                devis.getLigneDevis().stream()
                                        .map(LigneDevisDto::fromEntity)
                                        .collect(Collectors.toList()):null
                )
                .etatDevis(devis.getEtatDevis())

                .createdAt(devis.getCreatedAt())
                .updatedAt(devis.getUpdatedAt())
                .createdBy(devis.getCreatedBy())
                .lastModifiedBy(devis.getLastModifiedBy())
                .build();
    }

    public static Devis toEntity(DevisDto dto){
        if(dto==null){
            return null;
        }
        Devis devis = new Devis();
                devis.setId(dto.getId());
                devis.setCode(dto.getCode());
                devis.setDateDevis(dto.getDateDevis());
                devis.setEtatDevis(dto.getEtatDevis());
                /*devis.setClient(ClientDto.toEntity(dto.getClientDto()));*/
                devis.setLigneDevis(
                        dto.getLigneDevisDtos()!=null?
                                dto.getLigneDevisDtos().stream()
                                        .map(LigneDevisDto::toEntity)
                                        .collect(Collectors.toList()):null
                );

        devis.setCreatedAt(dto.getCreatedAt());
        devis.setUpdatedAt(dto.getUpdatedAt());
        devis.setCreatedBy(dto.getCreatedBy());
        devis.setLastModifiedBy(dto.getLastModifiedBy());
        return devis;
    }

    @JsonIgnore
    public boolean isDevisConfirmed(){
        return QuoteStatus.CONFIRMED.equals(this.etatDevis);
    }
}
