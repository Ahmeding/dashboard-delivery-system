package com.hitecart.deliverysystem.dto.role;

import com.hitecart.deliverysystem.model.enums.RoleType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse{
       private Integer id;
       private RoleType name;

}
