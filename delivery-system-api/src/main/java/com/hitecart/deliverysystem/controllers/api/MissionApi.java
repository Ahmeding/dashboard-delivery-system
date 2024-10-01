package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.MissionDto;
import com.hitecart.deliverysystem.dto.MissionToSaveRequest;
import com.hitecart.deliverysystem.model.enums.EtatMission;
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
            description = "Rechercher tout les Missions! Cette methode permet de retouner toutes les missions",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Missions retrouvées ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<MissionDto>> findAll();
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
}
