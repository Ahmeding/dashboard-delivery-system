package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.VehicleDto;
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

@RequestMapping(VEHICULE_ENDPOINT)
@Tag(name = "Vehicule")
public interface VehiculeApi {

    @PostMapping(value = CREATE_VEHICULE_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Ajouter/Modifier un vehicule, cette methode permet " +
                    "d'enregistrer ou modifier un vehicule",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Vehicule a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VehicleDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Vehicule n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<VehicleDto> save(@RequestBody VehicleDto dto);
    @GetMapping(value = FIND_VEHICULE_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Vehicule par ID! Cette methode permet de chercher un vehicule par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le vehicule a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le vehicule avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<VehicleDto> findById(@PathVariable("id") Integer id);
    @GetMapping(value = FIND_VEHICULE_BY_IMMAT_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Vehicule par son email ID! Cette methode permet de chercher un vehicule par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le vehicule a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le vehicule avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<VehicleDto> findByImmatriculation(@PathVariable("immat") String immat);
    @GetMapping(value = FIND_ALL_VEHICULE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les Vehicule! Cette methode permet de retouner tous les vehicules",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vehicules retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<VehicleDto>> findAll();
    @PutMapping(value = UPDATE_ISACTIVE_VEHICULE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<VehicleDto> updateIsActive(@PathVariable("id") Integer id, @PathVariable("isactive") Boolean isActive);
    @DeleteMapping(value = DELETE_VEHICULE_ENDPOINT)
    @Operation(
            description = "Supprimer un Vehicule par ID! Cette methode permet de suprimmer un vehicule par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le vehicule a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le vehicule avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_VEHICULE_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<VehicleDto>> findAllVehicules(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );

}
