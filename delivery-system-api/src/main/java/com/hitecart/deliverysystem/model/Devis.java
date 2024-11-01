package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.QuoteStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "devis")
public class Devis extends AbstractEntity{

    @Column(name = "code")
    private String code;
    @Column(name = "dateDevis")
    private LocalDateTime dateDevis;
    @Column(name = "montant_total_ht")
    private BigDecimal montantTotalHT;
    @Column(name = "montant_total_ttc")
    private BigDecimal montantTotalTTC;
    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;
    @OneToMany(mappedBy = "devis")
    private List<LigneDevis> ligneDevis;
    @Column(name = "etat_devis")
    @Enumerated(EnumType.STRING)
    private QuoteStatus etatDevis;

}
