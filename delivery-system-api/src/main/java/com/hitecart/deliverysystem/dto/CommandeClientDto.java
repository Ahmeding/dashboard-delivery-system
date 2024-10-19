package com.hitecart.deliverysystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.CommandeClient;
import com.hitecart.deliverysystem.model.LigneCommandeClient;
import com.hitecart.deliverysystem.model.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class CommandeClientDto  {
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private String code;
    private LocalDateTime dateCommnde;
    private Integer clientId;
    private List<LigneCommandeClientDto> ligneCommandeClientsDtos;
    private OrderStatus etatCommande;

    public static CommandeClientDto fromEntity(CommandeClient commandeClient) {
        if(commandeClient ==null){
            return null;
        }
        return CommandeClientDto.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommnde(commandeClient.getDateCommnde())
                .clientId(commandeClient.getClient()!=null?commandeClient.getClient().getId():null)
                .ligneCommandeClientsDtos(
                        commandeClient.getLigneCommandeClients()!=null?
                                commandeClient.getLigneCommandeClients().stream()
                                        .map(LigneCommandeClientDto::fromEntity)
                                        .collect(Collectors.toList()):null
                )
                .etatCommande(commandeClient.getEtatCommande())

                .createdAt(commandeClient.getCreatedAt())
                .updatedAt(commandeClient.getUpdatedAt())
                .createdBy(commandeClient.getCreatedBy())
                .lastModifiedBy(commandeClient.getLastModifiedBy())
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
                /*commandeClient.setClient(ClientDto.toEntity(dto.getClientDto()));*/
                commandeClient.setEtatCommande(dto.getEtatCommande());
                commandeClient.setLigneCommandeClients(
                        dto.getLigneCommandeClientsDtos()!=null?
                                dto.getLigneCommandeClientsDtos().stream()
                                        .map(LigneCommandeClientDto::toEntity)
                                        .collect(Collectors.toList()):null);

        commandeClient.setCreatedAt(dto.getCreatedAt());
        commandeClient.setUpdatedAt(dto.getUpdatedAt());
        commandeClient.setCreatedBy(dto.getCreatedBy());
        commandeClient.setLastModifiedBy(dto.getLastModifiedBy());

        return commandeClient;
    }
    @JsonIgnore
    public boolean isCommandeCompleted(){
        return OrderStatus.COMPLETED.equals(this.etatCommande);
    }
}
