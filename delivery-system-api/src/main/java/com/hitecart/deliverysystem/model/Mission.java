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
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
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

    @OneToMany(mappedBy = "mission")
    private List<Etape> etapes;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private List<MissionVehicle> missionVehicules = new ArrayList<>();

    @Column(name = "etatmission")
    @Enumerated(EnumType.STRING)
    private EtatMission etatMission;

    @Column(name = "kmdebut")
    private double kmDebut;

    @Column(name = "kmfin")
    private double kmFin;
    @OneToOne
    @JoinColumn(name = "idfraismission", referencedColumnName = "id")
    private FraisMission fraisMission;
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
}
