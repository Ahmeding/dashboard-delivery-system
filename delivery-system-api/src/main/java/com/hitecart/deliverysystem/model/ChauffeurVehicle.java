package com.hitecart.deliverysystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table
public class ChauffeurVehicle extends AbstractEntity{


    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    private Chauffeur chauffeur;
    @ManyToOne
    @JoinColumn(name = "mission_vehicle_id")
    private MissionVehicle missionVehicle;
    private LocalDateTime assignedAt ;
}
