package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Getter
@SuperBuilder
@Setter
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
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    private int longTrips;
    private int standardTrips;

    @OneToOne
    @JoinColumn(name = "driver_id", unique = true)
    @JsonIgnore // Add this annotation
    private Driver driver; // Link to driver

    public void incrementLongTrips() { longTrips++; }
    public void incrementStandardTrips() { standardTrips++; }
    public int getTotalMissions() { return longTrips + standardTrips; }
}
