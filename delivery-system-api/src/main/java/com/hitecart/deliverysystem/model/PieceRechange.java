package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "piece")
public class PieceRechange extends AbstractEntity {

    @Column(name = "reference")
    private String reference;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prix")
    private BigDecimal prix;
    @ManyToOne
    @JoinColumn(name = "idreparation")
    private Reparation reparation;
}
