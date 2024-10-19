package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.*;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class ClientDto {
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private String nom;
    private String prenom;
    private String description;
    private String numTel;
    private String adresseEmail;
    private AdresseDto adresseDto;
    private String siret;
    private String siren;
    private List<CommandeClientDto> commandeClientsDtos;
    private List<DevisDto> devisDtos;
    private Boolean isActive;

    public static ClientDto fromEntity(Client client) {
        if(client==null){

        return null;
        }
        return ClientDto.builder()

                .id(client.getId())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .description(client.getDescription())
                .numTel(client.getNumTel())
                .adresseEmail(client.getAdresseEmail())
                .adresseDto(AdresseDto.fromEntity(client.getAdresse()))
                .siret(client.getSiret())
                .siren(client.getSiren())
                .isActive(client.getIsActive())
                .commandeClientsDtos(
                        client.getCommandeClients()!=null?
                                client.getCommandeClients().stream()
                                        .map(CommandeClientDto::fromEntity)
                                        .collect(Collectors.toList()):null
                )
                .devisDtos(
                        client.getDevis()!=null?
                                client.getDevis().stream()
                                        .map(DevisDto::fromEntity)
                                        .collect(Collectors.toList()):null
                )


                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .createdBy(client.getCreatedBy())
                .lastModifiedBy(client.getLastModifiedBy())
                .build();
    }

    public static Client toEntity(ClientDto dto) {
        if(dto==null){
            return null;
        }
        Client client = new Client();
        client.setId(dto.getId());
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setNumTel(dto.getNumTel());
        client.setAdresseEmail(dto.getAdresseEmail());
        client.setDescription(dto.getDescription());
        client.setSiren(dto.getSiren());
        client.setSiret(dto.getSiret());
        client.setAdresse(AdresseDto.toEntity(dto.getAdresseDto()));
        client.setIsActive(dto.isActive);
        //Cmd client
        client.setCommandeClients(
                dto.getCommandeClientsDtos()!=null?
                        dto.getCommandeClientsDtos().stream()
                                .map(CommandeClientDto::toEntity)
                                .collect(Collectors.toList()):null
        );
        //devis client
        client.setDevis(
        dto.getDevisDtos()!=null?
                dto.getDevisDtos().stream()
                        .map(DevisDto::toEntity)
                        .collect(Collectors.toList()):null
                );

        client.setCreatedAt(dto.getCreatedAt());
        client.setUpdatedAt(dto.getUpdatedAt());
        client.setCreatedBy(dto.getCreatedBy());
        client.setLastModifiedBy(dto.getLastModifiedBy());
        return client;
    }

}
