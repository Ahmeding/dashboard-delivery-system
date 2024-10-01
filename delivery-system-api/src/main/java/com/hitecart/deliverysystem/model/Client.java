package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client extends AbstractEntity{

    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "descrition")
    private String description;
    @Column(name = "numtel")
    private String numTel;
    @Column(name = "adresseEmail")
    private String adresseEmail;
    @Embedded
    private Adresse adresse;
    @Column(name = "siret")
    private String siret;
    @Column(name = "siren")
    private String siren;
    @OneToMany(mappedBy = "client")
    private List<CommandeClient> commandeClients;
    @OneToMany(mappedBy = "client")
    private List<Devis> devis;


}
