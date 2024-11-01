package com.hitecart.deliverysystem.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;
    private String refreshToken;
    private boolean isAuthenicated;
    private UtilisateurDto user;
}
