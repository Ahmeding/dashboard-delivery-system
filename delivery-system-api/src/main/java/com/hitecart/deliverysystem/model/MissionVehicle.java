package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table
public class MissionVehicle  extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicule vehicule;


    @OneToMany(mappedBy = "missionVehicle")
    private List<ChauffeurVehicle> chauffeurVehiculeList;

    private LocalDateTime assignedAt;
}
