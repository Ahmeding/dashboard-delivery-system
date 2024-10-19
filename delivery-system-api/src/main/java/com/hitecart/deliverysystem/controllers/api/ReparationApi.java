package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.dto.ReparationDto;
import com.hitecart.deliverysystem.dto.ReparationDto;
import com.hitecart.deliverysystem.utils.Constants;
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

@RequestMapping(REPARATION_ENDPOINT)
@Tag(name = "Reparation")
public interface ReparationApi {

    @PostMapping(value = CREATE_REPARATION_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Ajouter/Modifier une reparation, cette methode permet " +
                    "d'enregistrer ou modifier une reparattion",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Reparation a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReparationDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Reparation n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<ReparationDto> save(@RequestBody ReparationDto dto);

    @GetMapping(value = FIND_REPARATION_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher une Reparation par ID! Cette methode permet de chercher une reparation par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La reparation a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La reparation avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<ReparationDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_REPARATION_BY_DATE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher une Reparation par DATE ! Cette methode permet de chercher une reparation par date",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La reparation a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La reparation avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<ReparationDto>> findByDate(@PathVariable("dateRep") Instant dateRep);

    @GetMapping(value = FIND_ALL_REPARATION_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les Reparation! Cette methode permet de retouner toutes les reparations",
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
    ResponseEntity<List<ReparationDto>> findAll();

    @DeleteMapping(value = DELETE_REPARATION_ENDPOINT)
    @Operation(
            description = "Supprimer une Reparation par ID! Cette methode permet de suprimmer une reparation par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La reparation a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La reparation avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_REPARATION_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<ReparationDto>> findAllReparations(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
