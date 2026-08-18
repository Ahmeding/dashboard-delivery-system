package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.feedback.FeedbackResponse;
import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import com.hitecart.deliverysystem.model.Feedback;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(FEEDBACK_ENDPOINT)
@Tag(name = "Feedback")
public interface FeedbackApi {

    @PostMapping(
            value = CREATE_FEEDBACK_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "saveFeedback",

            description = "Ajouter/Modifier un client, cette methode permet " +
                          "d'enregistrer ou modifier un client",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Client a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Client n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<FeedbackResponse> saveFeedback(@RequestBody FeedbackRequest feedBackRequest, Authentication connectedUser);



    @PatchMapping(
            value = UPDATE_FEEDBACK_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(operationId = "updateFeedbackInfo",

            description = "Modifier un client, cette methode permet " +
                          "modifier un client",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Client a été  modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Client n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<FeedbackResponse> updateFeedbackInfo(@PathVariable("feedbackId") Integer feedbackId, @RequestBody FeedbackRequest feedBackRequest, Authentication connectedUser);




    @GetMapping(value = FIND_FEEDBACKS_BY_DRIVER_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllFeedBackByDriver")
    ResponseEntity<List<FeedbackResponse>> findAllFeedBackByDriver(
            @PathVariable("driverId") Integer driverId,
            Authentication connectedUser);




    @GetMapping(value = FIND_ALL_FEEDBACK_PAGEABLE_BY_DRIVER_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllFeedBackByDriverPageable")
    ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedBackByDriverPageable(
            @PathVariable("driverId") Integer driverId,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)int size,
            Authentication connectedUser);

    @DeleteMapping(value = DELETE_FEEDBACK_BY_ID_ENDPOINT)
    @Operation(operationId = "deleteFeedback",

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
    ResponseEntity<?> deleteFeedback(@PathVariable("feedbackId") Integer feedbackId, Authentication connectedUser);
}
