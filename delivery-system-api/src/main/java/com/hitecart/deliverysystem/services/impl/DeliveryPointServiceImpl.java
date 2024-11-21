package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.dto.CreatedByUserDto;
import com.hitecart.deliverysystem.dto.DeliveryPointDto;
import com.hitecart.deliverysystem.dto.UtilisateurDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.repository.DeliveryPointRepository;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.services.IDeliveryPointService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.validator.DeliveryPointValidator;
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
public class DeliveryPointServiceImpl implements IDeliveryPointService {

    private final DeliveryPointRepository deliveryPointRepository;

    private final UtilisateurService utilisateurService;
    private final ClientService clientService;

    @Override
    public DeliveryPointDto save(DeliveryPointDto dto) {
        checkDto(dto,"DeliveryPoint not valid","le DeliveryPoint n'est pas valid");

        Client client
                = ClientDto.toEntity(clientService.findById(dto.getClientId()));
        DeliveryPoint deliveryPointToSave = DeliveryPointDto.toEntity(dto);
        deliveryPointToSave.setClient(client);

        return DeliveryPointDto.fromEntity(deliveryPointRepository.save(deliveryPointToSave));
    }

    @Override
    public DeliveryPointDto findById(Integer id) {
        CheckParmasUtil.checkID(id,"Le parametre ID DeliveryPoint est null",
                "Impossible de trouver un DeliveryPoint avec un ID null",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);

        DeliveryPoint DeliveryPointOp = deliveryPointRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException( "Opps! Aucun DeliveryPoint avec l'id :"+ id +"n'a ete trouvé dans la base de donnée",
                        ErrorCodes.DELIVERY_POINT_NOT_FOUND));

        DeliveryPointDto deliveryPointDto =
                        DeliveryPointDto.fromEntity(DeliveryPointOp);

        setCreatedByUser(deliveryPointDto);
        return deliveryPointDto;
    }

    @Override
    public DeliveryPointDto findByCity(String city) {
        CheckParmasUtil.checkStringParma(city,"","",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);
        Optional<DeliveryPoint> deliveryPointOp= deliveryPointRepository.findDeliveryPointByCity(city);
        DeliveryPointDto deliveryPointDto =Optional.of(DeliveryPointDto.fromEntity(deliveryPointOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Opps! Aucun deliveryPoint avec la ville :"
                                        + city +"n'a ete trouvé dans la base de donnée",
                                ErrorCodes.DELIVERY_POINT_NOT_FOUND
                        )
                );

        setCreatedByUser(deliveryPointDto);
        return deliveryPointDto;
    }

    @Override
    public List<DeliveryPointDto> findAll() {
        return deliveryPointRepository.findAll().stream()
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryPointDto updateIsActive(Integer id, Boolean enabled) {
        CheckParmasUtil.checkID(id,"Le parametre ID deliveryPoint est null",
                "Impossible de trouver un deliveryPoint avec un ID null",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);

        DeliveryPoint deliveryPointOp = deliveryPointRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Opps! Aucun deliveryPoint avec l'id :"
                        + id + "n'a ete trouvé dans la base deonnée",
                        ErrorCodes.CLIENT_NOT_FOUND)
        );
        deliveryPointOp.setEnabled(enabled);
        DeliveryPointDto deliveryPointDto = DeliveryPointDto.fromEntity(deliveryPointRepository.save(deliveryPointOp));
        setCreatedByUser(deliveryPointDto);
        return deliveryPointDto ;
    }

    @Override
    public void delete(Integer id) {

        CheckParmasUtil.checkID(id,"","",ErrorCodes.DELIVERY_POINT_NOT_VALID_OPERATION);

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
        deliveryPointRepository.deleteById(id);
    }

    @Override
    public PageResponse<DeliveryPointDto> findAllDeliveryPoints(int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        Page<DeliveryPoint> deliveryPoints = deliveryPointRepository.findAll(pageable);
        List<DeliveryPointDto> deliveryPointsDto = deliveryPoints.stream()
                .map(this::updatedResponse)
                .toList();


        return new PageResponse<>(
                deliveryPointsDto,
                deliveryPoints.getNumber(),
                deliveryPoints.getSize(),
                deliveryPoints.getTotalElements(),
                deliveryPoints.getTotalPages(),
                deliveryPoints.isFirst(),
                deliveryPoints.isLast()
        );
    }



    private void checkDto(DeliveryPointDto dto, String msgLog, String msgException) {
        List<String> errors = DeliveryPointValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.DELIVERY_POINT_NOT_FOUND,errors);
        }
    }

    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurService.findById(userid);
    }

    private void setCreatedByUser(DeliveryPointDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private DeliveryPointDto updatedResponse(DeliveryPoint deliveryPoint) {
        DeliveryPointDto deliveryPointDto =DeliveryPointDto.fromEntity(deliveryPoint);
        setCreatedByUser(deliveryPointDto);
        return deliveryPointDto;
    }




}
