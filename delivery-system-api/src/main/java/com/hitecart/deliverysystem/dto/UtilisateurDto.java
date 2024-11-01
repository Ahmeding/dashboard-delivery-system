package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Adresse;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.model.enums.RoleUtilisateur;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class UtilisateurDto {

    private Integer id;
    private String lastname;
    private String firstname;
    private String fullname;
    private String phoneNum;
    private String email;
    private String password;
    private AdresseDto address;
    private LocalDate dateOfBirth;
    private RoleUtilisateur role;
    private boolean accountLocked;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;



    public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
        if(utilisateur == null){
            return null;
        }
        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .lastname(utilisateur.getLastname())
                .firstname(utilisateur.getFirstname())
                .fullname(utilisateur.fullName())
                .phoneNum(utilisateur.getPhoneNum())
                .email(utilisateur.getEmail())
                .password(utilisateur.getPassword())
                .dateOfBirth(utilisateur.getDateOfBirth())
                .role(utilisateur.getRole())
                .address(AdresseDto.fromEntity(utilisateur.getAddress()))
                .createdAt(utilisateur.getCreatedAt())
                .updatedAt(utilisateur.getUpdatedAt())
                .build();
    }

    public static Utilisateur toEntity(UtilisateurDto dto) {
        if(dto==null){
            return null;
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.getId());
        utilisateur.setLastname(dto.getLastname());
        utilisateur.setFirstname(dto.getFirstname());
        utilisateur.setPhoneNum(dto.getPhoneNum());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setPassword(dto.getPassword());
        utilisateur.setRole(dto.getRole());
        utilisateur.setAddress(AdresseDto.toEntity(dto.getAddress()));
        utilisateur.setCreatedAt(dto.getCreatedAt());
        utilisateur.setUpdatedAt(dto.getUpdatedAt());

        return utilisateur;
    }
}
