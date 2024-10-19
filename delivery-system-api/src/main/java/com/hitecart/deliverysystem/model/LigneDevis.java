package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
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
    @JoinColumn(name = "idmission")
    private Mission mission;
    @ManyToOne
    @JoinColumn(name = "iddevis")
    private Devis devis;
}
