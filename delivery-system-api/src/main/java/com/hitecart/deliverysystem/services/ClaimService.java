package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.claim.ClaimRequest;
import com.hitecart.deliverysystem.dto.claim.ClaimResponse;
import com.hitecart.deliverysystem.model.Claim;
import com.hitecart.deliverysystem.model.enums.ClaimStatus;
import com.hitecart.deliverysystem.model.enums.ClaimType;

import java.util.List;


/**
 * Service interface for managing claims in the delivery system.
 * Provides operations for creating, updating, retrieving, and managing claims.
 * This service handles the business logic for claim processing and status management.
 *
 * @author HiteCart Delivery System
 * @version 1.0
 */
public interface ClaimService {

    /**
     * Creates a new claim in the system.
     * Automatically sets the claim status to OPEN and reported date to current time.
     *
     * @param request the claim object containing all claim details
     * @return the created claim with generated ID and timestamps
     * @throws IllegalArgumentException if required fields are missing or invalid
     */
    ClaimResponse createClaim(ClaimRequest request);

    /**
     * Updates an existing claim with new information.
     * Only updates modifiable fields; does not change status or reported date.
     *
     * @param id the ID of the claim to update
     * @param claimRequest the updated claim information
     * @return the updated claim object
     * @throws RuntimeException if no claim exists with the given ID
     */
    ClaimResponse updateClaimInfo(Integer id, ClaimRequest claimRequest);

    /**
     * Updates the status of a specific claim.
     * If status is set to RESOLVED or CLOSED, automatically sets the resolved date.
     *
     * @param id the ID of the claim to update
     * @param status the new status to set
     * @return the updated claim object
     * @throws RuntimeException if no claim exists with the given ID
     */
    ClaimResponse updateStatus(Integer id, ClaimStatus status);

    /**
     * Retrieves a claim by its unique identifier.
     *
     * @param id the ID of the claim to retrieve
     * @return the claim object if found
     * @throws RuntimeException if no claim exists with the given ID
     */
    ClaimResponse getClaimById(Integer id);

    /**
     * Retrieves all claims in the system.
     * Use with caution for large datasets; consider pagination for production use.
     *
     * @return a list of all claims, ordered by creation date descending
     */
    List<ClaimResponse> getAllClaims();

    /**
     * Retrieves all claims of a specific type.
     * Useful for filtering claims by category (driver, vehicle, mission, etc.).
     *
     * @param type the claim type to filter by
     * @return a list of claims matching the specified type
     */
    List<ClaimResponse> getClaimsByType(ClaimType type);

    /**
     * Retrieves all claims with a specific status.
     * Useful for workflow management and status tracking.
     *
     * @param status the claim status to filter by
     * @return a list of claims matching the specified status
     */
    List<ClaimResponse> getClaimsByStatus(ClaimStatus status);

    /**
     * Deletes a claim from the system.
     * This operation is permanent and should be used with caution.
     *
     * @param id the ID of the claim to delete
     * @throws RuntimeException if no claim exists with the given ID
     */
    void deleteClaim(Integer id);

    /**
     * Retrieves a paginated list of all claims in the system.
     * <p>
     * This method provides efficient, paginated access to claims for display in user interfaces
     * and API responses. The pagination mechanism ensures optimal performance even with large
     * datasets by limiting the amount of data transferred per request.
     * </p>
     * <p>
     * The returned page includes comprehensive claim information along with pagination metadata
     * such as total elements, total pages, and current page position.
     * </p>
     *
     * @param page the zero-based page index (0 = first page). Must not be negative.
     * @param size the number of claims to include in each page. Must be between 1 and 200.
     * @return a {@link PageResponse} containing:
     *         <ul>
     *           <li>List of {@link ClaimResponse} objects for the requested page</li>
     *           <li>Pagination metadata (total pages, total elements, current page, etc.)</li>
     *           <li>Sorting information if applicable</li>
     *         </ul>
     * @throws IllegalArgumentException if page is negative or size is out of valid range
     * @throws org.springframework.dao.DataAccessException if there's an issue accessing the data repository
     *
     * @example
     * <pre>{@code
     * // Get first page with 20 claims per page
     * PageResponse<ClaimResponse> page1 = claimService.findAllClaims(0, 20);
     *
     * // Get second page with 10 claims per page
     * PageResponse<ClaimResponse> page2 = claimService.findAllClaims(1, 10);
     * }</pre>
     *
     * @see PageResponse
     * @see ClaimResponse
     * @see #getClaimsByType(ClaimType)
     * @see #getClaimsByStatus(ClaimStatus)
     */
    PageResponse<ClaimResponse> findAllClaims(int page, int size);
}
