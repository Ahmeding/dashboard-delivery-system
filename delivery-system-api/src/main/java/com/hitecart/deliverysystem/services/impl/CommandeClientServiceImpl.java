package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.*;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.dao.CommandeClientRepository;
import com.hitecart.deliverysystem.dao.LigneCommandeClientRepository;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.services.CommandeClientService;
import com.hitecart.deliverysystem.services.IServiceDeliveryService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.utils.CalculeTva;
import com.hitecart.deliverysystem.validator.CommandeClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommandeClientServiceImpl implements CommandeClientService {

    private final CommandeClientRepository commandeClientRepository;
     private final LigneCommandeClientRepository ligneCommandeClientRepository;
    private final ClientService   clientService;
    private final UtilisateurService utilisateurServiceImpl;
    private final IServiceDeliveryService serviceDeliveryService;


    @Override
    public CommandeClientDto save(CommandeClientDto dto) {
        checkDto(dto,"Commande Client n'est pas valide",
                "la commande client n'est pas valide",ErrorCodes.COMMANDECLIENT_NOT_VALID);
        if(dto.getId()!=null && dto.isCommandeCompleted()){
    throw new InvalidOperationException("Unable to modify the Order once it is completed",
            ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);
        }


        BigDecimal[] totalAmountHT = {BigDecimal.ZERO};
        BigDecimal[] totalAmountTTC = {BigDecimal.ZERO};

        Client client = ClientDto.toEntity(this.clientService.findById(dto.getClientId()));
        CommandeClient savedCommandeClient = CommandeClientDto.toEntity(dto);
        savedCommandeClient.setClient(client);
         savedCommandeClient = commandeClientRepository.save(savedCommandeClient);
        if(!dto.getLigneCommandeClientsDtos().isEmpty()){

            CommandeClient finalSavedCommandeClient = savedCommandeClient;

            for (LigneCommandeClientDto cmdLine : dto.getLigneCommandeClientsDtos()) {
                BigDecimal prixttc = CalculeTva.calculeTTC(cmdLine.getPrixUnitaire(), cmdLine.getTva());
                // Cumul des montants HT et TTC
                totalAmountHT[0] = totalAmountHT[0].add(cmdLine.getPrixUnitaire().multiply(cmdLine.getQuantite()));
                totalAmountTTC[0] = totalAmountTTC[0].add(prixttc.multiply(cmdLine.getQuantite()));
                LigneCommandeClient cmdLineToSave = LigneCommandeClientDto.toEntity(cmdLine);
                cmdLineToSave.setPrixTTC(prixttc);
                cmdLineToSave.setComamndeClient(finalSavedCommandeClient);
                ServiceDelivery serviceDelivery = this.getServiceDeliveryById(cmdLine.getServiceDeliveryDtoId());
                if (serviceDelivery == null) {
                    throw new InvalidOperationException("ServiceDeliveryDto ID is missing for one of the command lines",
                            ErrorCodes.SERVICE_DELIVERY_NOT_FOUND);
                }
               cmdLineToSave.setServiceDelivery(serviceDelivery);

                ligneCommandeClientRepository.save(cmdLineToSave);
            }
        }
        savedCommandeClient.setMontantTotalHT(totalAmountHT[0]);
        savedCommandeClient.setMontantTotalTTC(totalAmountTTC[0]);
        savedCommandeClient = commandeClientRepository.save(savedCommandeClient);
        return CommandeClientDto.fromEntity(savedCommandeClient);
    }

    private ServiceDelivery getServiceDeliveryById(Integer id_sd){
        log.info("Fetching ServiceDelivery with ID: {}", id_sd);
        ServiceDeliveryDto serviceDeliveryDto = serviceDeliveryService.findById(id_sd);
        log.info("Fetched ServiceDelivery: {}", serviceDeliveryDto);
        return ServiceDeliveryDto.toEntity(serviceDeliveryDto);
    }
    @Override
    public CommandeClientResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Commande Client est null",
                "Impossible de trouver une commande client avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);

       CommandeClient commandeClientOp= commandeClientRepository.findById(id).orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                + id +"n'a ete trouvé dans la base deonnée"
                ,ErrorCodes.COMMANDECLIENT_NOT_FOUND));
        CommandeClientResponse commandeClientResponse= CommandeClientResponse.fromEntity(commandeClientOp);
        setCreatedByUser(commandeClientResponse);
        return commandeClientResponse;
    }

    @Override
    public CommandeClientResponse findByCode(String code) {
        CheckParmasUtil.checkCode(code,"ERREUR ! Le parametre Code Commande client  est null ",
                "Impossible de trouver une commande client avec un Code null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);

        Optional<CommandeClient> commandeClientOp= commandeClientRepository.findCommandeClientByCode(code);
        CommandeClientResponse commandeClientResponse=Optional.of(CommandeClientResponse.fromEntity(commandeClientOp.get()))
                .orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                        + code +"n'a ete trouvé dans la base deonnée"
                        ,ErrorCodes.COMMANDECLIENT_NOT_FOUND));
        setCreatedByUser(commandeClientResponse);
        return commandeClientResponse;
    }

    @Override
    public List<CommandeClientResponse> findAll() {
        return commandeClientRepository.findAll().stream()
                //.map(CommandeClientResponse::fromEntity)
                .map(order->{
                    CommandeClientResponse commandeClientResponse=CommandeClientResponse.fromEntity(order);
                    setCreatedByUser(commandeClientResponse);
                    return  commandeClientResponse;
                })
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

    @Override
    public PageResponse<CommandeClientResponse> findAllCmdClient(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CommandeClient> commandeClients= commandeClientRepository.findAllCmdsClient(pageable);
        List<CommandeClientResponse> commandeClientDtos = commandeClients.stream()
                //.map(CommandeClientResponse::fromEntity)
                .map(order->{
                    CommandeClientResponse commandeClientResponse=CommandeClientResponse.fromEntity(order);
                    setCreatedByUser(commandeClientResponse);
                    return commandeClientResponse;
                })
                .toList();


        return new PageResponse<>(
                commandeClientDtos,
                commandeClients.getNumber(),
                commandeClients.getSize(),
                commandeClients.getTotalElements(),
                commandeClients.getTotalPages(),
                commandeClients.isFirst(),
                commandeClients.isLast()
        );
    }

    private void checkDto(CommandeClientDto dto, String msgLog, String msgException, ErrorCodes commandeclientNotValid) {
        List<String> errors = CommandeClientValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.CHAUFFEUR_NOT_VALID,errors);
        }
    }

    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(CommandeClientResponse commandeClientResponse){
        UtilisateurDto uDto = getCreatedBy(commandeClientResponse.getCreatedBy());
        commandeClientResponse.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }
    }
