package com.hitecart.deliverysystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
public class DeliveryPointCategory extends AbstractEntity {

    private String reference;
    private String name;
    private String description;
    private Double additionalFees;
    @OneToMany(mappedBy = "deliveryPointCategory")
    private List<DeliveryPoint> deliveryPoints = new ArrayList<>();

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "DPC-"+System.currentTimeMillis();
        }
    }
}
