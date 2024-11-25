package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.DeliveryPointDto;
import com.hitecart.deliverysystem.dto.DeliveryPointDto;
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

@RequestMapping(DELIVERY_POINT_ENDPOINT)
@Tag(name = "delivery-points")
public interface DeliveryPointApi {

    @PostMapping(
            value = CREATE_DELIVERY_POINT_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            description = "Ajouter/Modifier un deliveryPoint, cette methode permet " +
                    "d'enregistrer ou modifier un deliveryPoint",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet DeliveryPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryPointDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet DeliveryPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryPointDto> save(@RequestBody DeliveryPointDto dto);

    @GetMapping(
            value = FIND_DELIVERY_POINT_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(
            description = "Rechercher un DeliveryPoint par ID! Cette methode permet de chercher un deliveryPoint par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le deliveryPoint a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le deliveryPoint avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DeliveryPointDto> findById(@PathVariable("id") Integer id);
    @GetMapping(
            value = FIND_DELIVERY_POINT_BY_CITY_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            description = "Rechercher un DeliveryPoint par son email ID! Cette methode permet de chercher un deliveryPoint par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le deliveryPoint a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le deliveryPoint avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<DeliveryPointDto>> findAllByCity(@PathVariable("city") String city);
    @GetMapping(
            value = FIND_ALL_DELIVERY_POINT_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            description = "Rechercher tout les DeliveryPoint! Cette methode permet de retouner tous les deliveryPoints",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "DeliveryPoints retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<DeliveryPointDto>> findAll();

    @PutMapping(value = UPDATE_ISACTIVE_DELIVERY_POINT_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DeliveryPointDto> updateIsActive(@PathVariable("id") Integer id, @PathVariable("isactive") Boolean isActive);

    @DeleteMapping(value = DELETE_DELIVERY_POINT_ENDPOINT)
    @Operation(
            description = "Supprimer un DeliveryPoint par ID! Cette methode permet de suprimmer un deliveryPoint par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le deliveryPoint a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le deliveryPoint avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_DELIVERY_POINT_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<DeliveryPointDto>> findAllDeliveryPoints(
      @RequestParam(name = "page",defaultValue = "1", required = false) int page,
      @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
