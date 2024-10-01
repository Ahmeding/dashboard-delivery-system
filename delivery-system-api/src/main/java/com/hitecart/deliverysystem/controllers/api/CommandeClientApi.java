package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.CommandeClientDto;
import com.hitecart.deliverysystem.dto.CommandeClientDto;
import com.hitecart.deliverysystem.utils.Constants;
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

@RequestMapping(COMMANDE_CLI_ENDPOINT)
@Tag(name = "Commande client")
public interface CommandeClientApi {
    @PostMapping(value = CREATE_COMMANDE_CLI_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Ajouter/Modifier un commande client , cette methode permet " +
                    "d'enregistrer ou modifier un chauffreur",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Commande Client  a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommandeClientDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet CommandeClient  n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto);
    @GetMapping(value = FIND_COMMANDE_CLI_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Rechercher un CommandeClient  par ID! Cette methode permet de chercher un commande client  par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La commande client  a été trouvée dans la BBD",
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
    ResponseEntity<CommandeClientDto> findById(@PathVariable("id") Integer id);
    @GetMapping(value = FIND_COMMANDE_CLI_BY_CODE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher un Commande Client  par CODE ! Cette methode permet de chercher une commande client  par son code",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La commande client  a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
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
    ResponseEntity<CommandeClientDto> findByCode(@PathVariable("code") String code);
    @GetMapping(value = FIND_ALL_COMMANDE_CLI_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les CommandeClient ! Cette methode permet de retouner toutes les commandes clients",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "CommandeClients retrouvées ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<CommandeClientDto>> findAll();
    @DeleteMapping(value = DELETE_COMMANDE_CLI_ENDPOINT)
    @Operation(
            description = "Supprimer un CommandeClient  par ID! Cette methode permet de suprimmer un commande client  par son ID",
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
    void delete(@PathVariable("id") Integer id);
}
