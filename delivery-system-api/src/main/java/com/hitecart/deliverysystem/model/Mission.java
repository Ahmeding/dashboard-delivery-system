package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.AssignmentStatus;
import com.hitecart.deliverysystem.model.enums.EtatMission;
import com.hitecart.deliverysystem.model.enums.RouteDifficulty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mission")
public class Mission extends AbstractEntity {

    private String reference;
    @Column(name = "titre")
    private String titre;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "volume")
    private BigDecimal volume;
    @Column(name = "date_debut")
    private LocalDateTime dateDebut;
    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Etape> etapes;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MissionVehicle> missionVehicules = new ArrayList<>();

    @Column(name = "etatmission")
    @Enumerated(EnumType.STRING)
    private EtatMission etatMission;

    @Column(name = "kmdebut")
    private double kmDebut;

    @Column(name = "kmfin")
    private double kmFin;
    @OneToMany(mappedBy = "mission",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<FraisMission> fraisMissionList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "id_delivery_destination")
    private DeliveryDestination deliveryDestination;

    @Enumerated(EnumType.STRING)
    private RouteDifficulty difficulty; // SIMPLE, MEDIUM, DIFFICULT

    @Column(name = "assignment_status")
    @Enumerated(EnumType.STRING)
    private AssignmentStatus assignmentStatus = AssignmentStatus.PENDING_VOLUME;


    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "MIS-"+System.currentTimeMillis();
        }
    }


    // Helper method to maintain bidirectional relationship
    public void addMissionVehicle(MissionVehicle vehicle) {
        if(missionVehicules == null){
            missionVehicules = new ArrayList<>();
        }
        missionVehicules.add(vehicle);
        vehicle.setMission(this);
    }
    public void removeMissionVehicle(MissionVehicle missionVehicle) {
        missionVehicules.remove(missionVehicle);
        missionVehicle.setMission(null); // break bidirectional relationship
    }


    // Lombok configuration
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MissionVehicle)) return false;
        MissionVehicle that = (MissionVehicle) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
