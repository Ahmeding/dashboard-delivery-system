package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.DrivingLicenseType;
import com.hitecart.deliverysystem.model.enums.RoleUtilisateur;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class ChauffeurDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private String phoneNum;
    private String email;
    private String password;
    private AdresseDto address;
    private LocalDate dateOfBirth;
    private RoleUtilisateur role;
    private boolean accountLocked;
    private boolean enabled;
    private String drivinglicenseNbr;
    private DrivingLicenseType drivingLicenseType;
    private String numSecu;
    private String rib;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    @JsonIgnore
    private List<ContratDto> contratDtos;
    @JsonIgnore
    private List<RoleDto> roles;
    private CreatedByUserDto createdByUser;

    public static ChauffeurDto fromEntity(Chauffeur chauffeur) {
        if(chauffeur==null) {
            return null;
        }
        return ChauffeurDto.builder()
                .id(chauffeur.getId())
                .firstname(chauffeur.getFirstname())
                .lastname(chauffeur.getLastname())
                .email(chauffeur.getEmail())
                .password(chauffeur.getPassword())
                .phoneNum(chauffeur.getPhoneNum())
                .dateOfBirth(chauffeur.getDateOfBirth())
                .drivinglicenseNbr(chauffeur.getDrivinglicenseNbr())
                .drivingLicenseType(chauffeur.getDrivinglicenseType())
                .numSecu(chauffeur.getNumSecu())
                .rib(chauffeur.getRib())
                .address(AdresseDto.fromEntity(chauffeur.getAddress()))
                .enabled(chauffeur.isEnabled())
                .accountLocked(chauffeur.isAccountLocked())
                .roles(chauffeur.getRoles()!=null?
                        chauffeur.getRoles().stream()
                                .map(RoleDto::fromEntiy)
                                .collect(Collectors.toList()):null)
                .createdBy(chauffeur.getCreatedBy())
                .createdAt(chauffeur.getCreatedAt())
                .lastModifiedBy(chauffeur.getLastModifiedBy())
                .updatedAt(chauffeur.getUpdatedAt())
            .build();
//                .contratDtos(
//                        chauffeur.getContrats()!= null?
//                                chauffeur.getContrats().stream()
//                                        .map(ContratDto::fromEntity)
//                                        .collect(Collectors.toList()):null
//                )

    }


    public static Chauffeur toEntity(ChauffeurDto dto){
        if(dto==null){
            return null;

        }

        Chauffeur  chauffeur = new Chauffeur();
                chauffeur.setId(dto.getId());
                chauffeur.setFirstname(dto.getFirstname());
                chauffeur.setLastname(dto.getLastname());
                chauffeur.setEmail(dto.getEmail());
                chauffeur.setPassword(dto.getPassword());
                chauffeur.setPhoneNum(dto.getPhoneNum());
                chauffeur.setDateOfBirth(dto.getDateOfBirth());
                chauffeur.setDrivinglicenseNbr(dto.getDrivinglicenseNbr());
                chauffeur.setDrivinglicenseType(dto.getDrivingLicenseType());
                chauffeur.setNumSecu(dto.getNumSecu());
                chauffeur.setRib(dto.getRib());
                chauffeur.setAddress(AdresseDto.toEntity(dto.getAddress()));
                chauffeur.setEnabled(dto.isEnabled());
                chauffeur.setAccountLocked(dto.isAccountLocked());
                chauffeur.setRoles(dto.roles!=null?
                        dto.roles.stream()
                        .map(RoleDto::toEntiy).collect(Collectors.toList()):null);
                chauffeur.setContrats(
                        dto.getContratDtos()!= null?
                                dto.getContratDtos().stream()
                                        .map(ContratDto::toEntity)
                                        .collect(Collectors.toList()):null
                );
        return chauffeur;

    }
}
