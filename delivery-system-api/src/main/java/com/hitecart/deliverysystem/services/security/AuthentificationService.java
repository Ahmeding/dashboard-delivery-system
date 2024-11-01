package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.dto.*;

public interface AuthentificationService {

    UtilisateurDto signup(SignupRequest signupRequest);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
