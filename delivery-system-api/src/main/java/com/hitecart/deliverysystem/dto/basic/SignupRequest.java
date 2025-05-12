package com.hitecart.deliverysystem.dto.basic;

import com.hitecart.deliverysystem.model.enums.RoleName;
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
    private RoleName role;

}
