package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.CommandeClientResponse;
import com.hitecart.deliverysystem.dto.ServiceDeliveryDto;
import com.hitecart.deliverysystem.dto.ServiceDeliveryDto;
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

@RequestMapping(SERVICE_DELIVERY_ENDPOINT)
@Tag(name = "Service_Delivery")
public interface ServiceDeliveryApi {


    @PostMapping(
            value = CREATE_SERVICE_DELIVERY_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces =MediaType.APPLICATION_JSON_VALUE )
    @Operation(
            description = "Ajouter/Modifier un service delivery , cette methode permet " +
                    "d'enregistrer ou modifier un service delivery",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Service Delivery  a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ServiceDeliveryDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Service Delivery  n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    public ResponseEntity<ServiceDeliveryDto> save(@RequestBody ServiceDeliveryDto dto);
    @GetMapping(
            value = FIND_SERVICE_DELIVERY_BY_ID_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE )
    @Operation(
            description = "Rechercher un Service Delivery  par ID! Cette methode permet de chercher un commande client  par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le Service Delivery  a été trouvée dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ServiceDeliveryDto.class)
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
     ResponseEntity<ServiceDeliveryDto> findById(@PathVariable("id") Integer id);
    @GetMapping(
            value = FIND_ALL_SERVICE_DELIVERY_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Operation(
            description = "Rechercher tout les ServiceDelivery ! Cette methode permet de retouner toutes les ServiceDelivery",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "ServiceDelivery retrouvées ",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ServiceDeliveryDto.class))
                            )
                    )
            }
    )
     ResponseEntity<List<ServiceDeliveryDto>> findAll();
    @PutMapping(
            value = UPDATE_SERVICE_DELIVERY_ENABLED_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces =MediaType.APPLICATION_JSON_VALUE )
    @Operation(
            description = "update un Service Delivery  par ID! Cette methode permet de maj un service delivery  par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le Service Delivery  a été maj dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ServiceDeliveryDto.class)
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
     ResponseEntity<ServiceDeliveryDto> updateEnabled(@PathVariable("id") Integer id, @PathVariable("isenabled") boolean enabled);
    @GetMapping(
            value = FIND_ALL_BY_ENABLED_SERVICE_DELIVERY_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE )
    @Operation(
            description = "Rechercher un Service Delivery  par son etat! Cette methode permet de chercher un service delivery  par son etat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le Service Delivery  a été trouvée dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ServiceDeliveryDto.class)
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
     ResponseEntity<List<ServiceDeliveryDto>> findAllEnabled(@PathVariable("isenabled") boolean enabled);
    @GetMapping(
            value = FIND_ALL_SERVICE_DELIVERY_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE )@Operation(
            description = "Rechercher tout les ServiceDelivery! Cette methode permet de retouner toutes les ServiceDelivery",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "CommandeClients retrouvées ",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CommandeClientResponse.class))
                            )
                    )
            }
    )
     ResponseEntity<PageResponse<ServiceDeliveryDto>> findAllServicesDelivery(
            @RequestParam(name = "page",defaultValue = "1", required = false)  int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size);
}
