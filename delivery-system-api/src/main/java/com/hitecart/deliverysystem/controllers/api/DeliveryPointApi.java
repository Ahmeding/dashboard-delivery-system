package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointRequest;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointResponse;
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
@Tag(name = "Delivery Points")
public interface DeliveryPointApi {

    @PostMapping(
            value = CREATE_DELIVERY_POINT_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "saveDeliveryPoint",

            description = "Ajouter/Modifier un deliveryPoint, cette methode permet " +
                    "d'enregistrer ou modifier un deliveryPoint",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet DeliveryPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryPointResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet DeliveryPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryPointResponse> saveDeliveryPoint(@RequestBody DeliveryPointRequest dto);

    @PatchMapping(
            value = UPDATE_DELIVERY_POINT_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "updateDeliveryPointInfo",

            description = "Ajouter/Modifier un deliveryPoint, cette methode permet " +
                    "d'enregistrer ou modifier un deliveryPoint",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet DeliveryPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryPointResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet DeliveryPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryPointResponse> updateDeliveryPointInfo(
            @PathVariable("deliveryPointId")Integer deliveryPointId,@RequestBody DeliveryPointRequest deliveryPointRequest);

    @GetMapping(
            value = FIND_DELIVERY_POINT_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(operationId = "findDeliveryPointById",

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
    ResponseEntity<DeliveryPointResponse> findDeliveryPointById(@PathVariable("id") Integer id);

    @GetMapping(
            value = FIND_DELIVERY_POINT_BY_CITY_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findAllDeliveryPointByCity",

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
    ResponseEntity<List<DeliveryPointResponse>> findAllDeliveryPointByCity(@PathVariable("city") String city);
    @GetMapping(
            value = FIND_ALL_DELIVERY_POINT_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findAllDeliveryPoint",

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
    ResponseEntity<List<DeliveryPointResponse>> findAllDeliveryPoint();

    @PutMapping(value = UPDATE_ISACTIVE_DELIVERY_POINT_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateDeliveryPointIsActive"
    )
    ResponseEntity<DeliveryPointResponse> updateDeliveryPointIsActive(@PathVariable("id") Integer id, @PathVariable("isactive") Boolean isActive);

    @DeleteMapping(value = DELETE_DELIVERY_POINT_ENDPOINT)
    @Operation(operationId = "deleteDeliveryPoint",

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
    ResponseEntity<?> deleteDeliveryPoint(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_DELIVERY_POINT_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    @Operation(operationId = "findAllDeliveryPointsPageable"
)
    ResponseEntity<PageResponse<DeliveryPointResponse>> findAllDeliveryPointsPageable(
      @RequestParam(name = "page",defaultValue = "1", required = false) int page,
      @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
