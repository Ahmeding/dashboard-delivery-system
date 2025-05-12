package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class MissionVehicle  extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    @JsonIgnore // Prevent JSON recursion
    private Mission mission;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id" , nullable = false)
    private Vehicule vehicule;


    @NotEmpty
    @Size(min = 1, max = 2, message = "Vehicle must have 1-2 drivers")
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "missionVehicle", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ChauffeurVehicle> chauffeurVehiculeList = new ArrayList<>();

    private LocalDateTime assignedAt;

    public void addChauffeurVehicle(ChauffeurVehicle chauffeurVehicle) {
        chauffeurVehiculeList.add(chauffeurVehicle);
        chauffeurVehicle.setMissionVehicle(this);
    }

    public void removeChauffeurVehicle(ChauffeurVehicle chauffeurVehicle) {
        chauffeurVehiculeList.remove(chauffeurVehicle);
        chauffeurVehicle.setMissionVehicle(null);
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
        MissionVehicle that = (MissionVehicle) o;
        return Objects.equals(this.getId(), that.getId());
    }
}
