package com.hitecart.deliverysystem.controllers.claim;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.ClaimApi;
import com.hitecart.deliverysystem.dto.claim.ClaimRequest;
import com.hitecart.deliverysystem.dto.claim.ClaimResponse;
import com.hitecart.deliverysystem.model.enums.ClaimStatus;
import com.hitecart.deliverysystem.model.enums.ClaimType;
import com.hitecart.deliverysystem.services.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ClaimController implements ClaimApi {
    private final ClaimService claimService;

    @Override
    public ResponseEntity<ClaimResponse> saveClaim(ClaimRequest request) {
        System.out.println("test end point");
        return ResponseEntity.ok(claimService.createClaim(request));
    }

    @Override
    public ResponseEntity<ClaimResponse> updateClaimInfo(Integer id, ClaimRequest claimRequest) {
        return ResponseEntity.ok(claimService.updateClaimInfo(id,claimRequest));
    }

    @Override
    public ResponseEntity<ClaimResponse> updateClaimStatus(Integer id, ClaimStatus status) {
        return ResponseEntity.ok(claimService.updateStatus(id,status));
    }

    @Override
    public ResponseEntity<ClaimResponse> findClaimById(Integer id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    @Override
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @Override
    public ResponseEntity<List<ClaimResponse>> findClaimsByType(ClaimType type) {
        return ResponseEntity.ok(claimService.getClaimsByType(type));
    }

    @Override
    public ResponseEntity<List<ClaimResponse>> findClaimsByStatus(ClaimStatus status) {
        return ResponseEntity.ok(claimService.getClaimsByStatus(status));
    }

    @Override
    public ResponseEntity<?> deleteClaim(Integer id) {

        try{
            claimService.deleteClaim(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Claim deleted successfully"
                    ));
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Client not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<ClaimResponse>> findAllClaimsPageable(int page, int size) {
        return ResponseEntity.ok(claimService.findAllClaims(page, size));
    }
}
