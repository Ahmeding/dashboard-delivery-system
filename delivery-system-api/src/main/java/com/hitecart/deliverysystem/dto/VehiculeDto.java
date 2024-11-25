package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.enums.TypeEnergie;
import com.hitecart.deliverysystem.model.Vehicule;
import com.hitecart.deliverysystem.model.enums.VehicleType;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class VehiculeDto {

    private String logo;
    private String reference;
    private VehicleType vehicleType;
    private String immatriculation;
    private String marque;
    private String modele;
    private String numVin;
    private LocalDate datePMC;
    private BigDecimal kilometrage;
    private TypeEnergie typecarburant;
    private BigDecimal volume;
    private LocalDate dateExpCtrlTec;
    @JsonIgnore
    private List<ReparationDto> reparationsDtos;
    private Boolean isActive;
    private boolean isAssigned;
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static VehiculeDto fromEntity(Vehicule vehicule) {
        if(vehicule==null){
            return null;
        }
        return VehiculeDto.builder()
         .id(vehicule.getId())
                .reference(vehicule.getReference())
         .immatriculation(vehicule.getImmatriculation())
         .marque(vehicule.getMarque())
         .modele(vehicule.getModele())
         .numVin(vehicule.getNumVin())
         .datePMC(vehicule.getDatePMC())
        .kilometrage(vehicule.getKilometrage())
        .typecarburant(vehicule.getTypecarburant())
        .volume(vehicule.getVolume())
                .isAssigned(vehicule.isAssigned())
                .isActive(vehicule.getIsActive())
        .dateExpCtrlTec(vehicule.getDateExpCtrlTec())

                .createdAt(vehicule.getCreatedAt())
                .updatedAt(vehicule.getUpdatedAt())
                .createdBy(vehicule.getCreatedBy())
                .lastModifiedBy(vehicule.getLastModifiedBy())
                .vehicleType(vehicule.getVehicleType())
                .logo(vehicule.getLogo())
                .build();

    }

    public static Vehicule toEntity(VehiculeDto dto){
        if(dto==null){
            return null;
        }
        Vehicule vh = new Vehicule();
                vh.setId(dto.getId());
                vh.setReference(dto.getReference());
                vh.setImmatriculation(dto.getImmatriculation());
                vh.setMarque(dto.getMarque());
                vh.setModele(dto.getModele());
                vh.setNumVin(dto.getNumVin());
                vh.setDatePMC(dto.getDatePMC());
                vh.setKilometrage(dto.getKilometrage());
                vh.setTypecarburant(dto.getTypecarburant());
                vh.setVolume(dto.getVolume());
                vh.setDateExpCtrlTec(dto.getDateExpCtrlTec());
                vh.setIsActive(dto.getIsActive());
                vh.setAssigned(dto.isAssigned);

        vh.setCreatedAt(dto.getCreatedAt());
        vh.setUpdatedAt(dto.getUpdatedAt());
        vh.setCreatedBy(dto.getCreatedBy());
        vh.setLastModifiedBy(dto.getLastModifiedBy());
            vh.setVehicleType(dto.getVehicleType());
            vh.setLogo(dto.getLogo());
        return vh;
    }

}
