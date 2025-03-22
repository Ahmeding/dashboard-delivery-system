package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.DrivingLicenseType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@SuperBuilder
@Table(name = "chauffeur")
@Access(AccessType.FIELD)
public class Chauffeur extends Utilisateur{

    @Column(name = "difficult_routes_completed")
    private int difficultRoutesCompleted = 0;  // +++ Increases when they finish hard missions

    @Column(name = "success_rate", precision = 5, scale = 2)
    private BigDecimal successRate = BigDecimal.valueOf(100.00); // Start at 100%

    private String reference;
    @Column(name = "driving_license_nbr",nullable = false,unique = true)
    private String drivinglicenseNbr;

    @Enumerated(EnumType.STRING)  // or EnumType.ORDINAL for integer mapping
    @Column(name="typepermis")
    private DrivingLicenseType drivinglicenseType;
    @Column(name="num_secu")
    private String numSecu;
    @Column(name="rib")
    private String rib;

    @OneToMany(mappedBy = "chauffeur")
    private List<Contrat> contrats = new ArrayList<>();
    @OneToMany(mappedBy = "chauffeur")
    private List<ChauffeurVehicle> assignments = new ArrayList<>();
    @Column(name="is_assigned")
    private boolean isAssigned;


    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "DRV-"+System.currentTimeMillis();
        }
    }
}
