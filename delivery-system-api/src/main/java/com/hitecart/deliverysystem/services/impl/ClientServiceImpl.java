package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ChauffeurDto;
import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.dto.CreatedByUserDto;
import com.hitecart.deliverysystem.dto.UtilisateurDto;
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
import com.hitecart.deliverysystem.repository.UtilisateurRepository;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.validator.ClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final CommandeClientRepository commandeClientRepository;
    private final DevisRepository devisRepository;
    private final UtilisateurService utilisateurService;


    @Override
    public ClientDto save(ClientDto dto) {
        checkDto(dto,"Client not valid","le Client n'est pas valid");
        return ClientDto.fromEntity(clientRepository.save(ClientDto.toEntity(dto)));
    }

    @Override
    public ClientDto findById(Integer id) {
        CheckParmasUtil.checkID(id,"Le parametre ID chauffeur est null",
                "Impossible de trouver un client avec un ID null",ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        Client clientOp = clientRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException(
                        "Opps! Aucun client avec l'id :"
                                + id +"n'a ete trouvé dans la base deonnée",
                        ErrorCodes.CLIENT_NOT_FOUND
                )
        );
        ClientDto clientDto =
                ClientDto.fromEntity(clientOp);


        setCreatedByUser(clientDto);
        return clientDto;
    }

    @Override
    public ClientDto findByEmail(String email) {
        CheckParmasUtil.checkStringParma(email,"","",ErrorCodes.CLIENT_NOT_VALID_OPERATION);
        Optional<Client> clientOp= clientRepository.findClientByEmail(email);


        ClientDto clientDto =Optional.of(ClientDto.fromEntity(clientOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Opps! Aucun client avec l'email :"
                                        + email +"n'a ete trouvé dans la base deonnée",
                                ErrorCodes.CLIENT_NOT_FOUND
                        )
                );

        setCreatedByUser(clientDto);
        return clientDto;
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                        //.map(ClientDto::fromEntity)
                        .map(this::updatedResponse)
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

    @Override
    public ClientDto updateIsActive(Integer id, Boolean enabled) {
        CheckParmasUtil.checkID(id,"Le parametre ID chauffeur est null",
                "Impossible de trouver un client avec un ID null",ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        Client clientOp = clientRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Opps! Aucun client avec l'id :"
                          + id + "n'a ete trouvé dans la base deonnée",
                  ErrorCodes.CLIENT_NOT_FOUND)
        );
        clientOp.setEnabled(enabled);

        ClientDto clientDto = ClientDto.fromEntity(clientRepository.save(clientOp));
        setCreatedByUser(clientDto);
        return clientDto ;
    }

    @Override
    public PageResponse<ClientDto> findAllClients(int page, int size) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(
                "createdAt").descending());
        Page<Client> clients = clientRepository.findAllClients(pageable);

        List<ClientDto> clientsDto = clients.stream()
                //.map(ClientDto::fromEntity)
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                clientsDto,
                clients.getNumber(),
                clients.getSize(),
                clients.getTotalElements(),
                clients.getTotalPages(),
                clients.isFirst(),
                clients.isLast()
        );
    }


    private void checkDto(ClientDto dto, String msgLog, String msgException) {
        List<String> errors = ClientValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.CHAUFFEUR_NOT_VALID,errors);
        }
    }

    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurService.findById(userid);
    }
    private void setCreatedByUser(ClientDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private ClientDto updatedResponse(Client client) {
        ClientDto clientDto=ClientDto.fromEntity(client);
        setCreatedByUser(clientDto);
        return clientDto;
    }

}
