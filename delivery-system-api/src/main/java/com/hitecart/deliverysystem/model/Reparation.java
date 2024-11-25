package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.TypeReparation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "reparation")
public class Reparation extends AbstractEntity{
    private String reference;
    @Column(name = "type")
    private TypeReparation type;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "datedebut")
    private LocalDate dateDebut;
    @Column(name = "datefin")
    private LocalDate dateFin;
    @Column(name = "dateprochainRep")
    private LocalDate dateProchainRep;
    @Column(name = "cout")
    private BigDecimal cout;
    @OneToMany(mappedBy = "reparation")
    private List<PieceRechange> pieceRechanges;
    @ManyToOne
    @JoinColumn(name = "idvehicule")
    private Vehicule vehicule;

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "RP-"+System.currentTimeMillis();
        }
    }
}
