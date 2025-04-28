package com.hitecart.deliverysystem.dto.basic;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {

    private String access_token;
    private String refresh_token;
    private boolean isAuthenicated;
    private UtilisateurDto user;
}
