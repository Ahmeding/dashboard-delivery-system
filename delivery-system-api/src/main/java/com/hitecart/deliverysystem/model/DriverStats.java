package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Tracks driver mission statistics
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false)
@Entity
@Table(name = "driver_stats")
@Access(AccessType.FIELD)
@EntityListeners(AuditingEntityListener.class)
public class DriverStats implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @CreatedDate
    @Column(name = "createdat", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updatedat")
    private LocalDateTime updatedAt;
    private int longTrips;
    private int standardTrips;

    @OneToOne
    @JoinColumn(name = "chauffeur_id", unique = true)
    private Chauffeur chauffeur; // Link to driver

    public void incrementLongTrips() { longTrips++; }
    public void incrementStandardTrips() { standardTrips++; }
    public int getTotalMissions() { return longTrips + standardTrips; }
}
