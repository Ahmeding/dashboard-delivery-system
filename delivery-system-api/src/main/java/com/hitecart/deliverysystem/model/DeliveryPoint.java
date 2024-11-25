package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.DeliveryPointType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
public class DeliveryPoint extends AbstractEntity{

    private String reference;
    private String name;
    private String city;
    private Adresse address;
    private DeliveryPointContact contact;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime schedule;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DeliveryPointType deliveryPointType;
    @ManyToOne
    @JoinColumn(name = "delivery_distination_id")
    private DeliveryDestination deliveryDestination;
    private boolean enabled;

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "DP-"+System.currentTimeMillis();
        }
    }
}
