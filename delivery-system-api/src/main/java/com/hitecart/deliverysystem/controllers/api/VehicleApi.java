package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.vehicle.VehicleRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;
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

@RequestMapping(VEHICLE_ENDPOINT)
@Tag(name = "Vehicle")
public interface VehicleApi {

    @PostMapping(value = CREATE_VEHICLE_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveVehicle",
            description = "Ajouter/Modifier un vehicle, cette methode permet " +
                    "d'enregistrer ou modifier un vehicle",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Vehicle a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VehicleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Vehicle n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<VehicleResponse> saveVehicle(@RequestBody VehicleRequest vehicleRequest);


    @PatchMapping(value = UPDATE_VEHICLE_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateVehicleInfo",
            description = "Ajouter/Modifier un vehicle, cette methode permet " +
                    "d'enregistrer ou modifier un vehicle",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Vehicle a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VehicleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Vehicle n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<VehicleResponse> updateVehicleInfo(@PathVariable("vehicleId") Integer vehicleId, @RequestBody  VehicleRequest request);



    @GetMapping(value = FIND_VEHICLE_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findVehicleById",
            description = "Rechercher un Vehicle par ID! Cette methode permet de chercher un vehicle par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le vehicle a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le vehicle avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<VehicleResponse> findVehicleById(@PathVariable("id") Integer id);



    @GetMapping(value = FIND_VEHICLE_BY_REGIS_NBR_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findVehicleByRegisterNbr",
            description = "Rechercher un Vehicle par son email ID! Cette methode permet de chercher un vehicle par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le vehicle a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le vehicle avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<VehicleResponse> findVehicleByRegisterNbr(@PathVariable("regisNbr") String regisNbr);



    @GetMapping(value = FIND_ALL_VEHICLE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllVehicle",
            description = "Rechercher tout les Vehicle! Cette methode permet de retouner tous les vehicules",
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
    ResponseEntity<List<VehicleResponse>> findAllVehicle();



    @PutMapping(value = UPDATE_ISACTIVE_VEHICLE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateVehicleIsActive")
    ResponseEntity<VehicleResponse> updateVehicleIsActive(@PathVariable("id") Integer id, @PathVariable("isactive") Boolean isActive);



    @DeleteMapping(value = DELETE_VEHICLE_ENDPOINT)
    @Operation(operationId = "deleteVehicle",
            description = "Supprimer un Vehicle par ID! Cette methode permet de suprimmer un vehicle par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le vehicle a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le vehicle avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
     ResponseEntity<?> deleteVehicle(@PathVariable("id") Integer id);



    @GetMapping(value =FIND_ALL_VEHICLE_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    @Operation(operationId = "findAllVehiclesPageable")
    ResponseEntity<PageResponse<VehicleResponse>> findAllVehiclesPageable(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );

}
