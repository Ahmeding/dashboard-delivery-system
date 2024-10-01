package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.CommandeClient;
import com.hitecart.deliverysystem.model.LigneCommandeClient;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CommandeClientDto {

    private Integer id;
    private String code;
    private LocalDateTime dateCommnde;
    private ClientDto clientDto;
    @JsonIgnore
    private List<LigneCommandeClientDto> ligneCommandeClientsDtos;

    public static CommandeClientDto fromEntity(CommandeClient commandeClient) {
        if(commandeClient ==null){
            return null;
        }
        return CommandeClientDto.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommnde(commandeClient.getDateCommnde())
                .clientDto(ClientDto.fromEntity(commandeClient.getClient()))
                .ligneCommandeClientsDtos(

                        commandeClient.getLigneCommandeClients()!=null?
                                commandeClient.getLigneCommandeClients().stream()
                                        .map(LigneCommandeClientDto::fromEntity)
                                        .collect(Collectors.toList()):null

                )
                .build();
    }

    public static CommandeClient toEntity(CommandeClientDto dto){
        if(dto ==null){
            return null;
        }
        CommandeClient commandeClient =new CommandeClient();
                commandeClient.setId(dto.getId());
                commandeClient.setCode(dto.getCode());
                commandeClient.setDateCommnde(dto.getDateCommnde());
                commandeClient.setClient(ClientDto.toEntity(dto.getClientDto()));
                commandeClient.setLigneCommandeClients(
                        dto.getLigneCommandeClientsDtos()!=null?
                                dto.getLigneCommandeClientsDtos().stream()
                                        .map(LigneCommandeClientDto::toEntity)
                                        .collect(Collectors.toList()):null

                );

        return commandeClient;
    }
}
