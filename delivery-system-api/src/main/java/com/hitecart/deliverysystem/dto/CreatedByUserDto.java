package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.CreatedByUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreatedByUserDto {
    private Integer id;
    private String fullname;

    public static CreatedByUserDto fromEntity(CreatedByUser createdBy) {
        if (createdBy == null){
            return null;
        }
        return CreatedByUserDto.builder()
                .id(createdBy.getId())
                .fullname(createdBy.getFullname())
                .build();
    }
    public static CreatedByUser toEntity(CreatedByUserDto dto) {
        if (dto == null){
            return null;
        }
        return CreatedByUser.builder()
                .id(dto.getId())
                .fullname(dto.getFullname())
                .build();
    }
}
