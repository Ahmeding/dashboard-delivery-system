package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.enums.TypeEnergie;
import com.hitecart.deliverysystem.model.Vehicule;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VehiculeDto {
    private Integer id;
    private String immatriculation;
    private String marque;
    private String modele;
    private String numVin;
    private LocalDateTime datePMC;
    private BigDecimal kilometrage;
    private TypeEnergie typecarburant;
    private BigDecimal volume;
    private LocalDateTime dateExpCtrlTec;
    @JsonIgnore
    private List<ReparationDto> reparationsDtos;


    public static VehiculeDto fromEntity(Vehicule vehicule) {
        if(vehicule==null){
            return null;
        }
        return VehiculeDto.builder()
         .id(vehicule.getId())
         .immatriculation(vehicule.getImmatriculation())
         .marque(vehicule.getMarque())
         .modele(vehicule.getModele())
         .numVin(vehicule.getNumVin())
         .datePMC(vehicule.getDatePMC())
        .kilometrage(vehicule.getKilometrage())
        .typecarburant(vehicule.getTypecarburant())
        .volume(vehicule.getVolume())
        .dateExpCtrlTec(vehicule.getDateExpCtrlTec())
                .build();

    }

    public static Vehicule toEntity(VehiculeDto dto){
        if(dto==null){
            return null;
        }
        Vehicule vh = new Vehicule();
                vh.setId(dto.getId());
                vh.setImmatriculation(dto.getImmatriculation());
                vh.setMarque(dto.getMarque());
                vh.setModele(dto.getModele());
                vh.setNumVin(dto.getNumVin());
                vh.setDatePMC(dto.getDatePMC());
                vh.setKilometrage(dto.getKilometrage());
                vh.setTypecarburant(dto.getTypecarburant());
                vh.setVolume(dto.getVolume());
                vh.setDateExpCtrlTec(dto.getDateExpCtrlTec());

        return vh;
    }

}
