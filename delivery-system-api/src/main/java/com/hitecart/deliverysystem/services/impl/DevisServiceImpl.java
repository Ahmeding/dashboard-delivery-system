package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.*;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.LigneDevis;
import com.hitecart.deliverysystem.dao.DevisRepository;
import com.hitecart.deliverysystem.dao.LigneDevisRepository;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.services.DevisService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.utils.CalculeTva;
import com.hitecart.deliverysystem.validator.DevisValidator;
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
public class DevisServiceImpl implements DevisService {

    private final DevisRepository devisRepository;
    private final LigneDevisRepository ligneDevisRepository;
    private final ClientService clientService;
    private final UtilisateurService utilisateurServiceImpl;


    @Override
    public DevisDto save(DevisDto dto) {
        checkDto(dto,"Commande Client n'est pas valide",
                "Le devis n'est pas valide",ErrorCodes.COMMANDECLIENT_NOT_VALID);

        if(dto.getId()!=null && dto.isDevisConfirmed()){
            throw new InvalidOperationException("Unable to modify the quotes once it is completed",
                    ErrorCodes.DEVIS_NOT_VALID_OPERATION);
        }


        Client client= ClientDto.toEntity(clientService.findById(dto.getClientId()));

        Devis savedDevis = DevisDto.toEntity(dto);
        savedDevis.setClient(client);
        savedDevis = devisRepository.save(savedDevis);

        BigDecimal[] totalAmountTTC= {BigDecimal.ZERO};
        BigDecimal[] totalAmountHT = {BigDecimal.ZERO};

        if(!dto.getLigneDevisDtos().isEmpty()){
            Devis finalSavedDevis = savedDevis;
            dto.getLigneDevisDtos().forEach(devisLine-> {

                BigDecimal prixTTC = CalculeTva.calculeTTC(devisLine.getPrixUnitaire(),devisLine.getTva());
                totalAmountTTC[0] =totalAmountTTC[0].add(prixTTC.multiply(devisLine.getQuantite()));
                totalAmountHT[0] =totalAmountHT[0].add(devisLine.getPrixUnitaire().multiply(devisLine.getQuantite()));

                LigneDevis devisLineToSave= LigneDevisDto.toEntity(devisLine);
                devisLineToSave.setDevis(finalSavedDevis);
                devisLineToSave.setPrixTTC(prixTTC);
                ligneDevisRepository.save(devisLineToSave);
            });
        }

        savedDevis.setMontantTotalHT(totalAmountHT[0]);
        savedDevis.setMontantTotalTTC(totalAmountTTC[0]);

        savedDevis = devisRepository.save(savedDevis);

        return DevisDto.fromEntity(savedDevis);

    }

    @Override
    public DevisDtoResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Commande Client est null",
                "Impossible de trouver un devis avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);
        Optional<Devis> devisOp= devisRepository.findById(id);
        DevisDtoResponse devisDtoResponse = Optional.of(DevisDtoResponse.fromEntity(devisOp.get()))
                .orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                        + id +"n'a ete trouvé dans la base deonnée"
                        ,ErrorCodes.COMMANDECLIENT_NOT_FOUND));
        setCreatedByUser(devisDtoResponse);
        return devisDtoResponse;
    }

    @Override
    public DevisDtoResponse findByCode(String code) {

        CheckParmasUtil.checkCode(code,"ERREUR ! Le parametre Code Commande client  est null ",
                "Impossible de trouver un devis avec un Code null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);

        Optional<Devis> devisOp= devisRepository.findDevisByCode(code);

        DevisDtoResponse devisDtoResponse = Optional.of(DevisDtoResponse.fromEntity(devisOp.get()))
                .orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                        + code +"n'a ete trouvé dans la base deonnée"
                        ,ErrorCodes.DEVIS_NOT_FOUND));

        setCreatedByUser(devisDtoResponse);
        return devisDtoResponse;
    }

    @Override
    public List<DevisDtoResponse> findAll() {


        return devisRepository.findAll().stream()
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID du devis  à supprimer est null",
                "Impossible de supprimer un devis  avec un ID null",ErrorCodes.DEVIS_NOT_VALID_OPERATION);

        List<LigneDevis> ligneDeviss= ligneDevisRepository.findAllByDevisId(id);
        if(!ligneDeviss.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un devis qui a deja des lignes de commandes"
                    ,ErrorCodes.DEVIS_NOT_VALID_OPERATION);
        }
        devisRepository.deleteById(id);
    }

    @Override
    public PageResponse<DevisDtoResponse> findAllDevis(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Devis> devis= devisRepository.findAllDevis(pageable);
        List<DevisDtoResponse> devisDtos = devis.stream()
                //.map(DevisDtoResponse::fromEntity)
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                devisDtos,
                devis.getNumber(),
                devis.getSize(),
                devis.getTotalElements(),
                devis.getTotalPages(),
                devis.isFirst(),
                devis.isLast()
        );
    }

    private void checkDto(DevisDto dto, String msgLog, String msgException, ErrorCodes commandeclientNotValid) {
        List<String> errors = DevisValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.CHAUFFEUR_NOT_VALID,errors);
        }
    }


    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(DevisDtoResponse response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private DevisDtoResponse updatedResponse(Devis devis) {
        DevisDtoResponse devisDto=DevisDtoResponse.fromEntity(devis);
        setCreatedByUser(devisDto);
        return devisDto;
    }

}
