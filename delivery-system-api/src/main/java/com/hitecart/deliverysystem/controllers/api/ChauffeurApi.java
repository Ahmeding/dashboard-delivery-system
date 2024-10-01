package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.ChauffeurDto;
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

@RequestMapping(CHAUFFEUR_ENDPOINT)
@Tag(name = "Chauffeur")
public interface ChauffeurApi {

    @PostMapping(
            value = CREATE_CHAUFFEUR_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Ajouter/Modifier un chauffeur, cette methode permet " +
                    "d'enregistrer ou modifier un chauffreur",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Chauffeur a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ChauffeurDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Chauffeur n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<ChauffeurDto> save(@RequestBody ChauffeurDto dto);
    @GetMapping(
            value = FIND_CHAUFFEUR_BY_ID_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE )
    @Operation(
            description = "Rechercher un Chauffeur par ID! Cette methode permet de chercher un chauffeur par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le chauffeur a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le chauffeur avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<ChauffeurDto> findById(@PathVariable("id") Integer id);
    @GetMapping(
            value = FIND_CHAUFFEUR_BY_EMAIL_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Chauffeur par son email ID! Cette methode permet de chercher un chauffeur par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le chauffeur a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le chauffeur avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<ChauffeurDto> findByEmail(@PathVariable("email") String email);
    @GetMapping(
            value = FIND_ALL_CHAUFFEUR_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les Chauffeur! Cette methode permet de retouner tous les chauffeurs",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Chauffeurs retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<ChauffeurDto>> findAll();
    @DeleteMapping(value = DELETE_CHAUFFEUR_ENDPOINT)
    @Operation(
            description = "Supprimer un Chauffeur par ID! Cette methode permet de suprimmer un chauffeur par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le chauffeur a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le chauffeur avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(Integer id);
}
