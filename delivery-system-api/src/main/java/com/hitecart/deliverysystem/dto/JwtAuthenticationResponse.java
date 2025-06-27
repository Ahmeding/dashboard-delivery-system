package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.dto.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {

    private String access_token;
    private String refresh_token;
}
