package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.EntrepriseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(ENTREPRISE_ENDPOINT)
public interface EntrepriseApi {

    @PostMapping(value = CREATE_ENTREPRISE_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Ajouter/Modifier une entreprise, cette methode permet " +
                    "d'enregistrer ou modifier une entreprise",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Entreprise a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EntrepriseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Entreprise n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<EntrepriseDto> save(@RequestBody EntrepriseDto dto);

    @GetMapping(value = FIND_ENTREPRISE_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher une Entreprise par ID! Cette methode permet de chercher une entreprise par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le entreprise a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le entreprise avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<EntrepriseDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_ENTREPRISE_BY_RSOCIAL_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher une Entreprise par nom! Cette methode permet de chercher une entreprise par son nom",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le entreprise a été trouvée dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le entreprise avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<EntrepriseDto> findByRaisonSocial(@PathVariable("rsocial") String raisonSocial);

    @GetMapping(value = FIND_ALL_ENTREPRISE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les Entreprise! Cette methode permet de retouner tous les entreprises",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Entreprises retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<EntrepriseDto>> findAll();

    @DeleteMapping(value = DELETE_ENTREPRISE_ENDPOINT)
    @Operation(
            description = "Supprimer une Entreprise par ID! Cette methode permet de suprimmer une entreprise par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le entreprise a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le entreprise avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_ENTREPRISE_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<EntrepriseDto>> findAllEntreprises(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
