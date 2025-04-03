package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.enums.QuoteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@Schema(description = "DTO representing the response of a Devis")
public class DevisDtoResponse{

    private String code;
    private LocalDateTime dateDevis;
    private ClientDto client;
    private List<LigneDevisDto> ligneDevisDtos;
    private QuoteStatus etatDevis;
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;
    private BigDecimal montantTotalHT;
    private BigDecimal montantTotalTTC;

    public static DevisDtoResponse fromEntity(Devis devis){
        if(devis==null){
            return null;
        }
        return DevisDtoResponse.builder()
                .id(devis.getId())
                .code(devis.getCode())
                .dateDevis(devis.getDateDevis())
                .client(devis.getClient()!=null?ClientDto.fromEntity(devis.getClient()):null)
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
                .montantTotalHT(devis.getMontantTotalHT())
                .montantTotalTTC(devis.getMontantTotalTTC())
                .build();
    }

    public static Devis toEntity(DevisDtoResponse dto){
        if(dto==null){
            return null;
        }
        Devis devis = new Devis();
                devis.setId(dto.getId());
                devis.setCode(dto.getCode());
                devis.setDateDevis(dto.getDateDevis());
                devis.setEtatDevis(dto.getEtatDevis());
                devis.setClient(ClientDto.toEntity(dto.getClient()));
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
        devis.setMontantTotalHT(dto.getMontantTotalHT());
        devis.setMontantTotalTTC(dto.getMontantTotalTTC());
        return devis;
    }

    @JsonIgnore
    public boolean isDevisConfirmed(){
        return QuoteStatus.CONFIRMED.equals(this.etatDevis);
    }
}
