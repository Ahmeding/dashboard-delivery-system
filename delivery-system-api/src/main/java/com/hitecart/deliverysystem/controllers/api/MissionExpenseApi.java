package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;
import static com.hitecart.deliverysystem.utils.Constants.FIND_MISSION_EXPENSE_BY_ID_ENDPOINT;

@RequestMapping(MISSION_EXPENSE_ENDPOINT)
@Tag(name = "Mission Expense")
public interface MissionExpenseApi {

    @PostMapping(value = CREATE_MISSION_EXPENSE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveMissionExpense",
            description = "Ajouter/Modifier un fraisMission, cette methode permet " +
                    "d'enregistrer ou modifier un fraisMission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet MissionExpense a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionExpenseResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet MissionExpense n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionExpenseResponse> saveMissionExpense(@RequestBody MissionExpenseRequest missionExpenseRequest);


    @PatchMapping(value = UPDATE_MISSION_EXPENSE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateMissionExpenseInfo",
            summary = "updateMissionExpenseInfo",
            description = "Modifier un fraisMission, cette methode permet " +
                    "de modifier un fraisMission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet MissionExpense a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionExpenseResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet MissionExpense n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionExpenseResponse> updateMissionExpenseInfo(@PathVariable("expenseId") Integer expenseId, @RequestBody MissionExpenseRequest missionExpenseRequest);


    @PatchMapping(value = UPDATE_QTE_LIGNE_MISSION_EXPENSE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateMissionExpenseQuantity",
            summary = "updateQuantite",
            description = "Modifier la qte dans une ligne fraisMission, cette methode permet " +
                    "de la qte modifier dans une ligne fraisMission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La qte a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionExpenseResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La qte n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionExpenseResponse> updateMissionExpenseQuantity(@PathVariable("idFM") Integer idMF, @PathVariable("qte") BigDecimal qte);


    @GetMapping(value = FIND_MISSION_EXPENSE_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findMissionExpenseById",
            description = "Rechercher un MissionExpense par ID! Cette methode permet de chercher un fraisMission par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le fraisMission a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le fraisMission avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<MissionExpenseResponse> findMissionExpenseById(@PathVariable("id") Integer id);


    @GetMapping(value = FIND_MISSION_EXPENSE_BY_IDMISSION_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findMissionExpenseByIdMission",
            description = "Rechercher un MissionExpense par l'ID de mission! Cette methode permet de chercher un fraisMission par l'ID de mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le fraisMission a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le fraisMission avec l'ID  de mission frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<MissionExpenseResponse> findMissionExpenseByIdMission(@PathVariable("idMission") Integer idMission);

    @GetMapping(value = FIND_ALL_MISSION_EXPENSE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllMissionExpense",
            description = "Rechercher tout les MissionExpense! Cette methode permet de retouner tous les fraisMissions",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "ExpenseMissions retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<MissionExpenseResponse>> findAllMissionExpense();

    @DeleteMapping(value = DELETE_MISSION_EXPENSE_ENDPOINT)
    @Operation(operationId = "deleteMissionExpense",
            description = "Supprimer un MissionExpense par ID! Cette methode permet de suprimmer un fraisMission par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le fraisMission a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le fraisMission avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<?> deleteMissionExpense(@PathVariable("id") Integer id);


    @GetMapping(value = FIND_ALL_MISSION_EXPENSE_PAGEABLE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllExpenseMissionsPageable")
    ResponseEntity<PageResponse<MissionExpenseResponse>> findAllExpenseMissionsPageable(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    );
}
