package com.hitecart.deliverysystem.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "delivery_service")
public class ServiceDelivery extends AbstractEntity{

    @Column(name = "reference",unique = true,nullable = false)
    private String reference;
    @Column(name = "destination_city")
    private String destinationCity;
    @Column(name = "distance")
    private double distance;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "basic_trip_cost")
    private BigDecimal basicTripCost;
    private boolean enabled;
    @OneToMany(mappedBy = "serviceDelivery")
    private List<DeliveryDestination> deliveryDestinations;


    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "SD-"+System.currentTimeMillis();
        }
    }

}
