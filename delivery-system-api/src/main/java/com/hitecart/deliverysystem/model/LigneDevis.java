package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table(name = "lignedevis")
public class LigneDevis extends AbstractEntity{
    @Column(name = "quantite")
    private BigDecimal quantite;
    @Column(name = "prixunitaire")
    private BigDecimal prixUnitaire;
    @Column(name = "prixTTC")
    private BigDecimal prixTTC;
    @Column(name = "tva")
    private BigDecimal tva;
    @ManyToOne
    @JoinColumn(name = "servdelivery_id")
    private ServiceDelivery serviceDelivery;
    @ManyToOne
    @JoinColumn(name = "iddevis")
    private Devis devis;
}
