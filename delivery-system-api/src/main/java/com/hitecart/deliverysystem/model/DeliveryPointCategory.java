package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
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
@Table(name = "delivery_point_category")
public class DeliveryPointCategory extends AbstractEntity {

    @Column(name = "reference")
    private String reference;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "additional_fees")
    private Double additionalFees;
    @OneToMany(mappedBy = "deliveryPointCategory")
    private List<DeliveryPoint> deliveryPoints = new ArrayList<>();

    @PrePersist
    private void generateReference() {
        if (this.reference == null || this.reference.isEmpty()) {
            this.reference = "DPC-" + System.currentTimeMillis();
        }
    }
}
