package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.TypeReparation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "repair")
public class Repair extends AbstractEntity{
    private String reference;
    @Column(name = "type")
    private TypeReparation type;
    @Column(name = "comment")
    private String comment;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "next_repair_date")
    private LocalDate nextRepairDate;
    @Column(name = "cost")
    private BigDecimal cost;
    @OneToMany(mappedBy = "repair")
    private List<SparePart> spareParts;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "RP-"+System.currentTimeMillis();
        }
    }
}
