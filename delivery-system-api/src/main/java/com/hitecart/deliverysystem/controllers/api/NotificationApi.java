package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.basic.NotificationDto;
import com.hitecart.deliverysystem.dto.basic.RoleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(NOTIFICATION_ENDPOINT)
@Tag(name = "Notification")
public interface NotificationApi {

    @PutMapping(value = UPDATE_NOTIFICATION_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            summary = "Mark As Read",
            operationId = "markAsRead",  // ← stable, custom ID

            description = "Modifier une role, cette methode permet " +
                    "modifier une reparattion",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet role a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet role n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<NotificationDto> markAsRead(@RequestBody NotificationDto dto);

    @PreAuthorize("#userId == authentication.principal.id")
    @GetMapping(value = FIND_ALL_NOTIFICATION_BY_USERID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "find All Notifications By User id",
            operationId = "findAllByUserID",  // ← stable, custom ID

            description = "Rechercher tout les notif! Cette methode permet de retouner toutes les roles",
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
    ResponseEntity<List<NotificationDto>> findAllByUserID(@PathVariable("userId") Integer userId);

}
