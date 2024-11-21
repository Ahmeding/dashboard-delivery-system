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
    private String firstname;
    private String lastname;
    private String phoneNum;
    private String email;
    private AdresseDto address;;
    private boolean accountLocked;
    private boolean enabled;
    private String siret;
    private String siren;
    private String description;
    private List<CommandeClientDto> commandeClientsDtos;
    private List<DevisDto> devisDtos;
    private List<DeliveryPointDto> deliveryPointDtos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static ClientDto fromEntity(Client client) {
        if(client==null){

        return null;
        }
        return ClientDto.builder()

                .id(client.getId())
                .firstname(client.getFirstname())
                .lastname(client.getLastname())
                .description(client.getDescription())
                .phoneNum(client.getPhoneNum())
                .email(client.getEmail())
                .address(AdresseDto.fromEntity(client.getAddress()))
                .siret(client.getSiret())
                .siren(client.getSiren())
                .enabled(client.isEnabled())
                .accountLocked(client.isAccountLocked())
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

                .deliveryPointDtos(client.getDeliveryPoints()!=null?
                                    client.getDeliveryPoints().stream()
                                            .map(DeliveryPointDto::fromEntity)
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
        client.setFirstname(dto.getFirstname());
        client.setLastname(dto.getLastname());
        client.setPhoneNum(dto.getPhoneNum());
        client.setEmail(dto.getEmail());
        client.setDescription(dto.getDescription());
        client.setSiren(dto.getSiren());
        client.setSiret(dto.getSiret());
        client.setAddress(AdresseDto.toEntity(dto.getAddress()));
        client.setEnabled(dto.isEnabled());
        client.setAccountLocked(dto.isAccountLocked());
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
