package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Claim;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.enums.ClaimStatus;
import com.hitecart.deliverysystem.model.enums.ClaimType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Repository interface for Claim entity persistence operations.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 * Handles all database interactions for the Claim entity.
 *
 * @author HiteCart Delivery System
 * @version 1.0
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer> {
    /**
     * Finds all claims of a specific type.
     *
     * @param claimType the type of claims to retrieve
     * @return a list of claims matching the specified type, ordered by creation date
     */
    Optional<List<Claim>> findByClaimType(ClaimType claimType);

    /**
     * Finds all claims with a specific status.
     *
     * @param status the status of claims to retrieve
     * @return a list of claims matching the specified status, ordered by creation date
     */
    Optional<List<Claim>> findByStatus(ClaimStatus status);

    /**
     * Finds all claims related to a specific entity.
     * Useful for retrieving all claims associated with a particular driver, vehicle, or mission.
     *
     * @param relatedEntityId the ID of the related entity
     * @param relatedEntityType the type of the related entity (e.g., "DRIVER", "VEHICLE")
     * @return a list of claims related to the specified entity
     */
    List<Claim> findBySubjectEntityIdAndRelatedEntityType(Integer relatedEntityId, String relatedEntityType);

    @Query("""
            SELECT c
            FROM Claim c
            WHERE c.title = :title
            AND c.id <> :excludeId
        """)
    Optional<Object> findByTitle(
            @Param("title") String title,
            @Param("excludeId") Integer excludeId);

    @Query("""
            SELECT claim
            FROM Claim claim
          """)
    Page<Claim> findAllClaims(Pageable pageable);
}
