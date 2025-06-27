package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.repair.RepairRequest;
import com.hitecart.deliverysystem.dto.repair.RepairResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(REPAIR_ENDPOINT)
@Tag(name = "Repair")
public interface RepairApi {

    @PostMapping(value = CREATE_REPAIR_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(operationId = "saveRepair",
            description = "Ajouter/Modifier une repair, cette methode permet " +
                    "d'enregistrer ou modifier une reparattion",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Repair a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RepairResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Repair n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<RepairResponse> saveRepair(@RequestBody RepairRequest request);

    @PatchMapping(value = UPDATE_REPAIR_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateRepairInfo",
            description = "Ajouter/Modifier une repair, cette methode permet " +
                    "d'enregistrer ou modifier une reparattion",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Repair a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RepairResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Repair n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<RepairResponse> updateRepairInfo(@PathVariable("repairId")Integer repairId ,@RequestBody  RepairRequest repairRequest);


    @GetMapping(value = FIND_REPAIR_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findRepairById",
            description = "Rechercher une Repair par ID! Cette methode permet de chercher une repair par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La repair a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La repair avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<RepairResponse> findRepairById(@PathVariable("id") Integer id);



    @GetMapping(value = FIND_REPAIR_BY_DATE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findRepairByDate",
            description = "Rechercher une Repair par DATE ! Cette methode permet de chercher une repair par date",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La repair a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La repair avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<RepairResponse>> findRepairByDate(@PathVariable("repDate") Instant dateRep);



    @GetMapping(value = FIND_ALL_REPAIR_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllRepair",
            description = "Rechercher tout les Repair! Cette methode permet de retouner toutes les repairs",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reparations retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<RepairResponse>> findAllRepair();



    @DeleteMapping(value = DELETE_REPAIR_ENDPOINT)
    @Operation(operationId = "deleteRepair",
            description = "Supprimer une Repair par ID! Cette methode permet de suprimmer une repair par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La repair a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La repair avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
     ResponseEntity<?> deleteRepair(@PathVariable("id") Integer id);



    @GetMapping(value =FIND_ALL_REPAIR_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    @Operation(operationId = "findAllRepairsPageable")
    ResponseEntity<PageResponse<RepairResponse>> findAllRepairsPageable(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
