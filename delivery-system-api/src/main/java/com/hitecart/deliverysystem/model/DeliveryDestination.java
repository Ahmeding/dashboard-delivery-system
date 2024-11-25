package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "delivery_destination")
public class DeliveryDestination extends AbstractEntity {

    private String reference;
    @OneToMany(mappedBy = "deliveryDestination")
    private List<DeliveryPoint> deliveryPoints =new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;
    @ManyToOne
    @JoinColumn(name = "service_delivery_id",nullable = false)
    private ServiceDelivery serviceDelivery;

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "DD-"+System.currentTimeMillis();
        }
    }
}
