package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.TypeContrat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "contrat")
public class Contrat extends AbstractEntity{


    @Enumerated(EnumType.STRING)
    private TypeContrat typeContrat;
    @Column(name = "datedebut")
    private LocalDateTime dateDebut;
    @Column(name = "datefin")
    private LocalDateTime dateFin;


    @ManyToOne
    @JoinColumn(name = "idchauffeur")
    private Chauffeur chauffeur;



}
