package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.DrivingLicenseType;
import com.hitecart.deliverysystem.model.enums.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString  // Add explicit @ToString
@EqualsAndHashCode  // Add explicit @EqualsAndHashCode
public class ChauffeurDto {

    private Integer id;
    private String firstname;
    private String lastname;
    private String phoneNum;
    private String email;
    private String password;
    private AdresseDto address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(type = "string", format = "date", example = "27-05-2025")
    private LocalDate dateOfBirth;
    private RoleName role;
    private String drivinglicenseNbr;
    private DrivingLicenseType drivingLicenseType;
    private String numSecu;
    private String rib;
    private boolean accountLocked;
    private boolean enabled;
    private boolean isAssigned;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ContratDto> contratDtos;
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RoleDto> roles;

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
                .isAssigned(chauffeur.isAssigned())
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
                chauffeur.setAssigned(dto.isAssigned());
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
