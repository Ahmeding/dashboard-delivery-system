package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.claim.ClaimRequest;
import com.hitecart.deliverysystem.dto.claim.ClaimResponse;
import com.hitecart.deliverysystem.dto.client.ClientUpdateRequest;
import com.hitecart.deliverysystem.model.enums.ClaimStatus;
import com.hitecart.deliverysystem.model.enums.ClaimType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(CLAIM_ENDPOINT)
@Tag(name="Claim")
public interface ClaimApi {
    @PostMapping(
            value = CREATE_CLAIM_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "saveClaim",

            description = "Ajouter une Reclamation , cette methode permet " +
                    "d'enregistrer une Reclamation ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Claim a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Claim n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
   ResponseEntity<ClaimResponse> saveClaim(@RequestBody ClaimRequest request);

    @PatchMapping(
            value = UPDATE_CLAIM_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "updateClaimInfo",

            description = "Modifier une Reclamation , cette methode permet " +
                    "modifier une Reclamation ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Claim a été  modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Claim n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<ClaimResponse> updateClaimInfo(@PathVariable("claimId")Integer id, @RequestBody ClaimRequest claimRequest);


    @PatchMapping(
            value = UPDATE_CLAIM_STATUS_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "updateClaimStatus",

            description = "Modifier la status d'une Reclamation , cette methode permet " +
                    "modifier la status d'une Reclamation ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Claim a été  modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClaimResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Claim n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<ClaimResponse> updateClaimStatus(@PathVariable("id") Integer id, @PathVariable("status")ClaimStatus status);

    @GetMapping(
            value = FIND_CLAIM_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(operationId = "findClaimById",

            description = "Rechercher une Reclamation par ID! Cette methode permet de chercher une Reclamation par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La Reclamation a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La Reclamation avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<ClaimResponse> findClaimById(@PathVariable("id") Integer id);



    @GetMapping(
            value = FIND_ALL_CLAIM_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findAllClaim",

            description = "Rechercher tout les Claim! Cette methode permet de retouner tous les Reclamations",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Claims retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<ClaimResponse>> getAllClaims();

    @GetMapping(
            value = FIND_CLAIM_BY_TYPE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findClaimsByType",

            description = "Rechercher une Reclamation par son type! Cette methode permet de chercher une Reclamation par son type",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La Reclamation a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "aucune Reclamation trouvé dans la BDD avec le type frouni",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<ClaimResponse>> findClaimsByType(@PathVariable("type") ClaimType type);


    @GetMapping(
            value = FIND_CLAIM_BY_STATUS_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findClaimsByStatus",

            description = "Rechercher des Claims par leur status! Cette methode permet de chercher une Reclamation par son status",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La Reclamation a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Aucune Reclamation trouvé dans la BDD, avec la status frouni",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<ClaimResponse>> findClaimsByStatus(@PathVariable("status") ClaimStatus status);

    @DeleteMapping(value = DELETE_CLAIM_ENDPOINT)
    @Operation(operationId = "deleteClaim",

            description = "Supprimer une Reclamation par ID! Cette methode permet de suprimmer une Reclamation par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La Reclamation a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La Reclamation avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
   ResponseEntity<?> deleteClaim(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_ALL_CLAIM_PAGEABLE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllClaimsPageable")
    ResponseEntity<PageResponse<ClaimResponse>> findAllClaimsPageable(
             @RequestParam(name = "page", defaultValue = "1", required = false)  int page,
             @RequestParam(name = "size", defaultValue = "10", required = false) int size);

}
