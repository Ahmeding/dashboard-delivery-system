package com.hitecart.deliverysystem.dto.basic;

import com.hitecart.deliverysystem.model.Adresse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdresseDto {

    private String adresse1;
    private String adresse2;
    private String ville;
    private String codepostal;
    private String pays;

    public static AdresseDto fromEntity(Adresse adresse){
        if(adresse==null){
            return null;
        }
        return AdresseDto .builder()
                .adresse1(adresse.getAdresse1())
                .adresse2(adresse.getAdresse2())
                .ville(adresse.getVille())
                .codepostal(adresse.getCodepostal())
                .pays(adresse.getPays())
                .build();
    }

    public static Adresse toEntity(AdresseDto dto){
        if(dto==null){
            return null;
        }
        Adresse adresse = new Adresse();
        adresse.setAdresse1(dto.getAdresse1());
        adresse.setAdresse2(dto.getAdresse2());
        adresse.setVille(dto.getVille());
        adresse.setCodepostal(dto.getCodepostal());
        adresse.setPays(dto.getPays());
        return adresse;
    }
}
