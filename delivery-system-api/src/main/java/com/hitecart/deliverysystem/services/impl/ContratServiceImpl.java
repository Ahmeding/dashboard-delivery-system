package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.repository.ContratRepository;
import com.hitecart.deliverysystem.services.ContratService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.validator.ContratValidator;
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
public class ContratServiceImpl implements ContratService {
    
    private final ContratRepository contratRepository;
    private final UtilisateurService utilisateurServiceImpl;

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

        ContratDto contratDto =  Optional.of(ContratDto.fromEntity(contratOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Aucun Contrat avec L'ID "+id+"n'a ete trouvé dans la base des données",
                                ErrorCodes.CONTRAT_NOT_FOUND)
                );

        setCreatedByUser(contratDto);
        return contratDto;
    }

    @Override
    public List<ContratDto> findAllByIdChauffeur(Integer idChauffeur) {
        CheckParmasUtil.checkID(idChauffeur,
                "ERREUR: Le parametre Id chauffeur est null",
                "Impossible de trouver la liste des contrat par chauffaur avec Id chauffeur null",
                ErrorCodes.CONTRAT_NOT_VALID_OPERATION);

        List<Contrat> contrats = contratRepository.findAllByChauffeur_Id(idChauffeur);

        return contrats.stream()
                //.map(ContratDto::fromEntity)
                .map(this::updatedResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<ContratDto> findAll() {

        return contratRepository.findAll().stream()
                //.map(ContratDto::fromEntity)
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID du contrat à supprimer est null",
                "Impossible de supprimer un contrat avec un ID null",ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        contratRepository.deleteById(id);
    }

    @Override
    public PageResponse<ContratDto> findAllContracts(int page, int size) {
        Pageable pageable
                = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Contrat> contrats = contratRepository.findAllContracts(pageable);
        List<ContratDto> contratDtos = contrats.stream()
                //.map(ContratDto::fromEntity)
                .map(this::updatedResponse)
                .toList();
        return new PageResponse<>(
                contratDtos,
                contrats.getNumber(),
                contrats.getSize(),
                contrats.getTotalElements(),
                contrats.getTotalPages(),
                contrats.isFirst(),
                contrats.isLast()
        );
    }

    private void checkDto(ContratDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = ContratValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }


    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(ContratDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private ContratDto updatedResponse(Contrat contrat) {
        ContratDto contratDto=ContratDto.fromEntity(contrat);
        setCreatedByUser(contratDto);
        return contratDto;
    }

}
