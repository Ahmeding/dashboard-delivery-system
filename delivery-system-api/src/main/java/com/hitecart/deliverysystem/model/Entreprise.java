package com.hitecart.deliverysystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "entreprise")
public class Entreprise extends AbstractEntity{

    @Column(name = "raisonsocial")
    private String raisonSocial;
    @Column(name = "numFiscal")
    private String numFiscla;
    @Column(name = "numtel")
    private String numtel;
    @Column(name = "adresseemail")
    private String adresseEmail;
    @Embedded
    private Adresse adresse;
}
