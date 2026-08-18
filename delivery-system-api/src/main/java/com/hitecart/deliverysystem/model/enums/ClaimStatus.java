package com.hitecart.deliverysystem.model.enums;


/**
 * Enumeration representing the current status of a claim in the resolution workflow.
 * Tracks the progress of a claim from creation to final resolution.
 *
 * @author HiteCart Delivery System
 * @version 1.0
 */
public enum ClaimStatus {
    /**
     * Initial status when a claim is first created.
     * The claim has been submitted but not yet reviewed or assigned.
     */
    OPEN,

    /**
     * The claim has been assigned and is being actively worked on.
     * Investigation or resolution steps are in progress.
     */
    IN_PROGRESS,

    /**
     * The claim issue has been addressed and resolved.
     * Waiting for confirmation or final approval.
     */
    RESOLVED,

    /**
     * Final status indicating the claim is completely closed.
     * No further action is required.
     */
    CLOSED
}
