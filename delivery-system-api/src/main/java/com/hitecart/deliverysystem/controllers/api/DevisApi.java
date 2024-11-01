package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.dto.DevisDto;
import com.hitecart.deliverysystem.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(DEVIS_ENDPOINT)
@Tag(name = "Devis")
public interface DevisApi {
    @PostMapping(value = CREATE_DEVIS_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Ajouter/Modifier un devis, cette methode permet " +
                    "d'enregistrer ou modifier un devis",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Devis a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DevisDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Devis n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DevisDto> save(@RequestBody DevisDto dto);

    @GetMapping(value = FIND_DEVIS_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Devis par ID! Cette methode permet de chercher un devis par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le devis a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DevisDtoResponse.class))
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le devis avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DevisDtoResponse> findById(@PathVariable("id") Integer id);
    @GetMapping(value = FIND_DEVIS_BY_CODE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Devis par code! Cette methode permet de chercher un devis par son code",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le devis a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DevisDtoResponse.class)
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le devis avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DevisDtoResponse> findByCode(@PathVariable("code") String code);
    @GetMapping(value = FIND_ALL_DEVIS_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les Devis! Cette methode permet de retouner tous les devis",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deviss retrouvés ",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DevisDtoResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<List<DevisDtoResponse>> findAll();
    @DeleteMapping(value = DELETE_DEVIS_ENDPOINT)
    @Operation(
            description = "Supprimer un Devis par ID! Cette methode permet de suprimmer un devis par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le devis a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le devis avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_DEVIS_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<DevisDtoResponse>> findAllDevis(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
