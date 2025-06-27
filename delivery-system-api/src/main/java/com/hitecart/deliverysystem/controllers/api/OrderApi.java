package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.order.OrderRequest;
import com.hitecart.deliverysystem.dto.order.OrderResponse;
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

@RequestMapping(ORDER_ENDPOINT)
@Tag(name = "Order")
public interface OrderApi {

    @PostMapping(value = CREATE_ORDER_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(operationId = "saveOrder",
            description = "Ajouter/Modifier un commande client , cette methode permet " +
                    "d'enregistrer ou modifier un chauffreur",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Commande Client  a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Order  n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<OrderResponse> saveOrder(
            @RequestBody OrderRequest orderRequest
    );

    @PatchMapping(value = UPDATE_ORDER_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(operationId = "updateOrderInfo",
            description = "Ajouter/Modifier un commande client , cette methode permet " +
                    "d'enregistrer ou modifier un chauffreur",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Commande Client  a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Order  n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<OrderResponse> updateOrderInfo(@PathVariable("orderId") Integer orderId ,@RequestBody OrderRequest orderRequest);

    @GetMapping(value = FIND_ORDER_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findOrderById",
            description = "Rechercher un Order  par ID! Cette methode permet de chercher un commande client  par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La commande client  a été trouvée dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class)
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La commande client  avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<OrderResponse> findOrderById(@PathVariable("id") Integer id);


    @GetMapping(value = FIND_ORDER_BY_CODE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findOrderByCode",
            description = "Rechercher un Commande Client  par CODE ! Cette methode permet de chercher une commande client  par son code",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La commande client  a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class)
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La commande client  avec le code frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<OrderResponse> findOrderByCode(@PathVariable("code") String code);


    @GetMapping(value = FIND_ALL_ORDER_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllOrder",
            description = "Rechercher tout les Order ! Cette methode permet de retouner toutes les commandes clients",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order retrouvées ",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<OrderResponse>> findAllOrder();


    @DeleteMapping(value = DELETE_ORDER_ENDPOINT)
    @Operation(operationId = "deleteOrder",
            description = "Supprimer un Order  par ID! Cette methode permet de suprimmer un commande client  par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La commande client  a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La commande client  avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
     ResponseEntity<?> deleteOrder(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_ALL_ORDER_PAGEABLE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            operationId = "findAllOrdersPageable",
            description = "Rechercher tout les Order ! Cette methode permet de retouner toutes les commandes clients"
/*            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order retrouvées ",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class))
                            )
                    )
            }*/
    )
    ResponseEntity<PageResponse<OrderResponse>> findAllOrdersPageable(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    );
}
