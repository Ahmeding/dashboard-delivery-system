package com.hitecart.deliverysystem.model.enums;

/**
 * Enumeration representing the urgency level for claim resolution.
 * Helps prioritize which claims should be addressed first based on business impact.
 *
 * @author HiteCart Delivery System
 * @version 1.0
 */
public enum Priority {
    /**
     * Low urgency claims that can be addressed during normal processing.
     * Minimal impact on operations or customer satisfaction.
     */
    LOW,

    /**
     * Standard priority claims that should be addressed in regular order.
     * Moderate impact that requires attention but not immediate action.
     */
    MEDIUM,

    /**
     * Important claims that require prompt attention.
     * Significant impact on operations or customer satisfaction.
     */
    HIGH,

    /**
     * Critical claims that require immediate attention and resolution.
     * Severe impact that may affect safety, major operations, or key customers.
     */
    URGENT
}
