package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ChauffeurDto;
import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.dto.UtilisateurDto;
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

@RequestMapping(UTILISATEUR_ENDPOINT)
@Tag(name = "Utilisateur")
public interface UtilisateurApi {

    @PostMapping(value = CREATE_UTILISATEUR_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Ajouter/Modifier un utilisateur, cette methode permet " +
                    "d'enregistrer ou modifier un utilisateur",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Utilisateur a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ChauffeurDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Utilisateur n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<UtilisateurDto> save(@RequestBody UtilisateurDto dto);

    @GetMapping(value = FIND_UTILISATEUR_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Utilisateur par ID! Cette methode permet de chercher un utilisateur par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le utilisateur a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le utilisateur avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<UtilisateurDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_UTILISATEUR_BY_EMAIL_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Utilisateur par son email ! Cette methode permet de chercher un utilisateur par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le utilisateur a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le utilisateur avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<UtilisateurDto> findByEmail(@PathVariable("email") String email);

    @GetMapping(value = FIND_ALL_UTILISATEUR_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les Utilisateur! Cette methode permet de retouner tous les chauffeurs",
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
    ResponseEntity<List<UtilisateurDto>> findAll();

    @DeleteMapping(value = DELETE_UTILISATEUR_ENDPOINT)
    @Operation(
            description = "Supprimer un Utilisateur par ID! Cette methode permet de suprimmer un utilisateur par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le utilisateur a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le utilisateur avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_UTILISATEUR_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<UtilisateurDto>> findAllUtilisateurs(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
