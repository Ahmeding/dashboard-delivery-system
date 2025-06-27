package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.EnergyType;
import com.hitecart.deliverysystem.model.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "vehicle")
public class Vehicle extends AbstractEntity{

    private String reference;
    @Column(name = "logo")
    private String logo;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    @Column(name = "registrationNbr")
    private String registrationNbr;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;
    @Column(name = "numVin")
    private String vinNbr;
    @Column(name = "datepmc")
    private LocalDate dfcDate;
    @Column(name = "mileage")
    private BigDecimal mileage;
    @Column(name = "energyType")
    @Enumerated(EnumType.STRING)
    private EnergyType energyType;
    @Column(name = "weight_capacity")
    private BigDecimal weightCapacity;
    @Column(name = "volume_capacity")
    private BigDecimal volumeCapacity;
    @Column(name = "ctExpDate")
    private LocalDate ctExpDate;
    @Column(name="is_active")
    private Boolean isActive;
    @Column(name="is_assigned")
    private boolean isAssigned;
    @Column(name="is_available")
    private boolean isAvailable = true;

    @OneToMany(mappedBy = "vehicle")
    private List<Repair> repairs = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle")
    private List<MissionVehicle> missionAssignments = new ArrayList<>();

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "DP-"+System.currentTimeMillis();
        }
    }
}
