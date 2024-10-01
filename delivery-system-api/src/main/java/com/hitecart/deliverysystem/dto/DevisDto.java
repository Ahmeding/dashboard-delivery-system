package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.LigneDevis;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class DevisDto {


    private Integer id;
    private String code;
    private LocalDateTime dateDevis;
    private ClientDto clientDto;
    @JsonIgnore
    private List<LigneDevisDto> ligneDevisDtos;

    public static DevisDto fromEntity(Devis devis){
        if(devis==null){
            return null;
        }
        return DevisDto.builder()
                . id(devis.getId())
                .code(devis.getCode())
                .dateDevis(devis.getDateDevis())
                .clientDto(ClientDto.fromEntity(devis.getClient()))
                .ligneDevisDtos(
                        devis.getLigneDevis()!=null?
                                devis.getLigneDevis().stream()
                                        .map(LigneDevisDto::fromEntity)
                                        .collect(Collectors.toList()):null
                )
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
                devis.setClient(ClientDto.toEntity(dto.getClientDto()));
                devis.setLigneDevis(
                        dto.getLigneDevisDtos()!=null?
                                dto.getLigneDevisDtos().stream()
                                        .map(LigneDevisDto::toEntity)
                                        .collect(Collectors.toList()):null
                );
        return devis;
    }
}
