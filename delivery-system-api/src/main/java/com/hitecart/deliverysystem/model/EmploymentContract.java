package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.enums.ContractStatus;
import com.hitecart.deliverysystem.model.enums.ContractType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@SuperBuilder
@Table(name = "contract")
public class EmploymentContract extends AbstractEntity{

    @Column(name = "reference")
    private String reference;
    @Column(name = "contract_type")
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    @Column(name = "contract_status")
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;



    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIgnore
    private Driver driver;

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "CTR-"+System.currentTimeMillis();
        }
    }

}
