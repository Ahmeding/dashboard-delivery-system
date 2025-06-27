package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import com.hitecart.deliverysystem.dto.sparepart.SparePartResponse;
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

@RequestMapping(SPARE_PART_ENDPOINT)
@Tag(name = "Spare Part")
public interface SparePartApi {

    @PostMapping(value = CREATE_SPARE_PART_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(operationId = "saveSparePart",
            description = "Ajouter/Modifier une piece de rechange, cette methode permet " +
                    "d'enregistrer ou modifier une piece de rechange",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet SparePart a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SparePartResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet SparePart n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<SparePartResponse> saveSparePart(@RequestBody SparePartRequest sparePartRequest);

    @PatchMapping(value = UPDATE_SPARE_PART_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateSparePartInfo",
            description = "Ajouter/Modifier une piece de rechange, cette methode permet " +
                    "d'enregistrer ou modifier une piece de rechange",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet SparePart a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SparePartResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet SparePart n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<SparePartResponse> updateSparePartInfo(@PathVariable("sparePartId")Integer sparePartId ,@RequestBody SparePartRequest request);

    @GetMapping(value = FIND_SPARE_PART_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findSparePartById",
            description = "Rechercher une SparePart par ID! Cette methode permet de chercher une piece de rechange par son ID",
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
    ResponseEntity<SparePartResponse> findSparePartById(@PathVariable("id") Integer id);
    
    

    @GetMapping(value = FIND_SPARE_PART_BY_REF_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findSparePartByReference",
            description = "Rechercher une SparePart par reference ! Cette methode permet de chercher une piece de rechange par sa reference",
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
    ResponseEntity<SparePartResponse> findSparePartByReference(@PathVariable("ref") String ref);
    
    

    @GetMapping(value = FIND_ALL_SPARE_PART_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllSparePart",
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
    ResponseEntity<List<SparePartResponse>> findAllSparePart();
    
    

    @DeleteMapping(value = DELETE_SPARE_PART_ENDPOINT)
    @Operation(operationId = "deleteSparePart",
            description = "Supprimer une SparePart par ID! Cette methode permet de suprimmer une piece de rechange par son ID",
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
     ResponseEntity<?> deleteSparePart(@PathVariable("id") Integer id);
    
    

    @GetMapping(value = FIND_ALL_SPARE_PART_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    @Operation(operationId = "findAllSparePartsPageable")
    ResponseEntity<PageResponse<SparePartResponse>> findAllSparePartsPageable(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
