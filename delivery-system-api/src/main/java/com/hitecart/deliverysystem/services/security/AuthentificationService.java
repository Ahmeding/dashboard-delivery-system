package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.dto.JwtAuthenticationResponse;
import com.hitecart.deliverysystem.dto.RefreshTokenRequest;
import com.hitecart.deliverysystem.dto.SigninRequest;
import com.hitecart.deliverysystem.dto.SignupRequest;
import com.hitecart.deliverysystem.dto.user.UserResponse;

public interface AuthentificationService {

    UserResponse signup(SignupRequest signupRequest);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
