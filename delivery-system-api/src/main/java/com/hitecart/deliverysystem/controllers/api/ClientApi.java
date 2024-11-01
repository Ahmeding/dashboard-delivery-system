package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.dto.VehiculeDto;
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

@RequestMapping(CLIENT_ENDPOINT)
@Tag(name = "Client")
public interface ClientApi {

    @PostMapping(
            value = CREATE_CLIENT_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            description = "Ajouter/Modifier un client, cette methode permet " +
                    "d'enregistrer ou modifier un client",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Client a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Client n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<ClientDto> save(@RequestBody ClientDto dto);

    @GetMapping(
            value = FIND_CLIENT_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(
            description = "Rechercher un Client par ID! Cette methode permet de chercher un client par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le client a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le client avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<ClientDto> findById(@PathVariable("id") Integer id);
    @GetMapping(
            value = FIND_CLIENT_BY_EMAIL_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            description = "Rechercher un Client par son email ID! Cette methode permet de chercher un client par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le client a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le client avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<ClientDto> findByEmail(@PathVariable("email") String email);
    @GetMapping(
            value = FIND_ALL_CLIENT_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            description = "Rechercher tout les Client! Cette methode permet de retouner tous les clients",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Clients retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<ClientDto>> findAll();

    @PutMapping(value = UPDATE_ISACTIVE_CLIENT_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClientDto> updateIsActive(@PathVariable("id") Integer id, @PathVariable("isactive") Boolean isActive);

    @DeleteMapping(value = DELETE_CLIENT_ENDPOINT)
    @Operation(
            description = "Supprimer un Client par ID! Cette methode permet de suprimmer un client par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le client a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le client avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_CLIENT_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    ResponseEntity<PageResponse<ClientDto>> findAllClients(
      @RequestParam(name = "page",defaultValue = "1", required = false) int page,
      @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
