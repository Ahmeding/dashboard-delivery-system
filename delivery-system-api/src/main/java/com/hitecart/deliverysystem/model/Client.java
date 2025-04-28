package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "client")
public class Client extends Utilisateur{

    private String reference;
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
    @OneToMany(mappedBy = "client")
    private List<DeliveryPoint>  deliveryPoints;


    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "CLI-"+System.currentTimeMillis();
        }
    }
}
