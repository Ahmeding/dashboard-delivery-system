package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
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
        client.setDeliveryPoints(dto.getDeliveryPointDtos()!=null?
                dto.getDeliveryPointDtos().stream()
                        .map(DeliveryPointDto::toEntity)
                        .collect(Collectors.toList()):null);
        client.setCreatedAt(dto.getCreatedAt());
        client.setUpdatedAt(dto.getUpdatedAt());
        client.setCreatedBy(dto.getCreatedBy());
        client.setLastModifiedBy(dto.getLastModifiedBy());
        return client;
    }

}
