package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "client")
public class Client extends User {

    @Column(name = "reference")
    private String reference;
    @Column(name = "description")
    private String description;
    @Column(name = "siret")
    private String siret;
    @Column(name = "siren")
    private String siren;
    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();
    @OneToMany(mappedBy = "client")
    private List<Quote> quotes= new ArrayList<>();
    @OneToMany(mappedBy = "client")
    private List<DeliveryPoint>  deliveryPoints= new ArrayList<>();


    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "CLI-"+System.currentTimeMillis();
        }
    }
}
