package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.FraisMissionDto;
import com.hitecart.deliverysystem.dto.basic.LigneFraisMissionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;
import static com.hitecart.deliverysystem.utils.Constants.FIND_FRAISMISSION_BY_ID_ENDPOINT;

@RequestMapping(FRAISMISSION_ENDPOINT)
@Tag(name = "Frais de Mission")
public interface FraisMissionApi {

    @PostMapping(value = CREATE_FRAISMISSION_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Ajouter/Modifier un fraisMission, cette methode permet " +
                    "d'enregistrer ou modifier un fraisMission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet FraisMission a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FraisMissionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet FraisMission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<FraisMissionDto> save(@RequestBody FraisMissionDto dto);
    @PatchMapping(value = UPDATE_FRAISMISSION_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Modifier un fraisMission, cette methode permet " +
                    "de modifier un fraisMission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet FraisMission a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FraisMissionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet FraisMission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<FraisMissionDto> updateFraisMission(@RequestBody FraisMissionDto dto);
    @PatchMapping(value = UPDATE_QTE_LIGNE_FRAISMISSION_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Modifier la qte dans une ligne fraisMission, cette methode permet " +
                    "de la qte modifier dans une ligne fraisMission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La qte a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FraisMissionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La qte n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<FraisMissionDto> updateQuantite(@PathVariable("idFM") Integer idMF, @PathVariable("idLigneFM")Integer idLigneFM, @PathVariable("qte")BigDecimal qte);
    @DeleteMapping(value = DELETE_LIGNE_FRAISMISSION_ENDPOINT)

    @Operation(
            description = "Supprimer une ligne du fraisMission, cette methode permet " +
                    "de supprimer une ligne  du fraisMission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La ligne a été supprimer",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FraisMissionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'action de suppression a echoué",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<FraisMissionDto> deleteLigneFraisMission(@PathVariable("idFM")Integer idFraisMission, @PathVariable("idLigneFM")Integer idLigneFraisMission);
    @GetMapping(value = FIND_FRAISMISSION_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un FraisMission par ID! Cette methode permet de chercher un fraisMission par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le fraisMission a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le fraisMission avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<FraisMissionDto> findById(@PathVariable("id") Integer id);
    @GetMapping(value = FIND_FRAISMISSION_BY_IDMISSION_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un FraisMission par l'ID de mission! Cette methode permet de chercher un fraisMission par l'ID de mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le fraisMission a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le fraisMission avec l'ID  de mission frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<FraisMissionDto> findByIdMission(@PathVariable("idMission") Integer idMission);
    @GetMapping(value = FIND_ALL_LIGNESFRAISMISSION_BY_IDFRAISMISSION_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher les lignes du frais de mission par l'ID de mission! Cette methode permet de chercher tous les lignes du fraisMission par l'ID de mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Les lignes du fraisMission a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Les lignes du  fraisMission avec l'ID  frais de mission frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<LigneFraisMissionDto>> findAllLignesFraisMissionByFraisMissionId(@PathVariable("idFM")Integer idFraisMission);
    @GetMapping(value = FIND_ALL_FRAISMISSION_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les FraisMission! Cette methode permet de retouner tous les fraisMissions",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "FraisMissions retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<FraisMissionDto>> findAll();
    @DeleteMapping(value = DELETE_FRAISMISSION_ENDPOINT)
    @Operation(
            description = "Supprimer un FraisMission par ID! Cette methode permet de suprimmer un fraisMission par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le fraisMission a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le fraisMission avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_FRAISMISSION_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<FraisMissionDto>> findAllFraisMissions(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
