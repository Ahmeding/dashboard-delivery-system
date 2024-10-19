package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.Adresse;
import com.hitecart.deliverysystem.model.Chauffeur;
import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.model.Mission;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ChauffeurDto {

    private Integer id;
    private String nom;
    private String prenom;
    private String adresseEmail;
    private String motDePasse;
    private String numTel;
    private LocalDate dateNaissance;
    private String typePermi;
    private String numSecu;
    private String rib;
    private AdresseDto adresseDto;
    private String drivinglicenseNbr;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    @JsonIgnore
    private List<ContratDto> contratDtos;
    private Boolean isActive;

    public static ChauffeurDto fromEntity(Chauffeur chauffeur) {
        if(chauffeur==null) {
            return null;
        }
        return ChauffeurDto.builder()
                .id(chauffeur.getId())
                .nom(chauffeur.getNom())
                .prenom(chauffeur.getPrenom())
                .adresseEmail(chauffeur.getAdresseEmail())
                .motDePasse(chauffeur.getMotDePasse())
                .numTel(chauffeur.getNumTel())
                .dateNaissance(chauffeur.getDateNaissance())
                .typePermi(chauffeur.getTypePermi())
                .numSecu(chauffeur.getNumSecu())
                .rib(chauffeur.getRib())
                .adresseDto(AdresseDto.fromEntity(chauffeur.getAdresse()))
                .isActive(chauffeur.getIsActive())
                .drivinglicenseNbr(chauffeur.getDrivinglicenseNbr())
//                .contratDtos(
//                        chauffeur.getContrats()!= null?
//                                chauffeur.getContrats().stream()
//                                        .map(ContratDto::fromEntity)
//                                        .collect(Collectors.toList()):null
//                )
               .build();
    }


    public static Chauffeur toEntity(ChauffeurDto dto){
        if(dto==null){
            return null;

        }

        Chauffeur  chauffeur = new Chauffeur();
                chauffeur.setId(dto.getId());
                chauffeur.setNom(dto.getNom());
                chauffeur.setPrenom(dto.getPrenom());
                chauffeur.setAdresseEmail(dto.getAdresseEmail());
                chauffeur.setMotDePasse(dto.getMotDePasse());
                chauffeur.setNumTel(dto.getNumTel());
                chauffeur.setDateNaissance(dto.getDateNaissance());
                chauffeur.setTypePermi(dto.getTypePermi());
                chauffeur.setNumSecu(dto.getNumSecu());
                chauffeur.setRib(dto.getRib());
                chauffeur.setAdresse(AdresseDto.toEntity(dto.getAdresseDto()));
                chauffeur.setIsActive(dto.isActive);
                chauffeur.setDrivinglicenseNbr(dto.getDrivinglicenseNbr());
        chauffeur.setContrats(
                        dto.getContratDtos()!= null?
                                dto.getContratDtos().stream()
                                        .map(ContratDto::toEntity)
                                        .collect(Collectors.toList()):null
                );
        return chauffeur;

    }
}
