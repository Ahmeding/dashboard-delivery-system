package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointResponse;
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

@RequestMapping(STOP_POINT_ENDPOINT)
@Tag(name = "Stop Point")
public interface StopPointApi {

    @PostMapping(value = CREATE_STOP_POINT_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(operationId = "saveStopPoint",
            description = "Ajouter/Modifier une piece de rechange, cette methode permet " +
                    "d'enregistrer ou modifier une piece de rechange",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet StopPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StopPointResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet StopPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<StopPointResponse> saveStopPoint(@RequestBody StopPointRequest stopPointRequest);

    @PatchMapping(value = UPDATE_STOP_POINT_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateStopPointInfo",
            description = "Ajouter/Modifier une piece de rechange, cette methode permet " +
                    "d'enregistrer ou modifier une piece de rechange",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet StopPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StopPointResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet StopPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<StopPointResponse> updateStopPointInfo(
            @PathVariable("stopPointId")Integer stopPointId ,
            @RequestBody StopPointRequest request);



    @GetMapping(value = FIND_STOP_POINT_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findStopPointById",
            description = "Rechercher une StopPoint par ID! Cette methode permet de chercher une piece de rechange par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La Piece de Rechange a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La Piece de Rechange avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<StopPointResponse> findStopPointById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_STOP_POINT_BY_MISSION_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findStopPointByMissionId",
            description = "Rechercher une StopPoint par findStopPointByMissionId! Cette methode permet de chercher une piece de rechange par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La Piece de Rechange a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La Piece de Rechange avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<StopPointResponse>> findStopPointByMissionId(@PathVariable("missionId") Integer missionId);

    @GetMapping(value = FIND_ALL_STOP_POINT_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllStopPoint",
            description = "Rechercher tout les stopPoint ! Cette methode permet de retouner tous les pieces de Rechange",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PieceRechanges retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<StopPointResponse>> findAllStopPoint();
    
    

    @DeleteMapping(value = DELETE_STOP_POINT_ENDPOINT)
    @Operation(operationId = "deleteStopPoint",
            description = "Supprimer une StopPoint par ID! Cette methode permet de suprimmer une piece de rechange par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La Piece de Rechange a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La Piece de Rechange avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
     ResponseEntity<?> deleteStopPoint(@PathVariable("id") Integer id);

}
