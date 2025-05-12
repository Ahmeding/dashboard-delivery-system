package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.TypeFraisMission;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "fraisMission")
public class FraisMission extends AbstractEntity{

    private String reference;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeFraisMission type;
    @Column(name = "expenseDate")
    private LocalDateTime expenseDate;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "comment")
    private String comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmission")
    private Mission mission;

    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "MF-"+System.currentTimeMillis();
        }
    }
}
