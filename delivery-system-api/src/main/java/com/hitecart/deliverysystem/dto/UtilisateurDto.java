package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.model.enums.RoleUtilisateur;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UtilisateurDto {

    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private String nom;
    private String prenom;
    private String numTel;
    private String email;
    private String motDePasse;
    private AdresseDto adresseDto;
    private RoleUtilisateur role;


    public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
        if(utilisateur == null){
            return null;
        }
        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .numTel(utilisateur.getNumTel())
                .email(utilisateur.getEmail())
                .motDePasse(utilisateur.getMotDePasse())
                .role(utilisateur.getRole())
                .adresseDto(AdresseDto.fromEntity(utilisateur.getAdresse()))

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
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setNumTel(dto.getNumTel());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(dto.getMotDePasse());
        utilisateur.setRole(dto.getRole());
        utilisateur.setAdresse(AdresseDto.toEntity(dto.getAdresseDto()));

        utilisateur.setCreatedAt(dto.getCreatedAt());
        utilisateur.setUpdatedAt(dto.getUpdatedAt());

        return utilisateur;
    }
}
