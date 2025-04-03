package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.CommandeClient;
import com.hitecart.deliverysystem.model.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@Schema(description = "DTO representing the response of a CommandeClient")
public class CommandeClientResponse {


    private String code;
    private LocalDateTime dateCommnde;
    private ClientDto client;
    private List<LigneCommandeClientDto> ligneCommandeClientsDtos;
    private OrderStatus etatCommande;
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;
    private BigDecimal montantTotalHT;
    private BigDecimal montantTotalTTC;

    public static CommandeClientResponse fromEntity(CommandeClient commandeClient) {
        if(commandeClient ==null){
            return null;
        }
        return CommandeClientResponse.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommnde(commandeClient.getDateCommnde())
                .client(ClientDto.fromEntity( commandeClient.getClient()))
                .montantTotalHT(commandeClient.getMontantTotalHT())
                .montantTotalTTC(commandeClient.getMontantTotalTTC())
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

    public static CommandeClient toEntity(CommandeClientResponse dto){
        if(dto ==null){
            return null;
        }
        CommandeClient commandeClient =new CommandeClient();
                commandeClient.setId(dto.getId());
                commandeClient.setCode(dto.getCode());
                commandeClient.setDateCommnde(dto.getDateCommnde());
                commandeClient.setClient(ClientDto.toEntity(dto.getClient()));
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
