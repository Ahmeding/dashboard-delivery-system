package com.hitecart.deliverysystem.model.enums;

/**
 * Enumeration representing different types of claims that can be submitted.
 * Categorizes claims based on the nature of the issue being reported.
 *
 * @author HiteCart Delivery System
 * @version 1.0
 */
public enum ClaimType {
    /**
     * Claims related to driver performance, behavior, or issues.
     * Examples: late arrival, rude behavior, license problems.
     */
    DRIVER_ISSUE,

    /**
     * Claims related to vehicle problems or maintenance issues.
     * Examples: breakdowns, accidents, mechanical failures.
     */
    VEHICLE_PROBLEM,

    /**
     * Claims related to delivery missions or tasks.
     * Examples: delayed delivery, wrong address, package issues.
     */
    MISSION_COMPLAINT,

    /**
     * Claims specifically about delivery process issues.
     * Examples: failed delivery attempts, customer not available.
     */
    DELIVERY_ISSUE,

    /**
     * Claims that don't fit into other categories.
     * Used for miscellaneous or general complaints.
     */
    OTHER
}
