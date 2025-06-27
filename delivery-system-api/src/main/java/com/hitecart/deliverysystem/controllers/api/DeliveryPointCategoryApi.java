package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCategoryRequest;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCatResponse;
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

@RequestMapping(DELIVERY_POINT_CATEGORY_ENDPOINT)
@Tag(name = "delivery-point-categories")
public interface DeliveryPointCategoryApi {

    @PostMapping(
            value = CREATE_DELIVERY_POINT_CATEGORY_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "saveDeliveryPointCategory",

            description = "Ajouter/Modifier une delivery Point categroy, cette methode permet " +
                    "d'enregistrer ou modifier un delivery Point categroy",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet DeliveryPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryPointCatResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet DeliveryPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryPointCatResponse> saveDeliveryPointCategory(@RequestBody DeliveryPointCategoryRequest deliveryPointCategoryRequest);

    @PatchMapping(
            value = UPDATE_DELIVERY_POINT_CATEGORY_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "updateDeliveryPointCategoryInfo",

            description = "Ajouter/Modifier une delivery Point categroy, cette methode permet " +
                    "d'enregistrer ou modifier un delivery Point categroy",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet DeliveryPoint a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeliveryPointCatResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet DeliveryPoint n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<DeliveryPointCatResponse> updateDeliveryPointCategoryInfo(@PathVariable("deliveryPointCatId") Integer deliveryPointCatId,@RequestBody DeliveryPointCategoryRequest dto);

    @GetMapping(
            value = FIND_DELIVERY_POINT_CATEGORY_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(operationId = "findDeliveryPointCategoryById",

            description = "Rechercher un DeliveryPoint par ID! Cette methode permet de chercher un delivery Point categroy par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le delivery Point categroy a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le delivery Point categroy avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DeliveryPointCatResponse> findDeliveryPointCategoryById(@PathVariable("id") Integer id);

    @GetMapping(
            value = FIND_DELIVERY_POINT_CATEGORY_BY_NAME_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findDeliveryPointCategoryByName",

            description = "Rechercher un DeliveryPoint par son ID! Cette methode permet de chercher un delivery Point categroy par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le delivery Point categroy a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le delivery Point categroy avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DeliveryPointCatResponse> findDeliveryPointCategoryByName(@PathVariable("name") String name);

    @GetMapping(
            value = FIND_ALL_DELIVERY_POINT_CATEGORY_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "findAllDeliveryPointCategory",

            description = "Rechercher tout les DeliveryPoint! Cette methode permet de retouner tous les delivery Point categroys",
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
    ResponseEntity<List<DeliveryPointCatResponse>> findAllDeliveryPointCategory();

    @DeleteMapping(value = DELETE_DELIVERY_POINT_CATEGORY_ENDPOINT)
    @Operation(operationId = "deleteDeliveryPointCategory",

            description = "Supprimer un DeliveryPoint par ID! Cette methode permet de suprimmer un delivery Point categroy par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le delivery Point categroy a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le delivery Point categroy avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<?> deleteDeliveryPointCategory(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_ALL_DELIVERY_POINT_CATEGORY_PAGEABLE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllDeliveryPointCategoriesPageable"
    )
    ResponseEntity<PageResponse<DeliveryPointCatResponse>> findAllDeliveryPointCategoriesPageable(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    );
}
