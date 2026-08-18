package com.hitecart.deliverysystem.services.impl;


import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.ClaimRepository;
import com.hitecart.deliverysystem.dto.claim.ClaimRequest;
import com.hitecart.deliverysystem.dto.claim.ClaimResponse;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.ClaimMapper;
import com.hitecart.deliverysystem.model.Claim;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.enums.ClaimStatus;
import com.hitecart.deliverysystem.model.enums.ClaimType;
import com.hitecart.deliverysystem.model.enums.RoleType;
import com.hitecart.deliverysystem.services.ClaimService;
import com.hitecart.deliverysystem.utils.CapitalizeFirstLetter;
import com.hitecart.deliverysystem.utils.CheckDuplicat;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.utils.EmailValidator;
import com.hitecart.deliverysystem.validator.ClaimValidator;
import com.hitecart.deliverysystem.validator.ClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the ClaimService interface.
 * Provides concrete business logic for claim management operations.
 * Handles transaction management and data persistence for claims.
 *
 * @author HiteCart Delivery System
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ClaimResponse createClaim(ClaimRequest request) {

        checkRequestValidation(request, "Claim not valid", "Claim validation failed");
        return savingClaim(request);
    }


    private ClaimResponse savingClaim(ClaimRequest request) {
        Claim claimToSave = claimMapper.toClaim(request);

        checkExists(claimToSave);
        if(claimToSave.getRelatedEntityType().equals("DRIVER")){
            checkIfDriverClaimHimSelf(claimToSave);
        }
        // Format and secure data
        claimToSave.setTitle(CapitalizeFirstLetter.capitalizeFirstLetter(claimToSave.getTitle()));
        claimToSave.setStatus(ClaimStatus.OPEN);
        claimToSave.setReportedDate(LocalDateTime.now());
        Claim savedClaim = claimRepository.save(claimToSave);
        return claimMapper.toClaimResponse(savedClaim);
    }
    private void  checkIfDriverClaimHimSelf(Claim claimToSave){
       if(Objects.equals(claimToSave.getSubjectEntityId(), claimToSave.getReportedBy().getId())){
           throw new InvalidOperationException(
                   "A driver cannot report a claim against themselves. " +
                           "Driver ID: " + claimToSave.getSubjectEntityId() +
                           " is the same as reporter ID: " + claimToSave.getReportedBy().getId()
           );
        }
    }
    private void checkExists(Claim claimToSave) {
        Integer excludeId=
                claimToSave.getId() != null?
                        claimToSave.getId():
                        -1;

        claimRepository.findByTitle(claimToSave.getTitle(),excludeId).ifPresent(
                existingClaim -> {
                    throw new InvalidOperationException("Claim already exists");
                }
        );

    }

    private void checkRequestValidation(ClaimRequest request, String claimNotValid, String claimValidationFailed) {
        List<String> errors = ClaimValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error(claimNotValid, request);
            throw new InvalidEntityException(claimValidationFailed, ErrorCodes.CLAIM_NOT_VALID, errors);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ClaimResponse updateClaimInfo(Integer id, ClaimRequest claimUpdateRequest) {
        var existingClaim = claimRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Claim not found with ID: " + id,
                                ErrorCodes.CLAIM_NOT_FOUND));

        claimMapper.updateClaimFromRequest(claimUpdateRequest,existingClaim);
        checkExists(existingClaim);
        Claim updatedClaim= claimRepository.save(existingClaim);
        return claimMapper.toClaimResponse(updatedClaim);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ClaimResponse updateStatus(Integer id, ClaimStatus status) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with ID: " + id));

        claim.setStatus(status);

        if (status == ClaimStatus.RESOLVED || status == ClaimStatus.CLOSED) {
            claim.setResolvedDate(LocalDateTime.now());
        }

        return claimMapper.toClaimResponse(claimRepository.save(claim));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClaimResponse getClaimById(Integer id) {
        CheckParmasUtil.checkID(id, "Claim ID parameter is null",
                "Cannot find claim with null ID", ErrorCodes.CLAIM_NOT_VALID_OPERATION);

        Claim claimById = claimRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Claim not found with ID: " + id,
                        ErrorCodes.CLAIM_NOT_FOUND
                )
        );
        return claimMapper.toClaimResponse(claimById);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClaimResponse> getAllClaims() {
        return claimRepository.findAll().stream()
                .map(claimMapper::toClaimResponse)
                .collect(Collectors.toList());
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClaimResponse> getClaimsByType(ClaimType type) {

//todo check type
        CheckParmasUtil.checkStringParma(type.toString(), "Type parameter is invalid",
                "Cannot search with empty type", ErrorCodes.CLAIM_NOT_VALID_OPERATION);
        List<Claim> claimsByType = claimRepository.findByClaimType(type)
                .orElseThrow(
                () -> new EntityNotFoundException(
                        "Claim not found with type: " + type,
                        ErrorCodes.CLAIM_NOT_FOUND
                ));
        return claimsByType.stream()
                .map(claimMapper::toClaimResponse)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClaimResponse> getClaimsByStatus(ClaimStatus status) {
   //todo check status
        CheckParmasUtil.checkStringParma(status.toString(), "Status parameter is invalid",
                "Cannot search with empty status", ErrorCodes.CLAIM_NOT_VALID_OPERATION);

        List<Claim> claimsByStatus = claimRepository.findByStatus(status)
                .orElseThrow( ()-> new EntityNotFoundException("Claim not found with status :"+status,
                        ErrorCodes.CLAIM_NOT_FOUND));
        return claimsByStatus.stream()
                .map(claimMapper::toClaimResponse)
                        .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteClaim(Integer id) {
        CheckParmasUtil.checkID(id, "Invalid claim ID",
                "Cannot delete with null ID", ErrorCodes.CLAIM_NOT_VALID_OPERATION);
        // Verify existence
        getClaimById(id);

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with ID: " + id));
        claimRepository.delete(claim);
    }

    /**
     * Retrieves paginated list of claims.
     *
     * @param page Page number (0-indexed)
     * @param size Number of items per page
     * @return Paginated response of claims
     */
    @Override
    public PageResponse<ClaimResponse> findAllClaims(int page, int size) {
        Pageable pageable= PageRequest.of(page,size,
                Sort.by("createdAt").descending());
        Page<Claim> claims = claimRepository.findAllClaims(pageable);

        List<ClaimResponse> claimsResponse = claims.stream()
                .map(claimMapper::toClaimResponse)
                .toList();


        return new PageResponse<>(
                claimsResponse,
                claims.getNumber(),
                claims.getSize(),
                claims.getTotalElements(),
                claims.getTotalPages(),
                claims.isFirst(),
                claims.isLast()
        );
    }
}
