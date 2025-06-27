package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.utils.TimeSlot;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "driver_vehicle")
public class DriverVehicle extends AbstractEntity{


    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;
    @ManyToOne
    @JoinColumn(name = "mission_vehicle_id",nullable = false)
    @JsonIgnore // Prevent JSON recursion
    private MissionVehicle missionVehicle;
    private LocalDateTime assignedAt ;


    public TimeSlot getTimeSlot() {
        return new TimeSlot(
                this.getMissionVehicle().getMission().getStartDate(),
                this.getMissionVehicle().getMission().getEndDate()
        );
    }
    @PrePersist
    @PreUpdate
    private void validateMissionVehicle() {
        if (missionVehicle == null) {
            throw new IllegalStateException("DriverVehicle must be assigned to a MissionVehicle");
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
        DriverVehicle that = (DriverVehicle) o;
        return Objects.equals(this.getId(), that.getId());
    }
}
