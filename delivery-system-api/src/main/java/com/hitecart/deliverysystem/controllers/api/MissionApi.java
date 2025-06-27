package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.mission.MissionRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(MISSION_ENDPOINT)
@Tag(name = "Mission")
public interface MissionApi {

    @PostMapping(value = CREATE_MISSION_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveMission",
            description = "Ajouter/Modifier une mission, cette methode permet " +
                    "d'enregistrer ou modifier une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Mission a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json"
                                    //schema = @Schema(implementation = MissionFullDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionResponse> saveMission(@RequestBody MissionRequest missionRequest);

    @PatchMapping(value = UPDATE_MISSION_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateMissionInfo",
            description = "Modifier une mission, cette methode permet " +
                    "modifier une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Mission a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json"
                                    //schema = @Schema(implementation = MissionFullDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionResponse> updateMissionInfo(@PathVariable("missionId") Integer missionId,@RequestBody MissionRequest missionRequest);

    @PatchMapping(value = UPDATE_EXPENSES_MISSION_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateExpensesMission",
            summary = "update Expenses Mission",
            description = "Modifier l'etat d'une mission, cette methode permet " +
                    "modifier l'etat d' une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le frais de l' objet Mission a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'etat de l'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionResponse> updateExpensesMission(@PathVariable("id") Integer idMission, @RequestBody  List<MissionExpenseRequest> missionExpenseRequests) ;

    @PatchMapping(value = UPDATE_STOP_POINT_MISSION_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateStopPointMission",
            summary = "update Etapes Mission",
            description = "Modifier l'etat d'une mission, cette methode permet " +
                    "modifier l'etat d' une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'etat de l' objet Mission a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'etat de l'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionResponse> updateStopPointMission(@PathVariable("id") Integer idMission, @RequestBody  List<StopPointRequest> stopPointRequests) ;

    @GetMapping(value = FIND_MISSION_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findMissionById",
            description = "Supprimer une Mission par ID! Cette methode permet de suprimmer une mission par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La mission a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La mission avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<MissionResponse> findMissionById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_MISSION_BY_DATE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllMissionByDate",
            description = "Rechercher une Mission par date! Cette methode permet de chercher une mission par une date",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La mission a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La mission avec la date frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<MissionResponse>> findAllMissionByDate(@PathVariable("dateMission") Instant dateMission);

    @GetMapping(value = FIND_ALL_MISSION_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllMission",

            summary = "find All Missions",
            description = "Rechercher tout les Missions! Cette methode permet de retouner toutes les missions",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Missions retrouvées ",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = MissionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<MissionResponse>> findAllMission();

    @DeleteMapping(value = DELETE_MISSION_ENDPOINT)
    @Operation(operationId = "deleteMission",
            description = "Supprimer une Mission par ID! Cette methode permet de suprimmer une mission par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La mission a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La mission avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<?> deleteMission(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_MISSION_PAGEABLE_ENDPOINT ,
            produces =MediaType.APPLICATION_JSON_VALUE )
    @Operation(operationId = "findAllMissionsPageable",
            summary = "find All Missions pageable")
    ResponseEntity<PageResponse<MissionResponse>> findAllMissionsPageable(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );

    @GetMapping(value =FIND_ALL_MISSION_PAGEABLE_BY_DRIVERID_ENDPOINT ,
            produces =MediaType.APPLICATION_JSON_VALUE )
    @Operation(operationId = "findAllMissionsByDriverIdPageable",
            summary = "find All Missions By Driver Id Pageable")
    ResponseEntity<PageResponse<MissionResponse>> findAllMissionsByDriverIdPageable(
       @RequestParam(name = "page",defaultValue = "1",required = false)int page,
       @RequestParam(name = "size",defaultValue = "10",required = false)int size,
       Authentication connectedUser);


    @GetMapping(value =FIND_ALL_MISSION_BY_DRIVER_ENDPOINT ,produces =MediaType.APPLICATION_JSON_VALUE )
    @Operation(operationId = "findAllMissionsByConnectedDriver",
            summary = "find All Missions By Driver"
    )
    ResponseEntity<List<MissionResponse>> findAllMissionsByConnectedDriver(Authentication connectedUser);

}



