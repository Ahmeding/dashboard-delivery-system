package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.EtatMission;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table(name = "mission")
public class Mission extends AbstractEntity {

    @Column(name = "titre")
    private String titre;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "volume")
    private BigDecimal volume;
    @Column(name = "date_debut")
    private LocalDateTime dateDebut;
    @Column(name = "date_fin")
    private LocalDateTime dateFin;
    @Column(name = "prix_ht")
    private BigDecimal prixHt;
    @Column(name = "tva")
    private BigDecimal tva;
    @Column(name = "prix_ttc")
    private BigDecimal prixTTC;
    @Column(name = "frais_chauffeur")
    private BigDecimal fraisChauffeur;

    @OneToMany(mappedBy = "mission")
    private List<Etape> etapes;

/*    @OneToMany(mappedBy = "mission")
    private List<MissionChauffeur> missionChauffeurs;*/

    @OneToMany(mappedBy = "mission")
    private List<MissionVehicle> missionVehicules;

    @Column(name = "etatmission")
    @Enumerated(EnumType.STRING)
    private EtatMission etatMission;

    @Column(name = "kmdebut")
    private double kmDebut;

    @Column(name = "kmfin")
    private double kmFin;

    @OneToOne
    @JoinColumn(name = "idfraismission", referencedColumnName = "id")
    private FraisMission fraisMission;


}
