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
public class  LigneDevisDto {

    private Integer id;
    private String reference;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal prixTTC;
    private BigDecimal tva;
    private Integer devisId;
    private ServiceDeliveryDto serviceDeliveryDto;

    public static LigneDevisDto fromEntity(LigneDevis ligneDevis) {
        if(ligneDevis == null){
            return null;
        }
        return LigneDevisDto.builder()
                .id(ligneDevis.getId())
                .quantite(ligneDevis.getQuantite())
                .prixUnitaire(ligneDevis.getPrixUnitaire())
                .prixTTC(ligneDevis.getPrixTTC())
                .tva(ligneDevis.getTva())
                .devisId(ligneDevis.getDevis()!=null?ligneDevis.getDevis().getId():null)
                .serviceDeliveryDto(ServiceDeliveryDto.fromEntity(ligneDevis.getServiceDelivery()))
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
        ligneDevis.setPrixTTC(dto.getPrixTTC());
        ligneDevis.setTva(dto.getTva());
        ligneDevis.setServiceDelivery(ServiceDeliveryDto.toEntity(dto.getServiceDeliveryDto()));

        return ligneDevis;
    }
}
