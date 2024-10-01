package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.CommandeClient;
import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.repository.ClientRepository;
import com.hitecart.deliverysystem.repository.CommandeClientRepository;
import com.hitecart.deliverysystem.repository.DevisRepository;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.validator.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private CommandeClientRepository commandeClientRepository;
    private DevisRepository devisRepository;

    public ClientServiceImpl(ClientRepository clientRepository, CommandeClientRepository commandeClientRepository, DevisRepository devisRepository) {
        this.clientRepository = clientRepository;
        this.commandeClientRepository = commandeClientRepository;
        this.devisRepository = devisRepository;
    }

    @Override
    public ClientDto save(ClientDto dto) {
        checkDto(dto,"Client not valid","le Client n'est pas valid");
        return ClientDto.fromEntity(clientRepository.save(ClientDto.toEntity(dto)));
    }

    @Override
    public ClientDto findById(Integer id) {
        CheckParmasUtil.checkID(id,"Le parametre ID chauffeur est null",
                "Impossible de trouver un client avec un ID null",ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        Optional<Client> clientOp = clientRepository.findById(id);
        return Optional.of(
                ClientDto.fromEntity(clientOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                        "Opps! Aucun client avec l'id :"
                        + id +"n'a ete trouvé dans la base deonnée",
                        ErrorCodes.CLIENT_NOT_FOUND
                        ));
    }

    @Override
    public ClientDto findByEmail(String email) {
        CheckParmasUtil.checkStringParma(email,"","",ErrorCodes.CHAUFFEUR_NOT_VALID_OPERATION);
        Optional<Client> clientOp= clientRepository.findClientByAdresseEmail(email);

        return Optional.of(ClientDto.fromEntity(clientOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Opps! Aucun client avec l'email :"
                                        + email +"n'a ete trouvé dans la base deonnée",
                                ErrorCodes.CLIENT_NOT_FOUND
                        )
                );
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                        .map(ClientDto::fromEntity)
                        .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"","",ErrorCodes.CLIENT_NOT_VALID_OPERATION);
        List<CommandeClient> commandeClients = commandeClientRepository.findAllByClientId(id);
        if(!commandeClients.isEmpty()){
           throw new InvalidOperationException("Impoosibles de supprimer un client qui a deja des commandes"
                   ,ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        }
        List<Devis> listDevis = devisRepository.findAllByClientId(id);
        if(!listDevis.isEmpty()){
            throw new InvalidOperationException("Impoosibles de supprimer un client qui a deja des devis"
                    ,ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        }

        clientRepository.deleteById(id);
    }

    private void checkDto(ClientDto dto, String msgLog, String msgException) {
        List<String> errors = ClientValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.CHAUFFEUR_NOT_VALID,errors);
        }
    }
}
