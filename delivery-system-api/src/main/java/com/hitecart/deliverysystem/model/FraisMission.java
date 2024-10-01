package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "fraisMission")
public class FraisMission extends AbstractEntity{
    @Column(name = "nom")
    private String nom;
    @Column(name = "commentaire")
    private String commentaire;

    @OneToOne(mappedBy = "fraisMission")
    private Mission mission;

    @OneToMany(mappedBy = "fraisMission")
    private List<LigneFraisMission> lignesFM;

}
