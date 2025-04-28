package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.MissionDto;
import com.hitecart.deliverysystem.dto.basic.MissionResponse;
import com.hitecart.deliverysystem.model.enums.EtatMission;
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
    @Operation(
            description = "Ajouter/Modifier une mission, cette methode permet " +
                    "d'enregistrer ou modifier une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Mission a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json"
                                    //schema = @Schema(implementation = MissionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionDto> save(@RequestBody MissionDto request);

    @PutMapping(value = UPDATE_MISSION_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Modifier une mission, cette methode permet " +
                    "modifier une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Mission a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json"
                                    //schema = @Schema(implementation = MissionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionDto> update(@PathVariable("id") Integer idMission,@RequestBody MissionDto request);

    @PatchMapping(value = UPDATE_ETAT_MISSION_ENDPOINT)
    @Operation(
            description = "Modifier l'etat d'une mission, cette methode permet " +
                    "modifier l'etat d' une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'etat de l' objet Mission a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'etat de l'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionDto> updateEtatMission(@PathVariable("id") Integer idMission,@PathVariable("etatMission") EtatMission etatMission);

    @PatchMapping(value = UPDATE_KILOMETRATGE_MISSION_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Mettre à jour le kilomtratge debut/fin d'une mission, cette methode permet " +
                    "Mettre à jour le kilomtratge debut/fin d' une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le kilomtratge de l' objet Mission a été mis à jour",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le kilomtratge de l'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionDto> updateKilometrage(@PathVariable("flag") String flag, @PathVariable("id") Integer idMission,@PathVariable("kilometrage") double kilometrage);

    @GetMapping(value = FIND_MISSION_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
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
    ResponseEntity<MissionDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_MISSION_BY_DATE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
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
    ResponseEntity<List<MissionDto>> findAllByDate(@PathVariable("dateMission") Instant dateMission);
    @GetMapping(value = FIND_ALL_MISSION_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(

            summary = "find All Missions",
            operationId = "findAll" ,  // ← stable, custom ID
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
    ResponseEntity<List<MissionResponse>> findAll();

    @DeleteMapping(value = DELETE_MISSION_ENDPOINT)
    @Operation(
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
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value = GENER_MISSION_ENDPOINT)
    void generateMissions();

    @GetMapping(value = GENER_ASSIGN_ENDPOINT)
    void generateWeeklyAssignments();

    @Operation(
            summary = "find All Missions pageable",
            operationId = "findAllMissions"   // ← stable, custom ID
    )
    @GetMapping(value =FIND_ALL_MISSION_PAGEABLE_ENDPOINT ,
            produces =MediaType.APPLICATION_JSON_VALUE )
    ResponseEntity<PageResponse<MissionDto>> findAllMissions(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );

    @Operation(
            summary = "find All Missions By Driver Id Pageable",
            operationId = "findAllMissionsByDriverIdPageable"   // ← stable, custom ID
    )
    @GetMapping(value =FIND_ALL_MISSION_PAGEABLE_BY_DRIVERID_ENDPOINT ,
            produces =MediaType.APPLICATION_JSON_VALUE )
    ResponseEntity<PageResponse<MissionDto>> findAllMissionsByDriverIdPageable(
       @RequestParam(name = "page",defaultValue = "1",required = false)int page,
       @RequestParam(name = "size",defaultValue = "10",required = false)int size,
       Authentication connectedUser
    );
    @Operation(
            summary = "find All Missions By Driver",
            operationId = "findAllMissionsByDriver"   // ← stable, custom ID
    )
    @GetMapping(value =FIND_ALL_MISSION_BY_DRIVER_ENDPOINT ,
            produces =MediaType.APPLICATION_JSON_VALUE )
    ResponseEntity<List<MissionResponse>> findAllMissionsByDriver(Authentication connectedUser);


    @PostMapping(value =CHECK_OVERLAPPING_VEHICLES_ENDPOINT )
    ResponseEntity<?> checkOverlappingVehicles(@RequestBody MissionDto dto);
    @PostMapping(value =CHECK_OVERLAPPING_DRIVERS_ENDPOINT )
    ResponseEntity<?> checkOverlappingDrivers(@RequestBody MissionDto dto);
}
