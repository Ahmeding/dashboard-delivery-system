package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.LigneCommandeClient;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LigneCommandeClientDto {

    private Integer id;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal prixTTC;
    private BigDecimal tva;
    private ServiceDeliveryDto serviceDeliveryDto;
    private Integer cmdClientId;
    public static LigneCommandeClientDto fromEntity(LigneCommandeClient ligneCommandeClient) {
        if(ligneCommandeClient == null){
            return null;
        }
        return LigneCommandeClientDto.builder()
                .id(ligneCommandeClient.getId())
                .quantite(ligneCommandeClient.getQuantite())
                .prixUnitaire(ligneCommandeClient.getPrixUnitaire())
                .prixTTC(ligneCommandeClient.getPrixTTC())
                .tva(ligneCommandeClient.getTva())
                .serviceDeliveryDto(ServiceDeliveryDto.fromEntity(ligneCommandeClient.getServiceDelivery()))
                .cmdClientId(ligneCommandeClient.getComamndeClient()!=null?ligneCommandeClient.getComamndeClient().getId():null)
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
        ligneCommandeClient.setPrixTTC(dto.getPrixTTC());
        ligneCommandeClient.setTva(dto.getTva());
        ligneCommandeClient.setServiceDelivery(ServiceDeliveryDto.toEntity(dto.getServiceDeliveryDto()));

        return ligneCommandeClient;
    }

}
