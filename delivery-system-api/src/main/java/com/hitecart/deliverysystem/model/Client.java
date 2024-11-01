package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "client")
public class Client extends Utilisateur{

    @Column(name = "descrition")
    private String description;
    @Column(name = "siret")
    private String siret;
    @Column(name = "siren")
    private String siren;
    @OneToMany(mappedBy = "client")
    private List<CommandeClient> commandeClients;
    @OneToMany(mappedBy = "client")
    private List<Devis> devis;
}
