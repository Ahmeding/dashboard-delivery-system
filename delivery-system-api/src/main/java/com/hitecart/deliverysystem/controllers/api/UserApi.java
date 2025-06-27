package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.user.UserRequest;
import com.hitecart.deliverysystem.dto.user.UserResponse;
import com.hitecart.deliverysystem.dto.user.UserUpdateRequest;
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

@RequestMapping(USER_ENDPOINT)
@Tag(name = "User")
public interface UserApi {

    @PostMapping(value = CREATE_USER_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveUser",
            description = "Ajouter/Modifier un utilisateur, cette methode permet " +
                    "d'enregistrer ou modifier un utilisateur",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet User a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet User n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest userRequest);

    @PatchMapping(value = UPDATE_USER_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateUserInfo",
            description = "Ajouter/Modifier un utilisateur, cette methode permet " +
                    "d'enregistrer ou modifier un utilisateur",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet User a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet User n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<UserResponse> updateUserInfo(@PathVariable("userId") Integer userId, @RequestBody UserUpdateRequest request);

    @GetMapping(value = FIND_USER_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findUserById",
            description = "Rechercher un User par ID! Cette methode permet de chercher un utilisateur par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le utilisateur a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le utilisateur avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<UserResponse> findUserById(@PathVariable("id") Integer id);



    @GetMapping(value = FIND_USER_BY_EMAIL_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findUserByEmail",
            description = "Rechercher un User par son email ! Cette methode permet de chercher un utilisateur par son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le utilisateur a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le utilisateur avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<UserResponse> findUserByEmail(@PathVariable("email") String email);



    @GetMapping(value = FIND_ALL_USER_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllUser",
            description = "Rechercher tout les User! Cette methode permet de retouner tous les chauffeurs",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Chauffeurs retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<UserResponse>> findAllUser();

    @DeleteMapping(value = DELETE_USER_ENDPOINT)
    @Operation(operationId = "deleteUser",
            description = "Supprimer un User par ID! Cette methode permet de suprimmer un utilisateur par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le utilisateur a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le utilisateur avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
     ResponseEntity<?> deleteUser(@PathVariable("id") Integer id);



    @GetMapping(value = FIND_ALL_USER_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    @Operation(operationId = "findAllUsersPageable")
    ResponseEntity<PageResponse<UserResponse>> findAllUsersPageable(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size);


    @PatchMapping(UPDATE_USER_ROLES_ENDPOINT)
    @Operation(operationId = "updateUserRoles",
            summary = "update User Roles")
    ResponseEntity<UserResponse> updateUserRoles(@PathVariable Integer id, @RequestBody List<RoleRequest> rolesDto);



    @PatchMapping(UPDATE_USER_PASS_ENDPOINT)
    @Operation(operationId = "updateUserPassword",
            summary = "update User Password"  // ← stable, custom ID
    )
    ResponseEntity<UserResponse> updateUserPassword(
            @PathVariable Integer id,
            @RequestBody UserRequest userRequest);
}
