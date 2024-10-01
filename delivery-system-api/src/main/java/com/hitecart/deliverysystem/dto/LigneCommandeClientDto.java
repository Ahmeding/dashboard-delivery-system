package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.LigneCommandeClient;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LigneCommandeClientDto {

    private Integer id;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private MissionDto missionDto;
    private CommandeClientDto commandeClientDto;

    public static LigneCommandeClientDto fromEntity(LigneCommandeClient ligneCommandeClient) {
        if(ligneCommandeClient == null){
            return null;
        }
        return LigneCommandeClientDto.builder()
                .id(ligneCommandeClient.getId())
                .quantite(ligneCommandeClient.getQuantite())
                .prixUnitaire(ligneCommandeClient.getPrixUnitaire())
                .missionDto(MissionDto.fromEntity(ligneCommandeClient.getMission()))
                .commandeClientDto(CommandeClientDto.fromEntity(ligneCommandeClient.getComamndeClient()))
                .build();
    }

    public static LigneCommandeClient toEntity(LigneCommandeClientDto dto) {
        if(dto==null){
            return null;
        }
        LigneCommandeClient ligneCommandeClient = new LigneCommandeClient();
        ligneCommandeClient.setId(dto.getId());
        ligneCommandeClient.setQuantite(dto.getQuantite());
        ligneCommandeClient.setPrixUnitaire(dto.getPrixUnitaire());
        ligneCommandeClient.setMission(MissionDto.toEntity(dto.getMissionDto()));
        ligneCommandeClient.setComamndeClient(CommandeClientDto.toEntity(dto.getCommandeClientDto()));

        return ligneCommandeClient;
    }

}
