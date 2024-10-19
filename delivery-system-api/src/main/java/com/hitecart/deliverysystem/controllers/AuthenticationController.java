package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.AuthenticationApi;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.services.security.AuthentificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController implements AuthenticationApi {


    private final AuthentificationService authentificationService;

    @Override
    public ResponseEntity<UtilisateurDto> signup(SignupRequest signupRequest) {
        return ResponseEntity.ok(authentificationService.signup(signupRequest));
    }

    @Override
    public ResponseEntity<JwtAuthenticationResponse> signin(SigninRequest signinRequest) {
  /*
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getEmail(),
                        signinRequest.getPassword()
                )
        );
*/
        return ResponseEntity.ok(authentificationService.signin(signinRequest));
    }

    @Override
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {

        return ResponseEntity.ok(authentificationService.refreshToken(refreshTokenRequest));
    }
}
