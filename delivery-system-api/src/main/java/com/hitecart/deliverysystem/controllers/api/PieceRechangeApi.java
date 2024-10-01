package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.PieceRechangeDto;
import com.hitecart.deliverysystem.dto.PieceRechangeDto;
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

@RequestMapping(PIECE_RECHANGE_ENDPOINT)
@Tag(name = "Pieces de Rechange")
public interface PieceRechangeApi {

    @PostMapping(value = CREATE_PIECE_RECHANGE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Ajouter/Modifier une piece de rechange, cette methode permet " +
                    "d'enregistrer ou modifier une piece de rechange",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet PieceRechange a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PieceRechangeDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet PieceRechange n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<PieceRechangeDto> save(@RequestBody PieceRechangeDto dto);

    @GetMapping(value = FIND_PIECE_RECHANGE_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher une PieceRechange par ID! Cette methode permet de chercher une piece de rechange par son ID",
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
    ResponseEntity<PieceRechangeDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_PIECE_RECHANGE_BY_REF_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher une PieceRechange par reference ! Cette methode permet de chercher une piece de rechange par sa reference",
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
                            description = "La Piece de Rechange avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<PieceRechangeDto> findByReference(@PathVariable("ref") String ref);

    @GetMapping(value = FIND_ALL_PIECE_RECHANGE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les PieceRechanges ! Cette methode permet de retouner tous les pieces de Rechange",
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
    ResponseEntity<List<PieceRechangeDto>> findAll();

    @DeleteMapping(value = DELETE_PIECE_RECHANGE_ENDPOINT)
    @Operation(
            description = "Supprimer une PieceRechange par ID! Cette methode permet de suprimmer une piece de rechange par son ID",
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
    void delete(@PathVariable("id") Integer id);
}
