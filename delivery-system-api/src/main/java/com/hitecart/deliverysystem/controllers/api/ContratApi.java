package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.dto.ContratDto;
import com.hitecart.deliverysystem.dto.ContratDto;
import com.hitecart.deliverysystem.utils.Constants;
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

@RequestMapping(CONTRAT_ENDPOINT)
@Tag( name = "Contrat")
public interface ContratApi {

    @PostMapping(value = CREATE_CONTRAT_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Ajouter/Modifier un contrat, cette methode permet " +
                    "d'enregistrer ou modifier un contrat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Contrat a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContratDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Contrat n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<ContratDto> save(@RequestBody ContratDto dto);

    @GetMapping(value = FIND_CONTRAT_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Contrat par ID! Cette methode permet de chercher un contrat par son ID",
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
    ResponseEntity<ContratDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_CONTRAT_BY_IDCHAUFFEUR_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Contrat par l'ID de chauffeur! Cette methode permet de chercher un contrat par l'ID du chauffeur",
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
                            description = "Le contrat avec l'ID du chauffeur frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<ContratDto>> findAllByIdChauffeur(@PathVariable("idChauffeur") Integer idContrat);

    @GetMapping(value = FIND_ALL_CONTRAT_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les Contrat! Cette methode permet de retouner tous les contrats",
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
    ResponseEntity<List<ContratDto>> findAll();
    @DeleteMapping(value = DELETE_CONTRAT_ENDPOINT)
    @Operation(
            description = "Supprimer un Contrat par ID! Cette methode permet de suprimmer un contrat par son ID",
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
    void delete(@PathVariable("id") Integer id);
    @GetMapping(value =FIND_ALL_CONTRAT_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<ContratDto>> findAllContrats(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
