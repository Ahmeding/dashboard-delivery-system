package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.enums.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignupRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleType role;

}
