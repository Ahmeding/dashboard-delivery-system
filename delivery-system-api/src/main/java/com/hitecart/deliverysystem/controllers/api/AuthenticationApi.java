package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.basic.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@Tag(name = "Authentication : enregistrement")
public interface AuthenticationApi {

        @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(description = "Permet à un utilisateur à s'enregitrer",
                    responses = {
                @ApiResponse(responseCode = "200",
                            description = "Le compte utilsateur à ete creer avec succés"),

                @ApiResponse(responseCode = "400",
                            description = "Utilsateur invalide , la creation de compte à été echoué")
                    })
        ResponseEntity<UtilisateurDto> signup(@RequestBody SignupRequest signupRequest);

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Permet à un utilisateur de se connecter",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "connexion etabli avec succés"),

                    @ApiResponse(responseCode = "400",
                            description = "Email ou Mot de passe  invalide , la connexion à été echouée")
            })
        ResponseEntity<JwtAuthenticationResponse> signin(@Valid @RequestBody SigninRequest signinRequest);


    @PostMapping(value = "/refreshtoken", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Permet de créer un refresh token",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "refresh token créé avec succés"),

                    @ApiResponse(responseCode = "400",
                            description = "creation de refresh token à été echouée")
            })
    ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest);


}

