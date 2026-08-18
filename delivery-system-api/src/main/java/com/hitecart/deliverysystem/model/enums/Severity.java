package com.hitecart.deliverysystem.model.enums;

/**
 * Enumeration representing the impact level of the claim issue.
 * Measures how serious the problem is in terms of consequences and effects.
 *
 * @author HiteCart Delivery System
 * @version 1.0
 */
public enum Severity {
    /**
     * Minor issues with minimal consequences.
     * Examples: small delays, minor inconveniences.
     */
    MINOR,

    /**
     * Moderate issues with noticeable but manageable consequences.
     * Examples: significant delays, customer dissatisfaction.
     */
    MODERATE,

    /**
     * Major issues with serious consequences requiring immediate action.
     * Examples: service disruptions, safety concerns.
     */
    MAJOR,

    /**
     * Critical issues with severe consequences requiring emergency response.
     * Examples: accidents, legal violations, major security breaches.
     */
    CRITICAL
}
