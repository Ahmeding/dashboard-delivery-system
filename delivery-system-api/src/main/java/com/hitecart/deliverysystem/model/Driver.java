package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.DriverExperience;
import com.hitecart.deliverysystem.model.enums.DrivingLicenseType;
import com.hitecart.deliverysystem.utils.TimeSlot;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@SuperBuilder
@Table(name = "driver")
@Access(AccessType.FIELD)
@ToString(exclude = {"employmentContracts", "assignments"})
public class Driver extends User {

    private String reference;
    @Column(name = "driving_license_nbr",nullable = false,unique = true)
    private String drivinglicenseNbr;
    @Enumerated(EnumType.STRING)  // or EnumType.ORDINAL for integer mapping
    @Column(name="driving_license_type")
    private DrivingLicenseType drivinglicenseType;
    @Column(name="secu_nbr")
    private String secuNbr;
    @Column(name="rib")
    private String rib;

    @OneToMany(mappedBy = "driver")
    private List<EmploymentContract> employmentContracts = new ArrayList<>();
    @OneToMany(mappedBy = "driver")
    private List<DriverVehicle> assignments = new ArrayList<>();
    @Column(name="is_assigned")
    private boolean isAssigned;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level")
    private DriverExperience experienceLevel;

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private DriverStats stats;
    @OneToMany(mappedBy = "driver")
    private List<Feedback> feedbacks;

    @Column(name = "next_available_time")
    private LocalDateTime nextAvailableTime;

    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "DRV-"+System.currentTimeMillis();
        }
    }
    // Automatically create stats for new drivers
    @PostPersist
    private void initStats() {
        if (this.stats == null) {
            this.stats = new DriverStats();
            this.stats.setDriver(this);
        }
    }

    public void ensureStatsInitialized() {
        if (this.stats == null) {
            this.stats = new DriverStats();
            this.stats.setDriver(this);
            log.info("Initialized new stats for driver {}", this.getId());
        }
    }

    // Update existing methods
    public void incrementStandardTrips() {
        ensureStatsInitialized();
        stats.incrementStandardTrips();
    }

    public void incrementLongTrips() {
        ensureStatsInitialized();
        stats.incrementLongTrips();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return this.getId() != null && this.getId().equals(driver.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public List<TimeSlot> getAssignedTimeSlots() {
        return assignments.stream()
                .map(a -> new TimeSlot(a.getAssignedAt(), a.getMissionVehicle().getMission().getEndDate()))
                .collect(Collectors.toList());
    }

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        double roundedRate = Math.round(rate * 10.0) / 10.0;

        // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
        return roundedRate;
    }
}
