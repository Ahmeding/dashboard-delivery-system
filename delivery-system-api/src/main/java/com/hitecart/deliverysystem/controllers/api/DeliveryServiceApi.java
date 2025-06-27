package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceRequest;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceResponse;
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

@RequestMapping(DELIVERY_SERVICE_ENDPOINT)
@Tag(name = "Delivery Service")
public interface DeliveryServiceApi {


    @PostMapping(
            value = CREATE_DELIVERY_SERVICE_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveDeliveryService",
            description = "Ajouter/Modifier un service delivery , cette methode permet " +
                    "d'enregistrer ou modifier un service delivery",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Service Delivery  a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryServiceResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Service Delivery  n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryServiceResponse> saveDeliveryService(@RequestBody DeliveryServiceRequest deliveryServiceRequest);

    @PatchMapping(
            value = UPDATE_DELIVERY_SERVICE_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateDeliveryServiceInfo",
            description = "Ajouter/Modifier un service delivery , cette methode permet " +
                    "d'enregistrer ou modifier un service delivery",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Service Delivery  a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryServiceResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Service Delivery  n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryServiceResponse> updateDeliveryServiceInfo(@PathVariable("deliveryServiceId") Integer deliveryServiceId,@RequestBody DeliveryServiceRequest dto);

    @GetMapping(
            value = FIND_DELIVERY_SERVICE_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findDeliveryServiceById",
            description = "Rechercher un Service Delivery  par ID! Cette methode permet de chercher un commande client  par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le Service Delivery  a été trouvée dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryServiceResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le Service Delivery  avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DeliveryServiceResponse> findDeliveryServiceById(@PathVariable("id") Integer id);

    @GetMapping(
            value = FIND_ALL_DELIVERY_SERVICE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllDeliveryService",
            description = "Rechercher tout les DeliveryService ! Cette methode permet de retouner toutes les DeliveryService",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "DeliveryService retrouvées ",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DeliveryServiceResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<DeliveryServiceResponse>> findAllDeliveryService();

    @PutMapping(
            value = UPDATE_DELIVERY_SERVICE_ENABLED_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateDeliveryServiceEnabled",
            description = "update un Service Delivery  par ID! Cette methode permet de maj un service delivery  par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le Service Delivery  a été maj dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryServiceResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le Service Delivery  avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DeliveryServiceResponse> updateDeliveryServiceEnabled(@PathVariable("id") Integer id, @PathVariable("enable") boolean enabled);

    @GetMapping(
            value = FIND_ALL_BY_ENABLED_DELIVERY_SERVICE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllEnabledDeliveryService",
            description = "Rechercher un Service Delivery  par son etat! Cette methode permet de chercher un service delivery  par son etat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le Service Delivery  a été trouvée dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryServiceResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le Service Delivery  avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<DeliveryServiceResponse>> findAllEnabledDeliveryService(@PathVariable("enable") boolean enabled);

    @GetMapping(value = FIND_ALL_DELIVERY_SERVICE_PAGEABLE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllDeliveryServicesPageable")
    ResponseEntity<PageResponse<DeliveryServiceResponse>> findAllDeliveryServicesPageable(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size);

    @DeleteMapping(value = DELETE_DELIVERY_SERVICE_ENDPOINT)
    @Operation(operationId = "deleteDeliveryService",
            description = "Supprimer un Delivery Service par ID! Cette methode permet de suprimmer un delivery service par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le delivery service a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le delivery service avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<?> deleteDeliveryService(@PathVariable("id") Integer id);

}
