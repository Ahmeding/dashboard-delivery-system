package com.hitecart.deliverysystem.dto.basic;


import com.hitecart.deliverysystem.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class RoleDto {

    private String name;
    private List<UtilisateurDto> users;

    public static RoleDto fromEntiy(Role role) {
        if(role == null){
        return null;
        }
        return RoleDto.builder()
                .name(role.getName())
                .build();
    }
    public static Role toEntiy(RoleDto dto) {
        if(dto == null){
        return null;
        }
        return Role.builder()
                .name(dto.getName())
                .build();
    }
}
