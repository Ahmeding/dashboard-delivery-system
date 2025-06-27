package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.DeliveryPointType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "delivery_point")
public class DeliveryPoint extends AbstractEntity {

    @Column(name = "reference")
    private String reference;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    @Embedded
    private Address address;
    @Embedded
    @Column(name = "contact")
    private DeliveryPointContact contact;
    @Column(name = "schedule")
    private LocalTime schedule;
    @Embedded
    private RecurrencePattern recurrencePattern;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DeliveryPointType deliveryPointType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_destination_id")
    private DeliveryDestination deliveryDestination;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    private boolean enabled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_point_category_id")
    private DeliveryPointCategory deliveryPointCategory;

    @PrePersist
    private void generateReference() {
        if (this.reference == null || this.reference.isEmpty()) {
            this.reference = "DP-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }
}
