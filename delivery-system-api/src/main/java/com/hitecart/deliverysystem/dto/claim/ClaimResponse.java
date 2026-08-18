package com.hitecart.deliverysystem.dto.claim;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.model.enums.ClaimStatus;
import com.hitecart.deliverysystem.model.enums.ClaimType;
import com.hitecart.deliverysystem.model.enums.Priority;
import com.hitecart.deliverysystem.model.enums.Severity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimResponse {
    private Integer id;
    /**
     * The title or summary of the claim.
     * This should briefly describe the issue.
     */
    private String title;

    /**
     * Detailed description of the claim issue.
     * Provides comprehensive information about what happened.
     */
    private String description;

    /**
     * The category or type of claim.
     * Determines the nature of the issue being reported.
     */
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;

    /**
     * Current status of the claim in the resolution process.
     * Tracks the progress from creation to resolution.
     */
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    /**
     * The urgency level for resolving the claim.
     * Helps prioritize claim processing.
     */
    @Enumerated(EnumType.STRING)
    private Priority priority;

    /**
     * The impact level of the claim issue.
     * Indicates how serious the problem is.
     */
    @Enumerated(EnumType.STRING)
    private Severity severity;

    /**
     * The date and time when the claim was initially reported.
     * Automatically set when the claim is created.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime reportedDate;

    /**
     * The date and time when the claim was resolved.
     * Null until the claim reaches RESOLVED or CLOSED status.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")

    private LocalDateTime resolvedDate;
    /**
     * The type of entity this claim is related to.
     * Examples: "DRIVER", "VEHICLE", "MISSION".
     * Helps categorize and filter claims by entity type.
     */

    private String relatedEntityType;
    /**
     * The user who submitted the claim.
     * Can be an administrator, driver, or application user.
     */
    private Integer reportedByUserId;

    /**
     * The ID of the entity this claim is related to.
     * For example: driverId, vehicleId, or missionId.
     */
    private Integer subjectEntityId;


}
