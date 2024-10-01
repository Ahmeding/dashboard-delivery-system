package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.DevisDto;
import com.hitecart.deliverysystem.dto.DevisDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.LigneDevis;
import com.hitecart.deliverysystem.repository.DevisRepository;
import com.hitecart.deliverysystem.repository.LigneDevisRepository;
import com.hitecart.deliverysystem.services.DevisService;
import com.hitecart.deliverysystem.validator.DevisValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DevisServiceImpl implements DevisService {
    
    private DevisRepository devisRepository;
    private LigneDevisRepository ligneDevisRepository;

    public DevisServiceImpl(DevisRepository devisRepository, LigneDevisRepository ligneDevisRepository) {
        this.devisRepository = devisRepository;
        this.ligneDevisRepository = ligneDevisRepository;
    }

    @Override
    public DevisDto save(DevisDto dto) {
        checkDto(dto,"Commande Client n'est pas valide",
                "Le devis n'est pas valide",ErrorCodes.COMMANDECLIENT_NOT_VALID);
        return DevisDto.fromEntity(devisRepository.save(
                DevisDto.toEntity(dto)
        ));
    }

    @Override
    public DevisDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Commande Client est null",
                "Impossible de trouver un devis avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);
        Optional<Devis> devisOp= devisRepository.findById(id);

        //return DevisDto.fromEntity(devisOp.get());
        return Optional.of(DevisDto.fromEntity(devisOp.get()))
                .orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                        + id +"n'a ete trouvé dans la base deonnée"
                        ,ErrorCodes.COMMANDECLIENT_NOT_FOUND));
    }

    @Override
    public DevisDto findByCode(String code) {

        CheckParmasUtil.checkCode(code,"ERREUR ! Le parametre Code Commande client  est null ",
                "Impossible de trouver un devis avec un Code null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);

        Optional<Devis> devisOp= devisRepository.findDevisByCode(code);

        return Optional.of(DevisDto.fromEntity(devisOp.get()))
                .orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                        + code +"n'a ete trouvé dans la base deonnée"
                        ,ErrorCodes.DEVIS_NOT_FOUND));


    }

    @Override
    public List<DevisDto> findAll() {


        return devisRepository.findAll().stream()
                .map(DevisDto::fromEntity)
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

    private void checkDto(DevisDto dto, String msgLog, String msgException, ErrorCodes commandeclientNotValid) {
        List<String> errors = DevisValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.CHAUFFEUR_NOT_VALID,errors);
        }
    }
}
