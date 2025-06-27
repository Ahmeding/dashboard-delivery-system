package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter @Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "spare_part")
public class SparePart extends AbstractEntity {

    @Column(name = "reference")
    private String reference;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "quantity")
    private Double quantity;
    @ManyToOne
    @JoinColumn(name = "repair_id")
    private Repair repair;

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "PR-"+System.currentTimeMillis();
        }
    }
}
