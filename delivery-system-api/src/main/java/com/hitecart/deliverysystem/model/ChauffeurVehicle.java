package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.utils.TimeSlot;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class ChauffeurVehicle extends AbstractEntity{


    @ManyToOne
    @JoinColumn(name = "chauffeur_id", nullable = false)
    private Chauffeur chauffeur;

    @ManyToOne
    @JoinColumn(name = "mission_vehicle_id",nullable = false)
    @JsonIgnore // Prevent JSON recursion
    private MissionVehicle missionVehicle;


    private LocalDateTime assignedAt ;

    public TimeSlot getTimeSlot() {
        return new TimeSlot(
                this.getMissionVehicle().getMission().getDateDebut(),
                this.getMissionVehicle().getMission().getDateFin()
        );
    }
    @PrePersist
    @PreUpdate
    private void validateMissionVehicle() {
        if (missionVehicle == null) {
            throw new IllegalStateException("ChauffeurVehicle must be assigned to a MissionVehicle");
        }
    }

    // Use only ID for hashCode/equals
    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChauffeurVehicle that = (ChauffeurVehicle) o;
        return Objects.equals(this.getId(), that.getId());
    }
}
