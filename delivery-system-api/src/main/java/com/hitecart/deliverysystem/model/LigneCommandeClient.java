package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
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
    @ManyToOne
    @JoinColumn(name = "servdelivery_id")
    private ServiceDelivery serviceDelivery;
    @ManyToOne
    @JoinColumn(name = "idcommandeclient")
    private CommandeClient comamndeClient;
}
