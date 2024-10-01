package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.CommandeClientDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.CommandeClient;
import com.hitecart.deliverysystem.model.LigneCommandeClient;
import com.hitecart.deliverysystem.repository.CommandeClientRepository;
import com.hitecart.deliverysystem.repository.LigneCommandeClientRepository;
import com.hitecart.deliverysystem.services.CommandeClientService;
import com.hitecart.deliverysystem.validator.CommandeClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeClientServiceImpl implements CommandeClientService {
     private CommandeClientRepository commandeClientRepository;
     private LigneCommandeClientRepository ligneCommandeClientRepository;

    public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository, LigneCommandeClientRepository ligneCommandeClientRepository) {
        this.commandeClientRepository = commandeClientRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
    }

    @Override
    public CommandeClientDto save(CommandeClientDto dto) {
        checkDto(dto,"Commande Client n'est pas valide",
                "la commande client n'est pas valide",ErrorCodes.COMMANDECLIENT_NOT_VALID);
        return CommandeClientDto.fromEntity(commandeClientRepository.save(
                CommandeClientDto.toEntity(dto)
        ));
    }

    @Override
    public CommandeClientDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Commande Client est null",
                "Impossible de trouver une commande client avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);
        Optional<CommandeClient> commandeClientOp= commandeClientRepository.findById(id);

         //return CommandeClientDto.fromEntity(commandeClientOp.get());
         return Optional.of(CommandeClientDto.fromEntity(commandeClientOp.get()))
                 .orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                         + id +"n'a ete trouvé dans la base deonnée"
                         ,ErrorCodes.COMMANDECLIENT_NOT_FOUND));
    }

    @Override
    public CommandeClientDto findByCode(String code) {
        CheckParmasUtil.checkCode(code,"ERREUR ! Le parametre Code Commande client  est null ",
                "Impossible de trouver une commande client avec un Code null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);

        Optional<CommandeClient> commandeClientOp= commandeClientRepository.findCommandeClientByCode(code);

        return Optional.of(CommandeClientDto.fromEntity(commandeClientOp.get()))
                .orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                        + code +"n'a ete trouvé dans la base deonnée"
                        ,ErrorCodes.COMMANDECLIENT_NOT_FOUND));
    }

    @Override
    public List<CommandeClientDto> findAll() {

        return commandeClientRepository.findAll().stream()
                .map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID de commande client  à supprimer est null",
                "Impossible de supprimer une commande client  avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);

        List<LigneCommandeClient> ligneCommandeClients= ligneCommandeClientRepository.findAllByComamndeClientId(id);
        if(!ligneCommandeClients.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer une commande client qui a deja des lignes de commandes"
                    ,ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);
        }
        commandeClientRepository.deleteById(id);
    }

    private void checkDto(CommandeClientDto dto, String msgLog, String msgException, ErrorCodes commandeclientNotValid) {
        List<String> errors = CommandeClientValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.CHAUFFEUR_NOT_VALID,errors);
        }
    }
}
