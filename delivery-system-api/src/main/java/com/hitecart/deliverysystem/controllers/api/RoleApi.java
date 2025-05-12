package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.RoleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(ROLE_ENDPOINT)
@Tag(name = "Role")
public interface RoleApi {

    @PostMapping(value = CREATE_ROLE_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            description = "Ajouter/Modifier une role, cette methode permet " +
                    "d'enregistrer ou modifier une reparattion",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet role a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoleDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet role n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<RoleDto> save(@RequestBody RoleDto dto);

    @GetMapping(value = FIND_ROLE_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher une role par ID! Cette methode permet de chercher une role par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La role a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "La role avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<RoleDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_ALL_ROLE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Rechercher tout les role! Cette methode permet de retouner toutes les roles",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "roles retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<RoleDto>> findAll();

    @DeleteMapping(value = DELETE_ROLE_ENDPOINT)
    @Operation(
            description = "Supprimer une role par ID! Cette methode permet de suprimmer une role par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La role a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La role avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    void delete(@PathVariable("id") Integer id);
}
