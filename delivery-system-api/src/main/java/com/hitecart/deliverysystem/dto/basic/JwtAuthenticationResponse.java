package com.hitecart.deliverysystem.dto.basic;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;
    private String refreshToken;
    private boolean isAuthenicated;
    private UtilisateurDto user;
}
