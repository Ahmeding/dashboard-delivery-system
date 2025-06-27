package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "delivery_destination")
public class DeliveryDestination extends AbstractEntity {


    @Column(name= "reference",unique = true, updatable = false)
    private String reference;
    @OneToMany(
            mappedBy = "deliveryDestination",
            cascade = CascadeType.ALL, // Propagate operations to DeliveryPoint
            orphanRemoval = true, // Remove DeliveryPoint if deleted
            fetch = FetchType.LAZY
    )
    private List<DeliveryPoint> deliveryPoints =new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "delivery_service_id",nullable = false)
    private DeliveryService deliveryService;
    @OneToMany(mappedBy = "deliveryDestination")
    private List<Mission> missions =new ArrayList<>();


    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "DD-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }
}
