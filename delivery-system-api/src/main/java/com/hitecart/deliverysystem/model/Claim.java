package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.ClaimStatus;
import com.hitecart.deliverysystem.model.enums.ClaimType;
import com.hitecart.deliverysystem.model.enums.Priority;
import com.hitecart.deliverysystem.model.enums.Severity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents a claim or complaint in the delivery system.
 * Claims can be submitted by users, drivers, or administrators regarding various issues
 * such as driver behavior, vehicle problems, mission complaints, or delivery issues.
 *
 * @author HiteCart Delivery System
 * @version 1.0
 * @since 2024
 */

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(callSuper=true)
@Table(name = "claim")
public class Claim extends AbstractEntity{

    /**
     * The title or summary of the claim.
     * This should briefly describe the issue.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Detailed description of the claim issue.
     * Provides comprehensive information about what happened.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The category or type of claim.
     * Determines the nature of the issue being reported.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimType claimType;

    /**
     * Current status of the claim in the resolution process.
     * Tracks the progress from creation to resolution.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;

    /**
     * The urgency level for resolving the claim.
     * Helps prioritize claim processing.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    /**
     * The impact level of the claim issue.
     * Indicates how serious the problem is.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    /**
     * The date and time when the claim was initially reported.
     * Automatically set when the claim is created.
     */
    @Column(name = "reported_date", nullable = false)
    private LocalDateTime reportedDate;

    /**
     * The date and time when the claim was resolved.
     * Null until the claim reaches RESOLVED or CLOSED status.
     */
    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;

    /**
     * The user who submitted the claim.
     * Can be an administrator, driver, or application user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    /**
     * The ID of the entity this claim is related to.
     * For example: driverId, vehicleId, or missionId.
     */
    @Column(name = "subject_entity_id")
    private Integer subjectEntityId;

    /**
     * The type of entity this claim is related to.
     * Examples: "DRIVER", "VEHICLE", "MISSION".
     * Helps categorize and filter claims by entity type.
     */
    @Column(name = "related_entity_type")
    private String relatedEntityType;
}

