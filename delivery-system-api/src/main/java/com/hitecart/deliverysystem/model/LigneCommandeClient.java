package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "lignecommandeclient")
public class LigneCommandeClient extends  AbstractEntity{


    @Column(name = "quantite")
    private BigDecimal quantite;
    @Column(name = "prixunitaire")
    private BigDecimal prixUnitaire;
    @Column(name = "prixTTC")
    private BigDecimal prixTTC;
    @Column(name = "tva")
    private BigDecimal tva;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_service_delivery",nullable = false,referencedColumnName = "id")
    private ServiceDelivery serviceDelivery;
    @ManyToOne
    @JoinColumn(name = "idcommandeclient")
    private CommandeClient comamndeClient;
}
