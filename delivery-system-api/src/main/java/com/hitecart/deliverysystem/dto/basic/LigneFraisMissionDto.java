package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.FraisMission;
import com.hitecart.deliverysystem.model.LigneFraisMission;
import com.hitecart.deliverysystem.model.enums.TypeFraisMission;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LigneFraisMissionDto {
    private Integer id;
    private TypeFraisMission type;
    private LocalDateTime dateAchat;
    private BigDecimal quantite;
    private BigDecimal cout;
    @JsonIgnore
    private FraisMission fraisMission;

    public static LigneFraisMissionDto fromEntity(LigneFraisMission ligneFraisMission){
        if(ligneFraisMission == null){
            //throw new NullPointerException("LigneFraisMission est null!");
        return null;
        }
        return LigneFraisMissionDto .builder()
                .id(ligneFraisMission.getId())
                .cout(ligneFraisMission.getCout())
                .type(ligneFraisMission.getType())
                .dateAchat(ligneFraisMission.getDateAchat())
                .quantite(ligneFraisMission.getQuantite())
                .fraisMission(ligneFraisMission.getFraisMission())
                .build();
    }
    public static LigneFraisMission toEntity(LigneFraisMissionDto ligneFraisMissionDto){
        if(ligneFraisMissionDto == null){
            throw new NullPointerException("LigneFraisMissionDto est null!");
        }

        LigneFraisMission ligneFraisMission =new LigneFraisMission();
                ligneFraisMission.setId(ligneFraisMissionDto.getId());
                ligneFraisMission.setCout(ligneFraisMissionDto.getCout());
                ligneFraisMission.setType(ligneFraisMissionDto.getType());
                ligneFraisMission.setDateAchat(ligneFraisMissionDto.getDateAchat());
                ligneFraisMission.setQuantite(ligneFraisMissionDto.getQuantite());
                ligneFraisMission.setFraisMission(ligneFraisMissionDto.getFraisMission());


        return ligneFraisMission;
    }
}
