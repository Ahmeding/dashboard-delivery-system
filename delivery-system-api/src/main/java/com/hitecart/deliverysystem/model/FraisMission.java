package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "fraisMission")
public class FraisMission extends AbstractEntity{

    private String reference;
    @Column(name = "nom")
    private String nom;
    @Column(name = "commentaire")
    private String commentaire;

    @OneToOne(mappedBy = "fraisMission")
    private Mission mission;

    @OneToMany(mappedBy = "fraisMission")
    private List<LigneFraisMission> lignesFM;
    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "MF-"+System.currentTimeMillis();
        }
    }
}
