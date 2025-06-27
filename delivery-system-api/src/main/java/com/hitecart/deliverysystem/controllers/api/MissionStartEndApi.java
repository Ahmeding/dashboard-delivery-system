package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.mission.MissionStartEndRequest;
import com.hitecart.deliverysystem.dto.mission.MissionStatusUpdateRequest;
import com.hitecart.deliverysystem.model.enums.KmFlag;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import com.hitecart.deliverysystem.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(MISSION_ENDPOINT)
@Tag(name = "Mission Start/End")
public interface MissionStartEndApi {


    @PatchMapping(value = UPDATE_MISSION_STATUS_ENDPOINT)
    @Operation(operationId = "updateMissionStatus",
            description = "Modifier l'etat d'une mission, cette methode permet " +
                    "modifier l'etat d' une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'etat de l' objet Mission a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'etat de l'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionResponse> updateMissionStatus(
            @PathVariable("missionId") Integer missionId,
            @RequestBody MissionStatusUpdateRequest missionStatusUpdateRequest);

    @PatchMapping(value = UPDATE_MILEAGE_MISSION_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateMissionMileage",
            description = "Mettre à jour le kilomtratge debut/fin d'une mission, cette methode permet " +
                    "Mettre à jour le kilomtratge debut/fin d' une mission",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le kilomtratge de l' objet Mission a été mis à jour",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MissionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le kilomtratge de l'objet Mission n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<MissionResponse> updateMissionMileage(
            @PathVariable("missionId") Integer missionId,
            @RequestBody MissionStartEndRequest missionStartEndRequest);


}
