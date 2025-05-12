package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.TypeContrat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@SuperBuilder
@Table(name = "contrat")
public class Contrat extends AbstractEntity{

    private String reference;
    @Enumerated(EnumType.STRING)
    private TypeContrat typeContrat;
    @Column(name = "datedebut")
    private LocalDate dateDebut;
    @Column(name = "datefin")
    private LocalDate dateFin;


    @ManyToOne
    @JoinColumn(name = "idchauffeur")
    private Chauffeur chauffeur;

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "CTR-"+System.currentTimeMillis();
        }
    }

}
