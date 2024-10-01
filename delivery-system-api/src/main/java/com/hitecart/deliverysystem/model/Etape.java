package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table(name = "etape")
@ToString(callSuper=true)
public class Etape extends AbstractEntity{

    @Column(name = "titre")
    private String titre;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "heurelivprevu")
    private LocalDateTime heureLivPrevu;
    @Column(name = "heurelivreel")
    private LocalDateTime heureLivReel;
    @Column(name = "datedepart")
    private LocalDateTime dateDepart;
    @Column(name = "datearrivee")
    private LocalDateTime dateArrivee;
    @Column(name = "lieudepart")
    private String lieuDepart;
    @Column(name = "lieuarrivee")
    private String lieuArrivee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmission")
    private Mission mission;


}
