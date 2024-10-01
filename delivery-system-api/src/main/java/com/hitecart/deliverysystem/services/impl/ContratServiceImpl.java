package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.ContratDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.repository.ContratRepository;
import com.hitecart.deliverysystem.services.ContratService;
import com.hitecart.deliverysystem.validator.ContratValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContratServiceImpl implements ContratService {
    
    private final ContratRepository contratRepository;

    public ContratServiceImpl(ContratRepository contratRepository) {
        this.contratRepository = contratRepository;
    }

    @Override
    public ContratDto save(ContratDto dto) {

        checkDto(
                dto,
                "Contrat n'est pas valid",
                "Le contrat n'est pa valide ",
                ErrorCodes.CONTRAT_NOT_VALID);
        return ContratDto.fromEntity(contratRepository.save(ContratDto.toEntity(dto)));
    }

    @Override
    public ContratDto findById(Integer id) {

        CheckParmasUtil.checkID(
                id,
                "ID contrat est null",
                "Immpossible de trouver un contrat avec un id null",
                ErrorCodes.CONTRAT_NOT_VALID_OPERATION);

        Optional<Contrat> contratOp = contratRepository.findById(id);

        return Optional.of(ContratDto.fromEntity(contratOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Aucun Contrat avec L'ID "+id+"n'a ete trouvé dans la base des données",
                                ErrorCodes.CONTRAT_NOT_FOUND)
                );
    }

    @Override
    public List<ContratDto> findAllByIdChauffeur(Integer idChauffeur) {
        CheckParmasUtil.checkID(idChauffeur,
                "ERREUR: Le parametre Id chauffeur est null",
                "Impossible de trouver la liste des contrat par chauffaur avec Id chauffeur null",
                ErrorCodes.CONTRAT_NOT_VALID_OPERATION);

        List<Contrat> contrats = contratRepository.findAllByChauffeur_Id(idChauffeur);

        return contrats.stream()
                .map(ContratDto::fromEntity)
                .collect(Collectors.toList());

    }

    @Override
    public List<ContratDto> findAll() {

        return contratRepository.findAll().stream()
                .map(ContratDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID du contrat à supprimer est null",
                "Impossible de supprimer un contrat avec un ID null",ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        contratRepository.deleteById(id);
    }
    private void checkDto(ContratDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = ContratValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }
}
