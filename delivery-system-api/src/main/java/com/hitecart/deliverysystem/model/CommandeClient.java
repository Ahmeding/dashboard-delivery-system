package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@Entity
@Table(name = "commandeclient")
public class CommandeClient extends AbstractEntity {

    @Column(name = "code")
    private String code;
    @Column(name = "datecommande")
    private LocalDateTime dateCommnde;
    @Column(name = "montant_total_ht")
    private BigDecimal montantTotalHT;
    @Column(name = "montant_total_ttc")
    private BigDecimal montantTotalTTC;
    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;
    @OneToMany(mappedBy = "comamndeClient")
    private List<LigneCommandeClient> ligneCommandeClients;
    @Column(name = "etat_commande")
    @Enumerated(EnumType.STRING)
    private OrderStatus etatCommande;
}
