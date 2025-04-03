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
@Table(name = "chauffeur")
@Access(AccessType.FIELD)
@ToString(exclude = {"contrats", "assignments"})
public class Chauffeur extends Utilisateur{

    private String reference;
    @Column(name = "driving_license_nbr",nullable = false,unique = true)
    private String drivinglicenseNbr;

    @Enumerated(EnumType.STRING)  // or EnumType.ORDINAL for integer mapping
    @Column(name="typepermis")
    private DrivingLicenseType drivinglicenseType;
    @Column(name="num_secu")
    private String numSecu;
    @Column(name="rib")
    private String rib;

    @OneToMany(mappedBy = "chauffeur")
    private List<Contrat> contrats = new ArrayList<>();
    @OneToMany(mappedBy = "chauffeur")

    private List<ChauffeurVehicle> assignments = new ArrayList<>();
    @Column(name="is_assigned")
    private boolean isAssigned;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level")
    private DriverExperience experienceLevel;

    @OneToOne(mappedBy = "chauffeur", cascade = CascadeType.ALL, orphanRemoval = true)
    private DriverStats stats;


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
            this.stats.setChauffeur(this);
        }
    }

    public void ensureStatsInitialized() {
        if (this.stats == null) {
            this.stats = new DriverStats();
            this.stats.setChauffeur(this);
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
        Chauffeur chauffeur = (Chauffeur) o;
        return this.getId() != null && this.getId().equals(chauffeur.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public List<TimeSlot> getAssignedTimeSlots() {
        return assignments.stream()
                .map(a -> new TimeSlot(a.getAssignedAt(), a.getMissionVehicle().getMission().getDateFin()))
                .collect(Collectors.toList());
    }
}
