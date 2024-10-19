package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Adresse;
import com.hitecart.deliverysystem.model.Entreprise;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class EntrepriseDto {

    private String raisonSocial;
    private String numFiscla;
    private String numtel;
    private String adresseEmail;
    private AdresseDto adresseDto;
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;

    public static EntrepriseDto fromEntity(Entreprise entreprise) {
        if(entreprise == null){
            return null;
        }
        return EntrepriseDto.builder()
                .id(entreprise.getId())
                .raisonSocial(entreprise.getRaisonSocial())
                .numFiscla(entreprise.getNumFiscla())
                .numtel(entreprise.getNumtel())
                .adresseEmail(entreprise.getAdresseEmail())
                .adresseDto(AdresseDto.fromEntity(entreprise.getAdresse()))

                .createdAt(entreprise.getCreatedAt())
                .updatedAt(entreprise.getUpdatedAt())
                .createdBy(entreprise.getCreatedBy())
                .lastModifiedBy(entreprise.getLastModifiedBy())
                .build();
    }

    public static Entreprise toEntity(EntrepriseDto dto) {
        if(dto==null){
            return null;
        }
        Entreprise entreprise = new Entreprise();
        entreprise.setId(dto.getId());
        entreprise.setRaisonSocial(dto.getRaisonSocial());
        entreprise.setNumFiscla(dto.getNumFiscla());
        entreprise.setNumtel(dto.getNumtel());
        entreprise.setAdresseEmail(dto.getAdresseEmail());
        entreprise.setAdresse(AdresseDto.toEntity(dto.getAdresseDto()));

        entreprise.setCreatedAt(dto.getCreatedAt());
        entreprise.setUpdatedAt(dto.getUpdatedAt());
        entreprise.setCreatedBy(dto.getCreatedBy());
        entreprise.setLastModifiedBy(dto.getLastModifiedBy());
        return entreprise;
    }
}
