package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Builder
@Table(name="chauffeur")
public class Chauffeur extends AbstractEntity{

    @Column(name="nom")
    private String nom;
    @Column(name="prenom")
    private String prenom;
    @Column(name="adresseemail")
    private String adresseEmail;
    @Column(name="motdepasse")
    private String motDePasse;
    @Column(name="numtel")
    private String numTel;
    @Column(name="date_naissance")
    private LocalDateTime dateNaissance;
    @Column(name="typepermis")
    private String typePermi;
    @Column(name="num_secu")
    private String numSecu;
    @Column(name="rib")
    private String rib;
    @Embedded
    private Adresse adresse;

    @OneToMany(mappedBy = "chauffeur")
    private List<Contrat> contrats;
}
