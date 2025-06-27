package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationRequest;
import com.hitecart.deliverysystem.dto.deliverydestination.DeliveryDestinationResponse;
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

@RequestMapping(DELIVERY_DESTINATION_ENDPOINT)
@Tag( name = "DELIVERY DESTINATION")
public interface DeliveryDestinationApi {


    @PostMapping(
            value = CREATE_DELIVERY_DESTINATION_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveDeliveryDestination",

            description = "Ajouter/Modifier une delivery_destination, cette methode permet " +
                    "d'enregistrer ou modifier une delivery_destination",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet DeliveryPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryDestinationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet DeliveryPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryDestinationResponse> saveDeliveryDestination(@RequestBody DeliveryDestinationRequest deliveryDestinationRequest);

    @PatchMapping(
            value = UPDATE_DELIVERY_DESTINATION_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateDeliveryDestinationInfo",

            description = "Modifier une delivery_destination, cette methode permet " +
                    "d'enregistrer ou modifier une delivery_destination",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet DeliveryPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryDestinationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet DeliveryPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryDestinationResponse> updateDeliveryDestinationInfo(
            @PathVariable("deliveryDestinationId")Integer deliveryDestinationId,@RequestBody DeliveryDestinationRequest dto);

    @GetMapping(
            value = FIND_DELIVERY_DESTINATION_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findDeliveryDestinationById",

            description = "Rechercher un DeliveryPoint par ID! Cette methode permet de chercher une delivery_destination par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La delivery_destination a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La delivery_destination avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DeliveryDestinationResponse> findDeliveryDestinationById(@PathVariable("id") Integer id);

    @GetMapping(
            value = FIND_DELIVERY_DESTINATION_BY_CLIENT_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findAllDeliveryDestinationByClientId",

            description = "Rechercher un DeliveryPoint par client ID! Cette methode permet de chercher une delivery_destination par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La delivery_destination a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le delivery_destination avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<DeliveryDestinationResponse>> findAllDeliveryDestinationByClientId(@PathVariable("clientId") Integer clientId);

    @GetMapping(
            value = FIND_ALL_DELIVERY_DESTINATION_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findAllDeliveryDestination",

            description = "Rechercher tout les DeliveryPoint! Cette methode permet de retouner tous les delivery_destinations",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "DeliveryPoints retrouvés ",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DeliveryDestinationResponse.class))

                            )
                    )
            }
    )
    ResponseEntity<List<DeliveryDestinationResponse>> findAllDeliveryDestination();

    @DeleteMapping(value = DELETE_DELIVERY_DESTINATION_ENDPOINT)
    @Operation(operationId = "deleteDeliveryDestination",

            description = "Supprimer un DeliveryPoint par ID! Cette methode permet de suprimmer un delivery_destination par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le delivery_destination a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le delivery_destination avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<?> deleteDeliveryDestination(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_DELIVERY_DESTINATION_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    @Operation(operationId = "findAllDeliveryDestinationsPageable"
)
    ResponseEntity<PageResponse<DeliveryDestinationResponse>> findAllDeliveryDestinationsPageable(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
