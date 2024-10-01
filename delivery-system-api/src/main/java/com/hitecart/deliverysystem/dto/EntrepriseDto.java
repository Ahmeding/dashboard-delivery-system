package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Adresse;
import com.hitecart.deliverysystem.model.Entreprise;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntrepriseDto {
    private Integer id;
    private String raisonSocial;
    private String numFiscla;
    private String numtel;
    private String adresseEmail;
    private AdresseDto adresseDto;

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
        return entreprise;
    }
}
