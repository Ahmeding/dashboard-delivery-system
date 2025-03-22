package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.TypeEnergie;
import com.hitecart.deliverysystem.model.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "vehicule")
public class Vehicule extends AbstractEntity{

    private String reference;
    @Column(name = "logo")
    private String logo;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "immatriculation")
    private String immatriculation;
    @Column(name = "marque")
    private String marque;
    @Column(name = "modele")
    private String modele;
    @Column(name = "numVin")
    private String numVin;
    @Column(name = "datepmc")
    private LocalDate datePMC;
    @Column(name = "kilometrage")
    private BigDecimal kilometrage;
    @Column(name = "typecarburant")
    @Enumerated(EnumType.STRING)
    private TypeEnergie typecarburant;
    @Column(name = "volume")
    private BigDecimal volume;
    @Column(name = "dateexpctrltech")
    private LocalDate dateExpCtrlTec;
    @Column(name="is_active")
    private Boolean isActive;
    @OneToMany(mappedBy = "vehicule")
    private List<Reparation> reparations = new ArrayList<>();
    @Column(name="is_assigned")
    private boolean isAssigned;
    @Column(name="is_available")
    private boolean isAvailable = true;


    @OneToMany(mappedBy = "vehicule")
    private List<MissionVehicle> missionAssignments = new ArrayList<>();

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "DP-"+System.currentTimeMillis();
        }
    }
}
