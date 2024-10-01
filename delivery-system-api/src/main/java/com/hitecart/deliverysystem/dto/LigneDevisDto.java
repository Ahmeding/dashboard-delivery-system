package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.LigneDevis;
import com.hitecart.deliverysystem.model.LigneDevis;
import com.hitecart.deliverysystem.model.Mission;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LigneDevisDto {
    private Integer id;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private MissionDto missionDto;
    private DevisDto devisDto;

    public static LigneDevisDto fromEntity(LigneDevis ligneDevis) {
        if(ligneDevis == null){
            return null;
        }
        return LigneDevisDto.builder()
                .id(ligneDevis.getId())
                .quantite(ligneDevis.getQuantite())
                .prixUnitaire(ligneDevis.getPrixUnitaire())
                .missionDto(MissionDto.fromEntity(ligneDevis.getMission()))
                .devisDto(DevisDto.fromEntity(ligneDevis.getDevis()))
                .build();
    }

    public static LigneDevis toEntity(LigneDevisDto dto) {
        if(dto==null){
            return null;
        }
        LigneDevis ligneDevis = new LigneDevis();
        ligneDevis.setId(dto.getId());
        ligneDevis.setQuantite(dto.getQuantite());
        ligneDevis.setPrixUnitaire(dto.getPrixUnitaire());
        ligneDevis.setMission(MissionDto.toEntity(dto.getMissionDto()));
        ligneDevis.setDevis(DevisDto.toEntity(dto.getDevisDto()));

        return ligneDevis;
    }
}
