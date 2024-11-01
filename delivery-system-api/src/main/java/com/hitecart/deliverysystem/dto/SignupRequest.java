package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.enums.RoleUtilisateur;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleUtilisateur role;

}
