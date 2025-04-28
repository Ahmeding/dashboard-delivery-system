package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table(name = "etape")
@SuperBuilder
@ToString(callSuper=true)
public class Etape extends AbstractEntity{

    @Column(name = "titre")
    private String titre;
    @Column(name = "commentaire")
    private String commentaire;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime scheduledDeliveryTime ;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime actualDeliveryTime;
    @Column(name = "datedepart")
    private LocalDate dateDepart;
    @Column(name = "datearrivee")
    private LocalDate dateArrivee;
    @Column(name = "lieudepart")
    private String lieuDepart;
    @Column(name = "lieuarrivee")
    private String lieuArrivee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmission")
    private Mission mission;


}
