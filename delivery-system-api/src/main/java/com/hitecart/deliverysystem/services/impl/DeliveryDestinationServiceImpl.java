package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.DeliveryDestination;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.ServiceDelivery;
import com.hitecart.deliverysystem.repository.DeliveryDestinationRepository;
import com.hitecart.deliverysystem.repository.DeliveryPointRepository;
import com.hitecart.deliverysystem.repository.IServiceDeliveryRepository;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.services.IServiceDeliveryService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.services.IDeliveryDestinationService;
import com.hitecart.deliverysystem.validator.DeliveryDestinationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryDestinationServiceImpl implements IDeliveryDestinationService {

    private final DeliveryDestinationRepository deliveryDestinationRepository;
    private final IServiceDeliveryService serviceDeliveryService;
    private final DeliveryPointRepository deliveryPointRepository;
    private final IServiceDeliveryRepository serviceDeliveryRepository;

    private final UtilisateurService utilisateurService;
    private final ClientService clientService;

    @Override
    public DeliveryDestinationDto save(DeliveryDestinationDto dto) {
        log.error( dto.toString());
        checkDto(dto,"Delivery Destination not valid","le Delivery Destination n'est pas valid");

        DeliveryDestination ddToSave = DeliveryDestinationDto.toEntity(dto);

        Client client
                = ClientDto.toEntity(clientService.findById(dto.getClientId()));
        ddToSave.setClient(client);

        ServiceDelivery sdToSave
                = ServiceDeliveryDto.toEntity(serviceDeliveryService.findById(dto.getServiceDeliveryId()));
        ddToSave.setServiceDelivery(sdToSave);

        DeliveryDestination deliveryDestinationToSave
                     = deliveryDestinationRepository.save(ddToSave);

        if(!dto.getDeliveryPointsDto().isEmpty()){
            dto.getDeliveryPointsDto()
                    .forEach(dp->{
                        DeliveryPoint deliveryPointToSave = DeliveryPointDto.toEntity(dp);
                deliveryPointToSave.setDeliveryDestination(deliveryDestinationToSave);
                deliveryPointRepository.save(deliveryPointToSave);
                    });
        }
        DeliveryDestinationDto deliveryDestinationSaved=
                DeliveryDestinationDto.fromEntity( deliveryDestinationToSave);

        return deliveryDestinationSaved;
    }

    @Override
    public DeliveryDestinationDto findById(Integer id) {
        CheckParmasUtil.checkID(id,"Le parametre ID Delivery Destination est null",
                "Impossible de trouver un Delivery Destination avec un ID null",ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);

        DeliveryDestination DeliveryDestinationOp = deliveryDestinationRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException( "Opps! Aucun Delivery Destination avec l'id :"+ id +"n'a ete trouvé dans la base de donnée",
                        ErrorCodes.DELIVERY_DESTINATION_NOT_FOUND));

        DeliveryDestinationDto deliveryDestinationDto =
                DeliveryDestinationDto.fromEntity(DeliveryDestinationOp);

        setCreatedByUser(deliveryDestinationDto);
        return deliveryDestinationDto;
    }


    @Override
    public List<DeliveryDestinationDto> findAllByClientId(Integer clientId) {

        CheckParmasUtil.checkID(clientId,"Le parametre ID est null",
                "Impossible de trouver un Delivery Destination avec un parametre id client  null",ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);

      List<DeliveryDestination> deliveryDestinations = deliveryDestinationRepository.findAllByClientId(clientId);

      if(deliveryDestinations.isEmpty()) {
          throw new EntityNotFoundException("Opps! Aucun Delivery Destination avec l'id client :" + clientId + "n'a ete trouvé dans la base de donnée",
                  ErrorCodes.DELIVERY_DESTINATION_NOT_FOUND);
      }

        return deliveryDestinations.stream()
                .map(this::updatedResponse).toList();
    }

    @Override
    public List<DeliveryDestinationDto> findAll() {

        return deliveryDestinationRepository.findAll().stream()
                .map(this::updatedResponse)
                .toList();
    }

    @Override
    public void delete(Integer id) {


        CheckParmasUtil.checkID(id,"","",ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);

      /*  List<CommandeClient> commandeClients = commandeClientRepository.findAllByClientId(id);
        if(!commandeClients.isEmpty()){
            throw new InvalidOperationException("Impoosibles de supprimer un client qui a deja des commandes"
                    ,ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        }
        List<Devis> listDevis = devisRepository.findAllByClientId(id);
        if(!listDevis.isEmpty()){
            throw new InvalidOperationException("Impoosibles de supprimer un client qui a deja des devis"
                    ,ErrorCodes.CLIENT_NOT_VALID_OPERATION);
        }
*/
        deliveryDestinationRepository.deleteById(id);
    }

    @Override
    public PageResponse<DeliveryDestinationDto> findAllDeliveryDestinations(int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        Page<DeliveryDestination> deliveryDestinations = deliveryDestinationRepository.findAll(pageable);
        List<DeliveryDestinationDto>deliveryDestinationsDto =deliveryDestinations.stream()
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                deliveryDestinationsDto,
                deliveryDestinations.getNumber(),
                deliveryDestinations.getSize(),
                 deliveryDestinations.getTotalElements(),
                deliveryDestinations.getTotalPages(),
                deliveryDestinations.isFirst(),
                deliveryDestinations.isLast()

        );
    }



    private void checkDto(DeliveryDestinationDto dto, String msgLog, String msgException) {
        List<String> errors = DeliveryDestinationValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.DELIVERY_POINT_NOT_FOUND,errors);
        }
    }
    private void setCreatedByUser(DeliveryDestinationDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurService.findById(userid);
    }

    private DeliveryDestinationDto updatedResponse(DeliveryDestination deliveryDestination) {
        DeliveryDestinationDto deliveryDestinationDto =DeliveryDestinationDto.fromEntity(deliveryDestination);
        setCreatedByUser(deliveryDestinationDto);
        return deliveryDestinationDto;
    }

}
