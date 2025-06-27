package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractResponse;
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

@RequestMapping(EMPLOYMENT_CONTRACT_ENDPOINT)
@Tag( name = "EmploymentContract")
public interface EmploymentContractApi {

    @PostMapping(value = CREATE_EMPLOYMENT_CONTRACT_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveEmploymentContract",
            description = "Ajouter/Modifier un contrat, cette methode permet " +
                    "d'enregistrer ou modifier un contrat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet EmploymentContract a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmploymentContractResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet EmploymentContract n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<EmploymentContractResponse> saveEmploymentContract(@RequestBody EmploymentContractRequest employmentContractRequest);

    @PatchMapping(value = UPDATE_EMPLOYMENT_CONTRACT_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateEmploymentContractInfo",
            description = "Ajouter/Modifier un contrat, cette methode permet " +
                    "d'enregistrer ou modifier un contrat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet EmploymentContract a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmploymentContractResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet EmploymentContract n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<EmploymentContractResponse> updateEmploymentContractInfo(@PathVariable("contractId") Integer contractId,@RequestBody EmploymentContractRequest dto);

    @GetMapping(value = FIND_EMPLOYMENT_CONTRACT_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findEmploymentContractById",
            description = "Rechercher un EmploymentContract par ID! Cette methode permet de chercher un contrat par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le contrat a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le contrat avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<EmploymentContractResponse> findEmploymentContractById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_EMPLOYMENT_CONTRACT_BY_DRIVER_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllEmploymentContractByDriverId",
            description = "Rechercher un EmploymentContract par l'ID de driver! Cette methode permet de chercher un contrat par l'ID du driver",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le contrat a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le contrat avec l'ID du driver frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<EmploymentContractResponse>> findAllEmploymentContractByDriverId(@PathVariable("driverId") Integer driverId);

    @GetMapping(value = FIND_ALL_EMPLOYMENT_CONTRACT_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllEmploymentContract",
            description = "Rechercher tout les EmploymentContract! Cette methode permet de retouner tous les employmentContracts",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contrats retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<EmploymentContractResponse>> findAllEmploymentContract();

    @DeleteMapping(value = DELETE_EMPLOYMENT_CONTRACT_ENDPOINT)
    @Operation(operationId = "deleteEmploymentContract",
            description = "Supprimer un EmploymentContract par ID! Cette methode permet de suprimmer un contrat par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le contrat a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le contrat avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<?> deleteEmploymentContract(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_EMPLOYMENT_CONTRACT_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    @Operation(operationId = "findAllEmploymentContractsPageable")
    ResponseEntity<PageResponse<EmploymentContractResponse>> findAllEmploymentContractsPageable(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
