package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.AssignmentStatus;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Column(name = "title")
    private String title;
    @Column(name = "comment")
    private String comment;
    @Column(name = "volume")
    private BigDecimal volume;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    private LocalDateTime eta;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean returnTransportRequired = false;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StopPoint> stopPoints = new ArrayList<>();

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionVehicle> missionVehicles = new ArrayList<>();

    @Column(name = "mission_status")
    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    @Column(name = "start_mileage")
    private double startMileage;

    @Column(name = "end_mileage")
    private double endMileage;
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionExpense> missionExpenseList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_delivery_destination")
    private DeliveryDestination deliveryDestination;

    @Column(name = "assignment_status")
    @Enumerated(EnumType.STRING)
    private AssignmentStatus assignmentStatus = AssignmentStatus.PENDING_VOLUME;
    @Column(name = "is_accomplished")
    private Boolean isAccomplished = false;


    @PrePersist
    private void generateReference() {
        if (this.reference == null || this.reference.isEmpty()) {
            this.reference = "MIS-" + System.currentTimeMillis();
        }
    }


    // Helper method to maintain bidirectional relationship
    public void addMissionVehicle(MissionVehicle missionVehicle) {
        if (missionVehicles == null) {
            missionVehicles = new ArrayList<>();
        }
        missionVehicles.add(missionVehicle);
        missionVehicle.setMission(this);
    }

    public void removeMissionVehicle(MissionVehicle missionVehicle) {
        missionVehicles.remove(missionVehicle);
        missionVehicle.setMission(null); // break bidirectional relationship
    }


    // Lombok configuration
/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MissionVehicle)) return false;
        MissionVehicle that = (MissionVehicle) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Fix: Should be 'instanceof Mission'
        if (!(o instanceof Mission)) return false;
        // Fix: Should cast to 'Mission'
        Mission that = (Mission) o;
        // Use the ID for comparison, which is a good practice for entities.
        // Ensure your AbstractEntity provides getId()
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        // Return a consistent hashcode. Using the class literal is safe when the ID is null.
        return getClass().hashCode();
    }

}
