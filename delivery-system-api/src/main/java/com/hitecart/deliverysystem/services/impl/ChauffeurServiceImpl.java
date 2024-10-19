package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ChauffeurDto;
import com.hitecart.deliverysystem.dto.ContratDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.Chauffeur;
import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.repository.ChauffeurRepository;
import com.hitecart.deliverysystem.repository.ContratRepository;
import com.hitecart.deliverysystem.repository.MissionRepository;
import com.hitecart.deliverysystem.services.ChauffeurService;
import com.hitecart.deliverysystem.validator.ChauffeurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class  ChauffeurServiceImpl implements ChauffeurService {

    private ChauffeurRepository chauffeurRepository;
    private ContratRepository contratRepository;


    @Autowired
    public ChauffeurServiceImpl(ChauffeurRepository chauffeurRepository,  ContratRepository contratRepository) {
        this.chauffeurRepository = chauffeurRepository;
        this.contratRepository = contratRepository;
    }

    @Override
    public ChauffeurDto save(ChauffeurDto dto) {
        checkDto(dto,"Chauffeur n'est valid","le Chauffeur n'est pas valid");
        return ChauffeurDto.fromEntity(chauffeurRepository.save(
                ChauffeurDto.toEntity(dto)
        ));
    }


    @Override
    public ChauffeurDto findById(Integer id) {
        checkID(id,"Le parametre ID chauffeur est null",
                "Impossible de trouver un chauffeur avec un ID null");

        Optional<Chauffeur> chauffeurOp= chauffeurRepository.findById(id);
        return Optional.of(ChauffeurDto.fromEntity(chauffeurOp.get()))
                .orElseThrow(
                ()->new EntityNotFoundException(
                        "Opps! Aucun chauffeur avec l'id :"
                                + id +"n'a ete trouvé dans la base donnée",
                        ErrorCodes.CHAUFFEUR_NOT_FOUND)
        );
    }



    @Override
    public ChauffeurDto findByEmail(String email) {
    checkEmail(email,"ERREUR ! Le parametre EMAIL chauffeur est null ",
            "Impossible de trouver un chauffeur avec un EMAIL null");

        Optional<Chauffeur> chauffeurOp= chauffeurRepository.findChauffeurByAdresseEmail(email);
        return Optional.of(ChauffeurDto.fromEntity(chauffeurOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Opps! Aucun chauffeur avec l'email :"
                                        + email +"n'a ete trouvé dans la base deonnée",
                                ErrorCodes.CHAUFFEUR_NOT_FOUND)
                );
    }



    @Override
    public List<ChauffeurDto> findAll() {
        return chauffeurRepository.findAll().stream()
                .map(ChauffeurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChauffeurDto> findAllIsActive(Boolean isActive) {
        return chauffeurRepository.findAllByIsActive(isActive).stream()
                .map(ChauffeurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ChauffeurDto> findAllDrivers(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Chauffeur> drivers = chauffeurRepository.findAllChauffeurs(pageable);
        List<ChauffeurDto> chauffeurDtos = drivers.stream()
                .map(ChauffeurDto::fromEntity)
                .toList();
        return new PageResponse<>(
                chauffeurDtos,
                drivers.getNumber(),
                drivers.getSize(),
                drivers.getTotalElements(),
                drivers.getTotalPages(),
                drivers.isFirst(),
                drivers.isLast()
        );
    }

    @Override
    public void delete(Integer id) {
        checkID(id,"l'ID du chauffeur à supprimer est null","Impossible de supprimer un chauffeur avec un ID null");

        // TODO CREATE QUERY DELETE FROM MANY_TO_MANY RELATION
        //List<Mission> missions = missionRepository.findAllByChauffeurId(id);
        List<Contrat> contrats = contratRepository.findAllByChauffeur_Id(id);
        if(!contrats.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un chaffeur qui a deja des contrat"
            ,ErrorCodes.CHAUFFEUR_NOT_VALID_OPERATION);
        }
        chauffeurRepository.deleteById(id);
    }

    @Override
    public ChauffeurDto updateIsActive(Integer id, Boolean isActive) {
        checkID(id,"l'ID du chauffeur à supprimer est null","Impossible de supprimer un chauffeur avec un ID null");
        Chauffeur chauffeurOp= chauffeurRepository.findById(id).orElseThrow(
                                ()-> new EntityNotFoundException("Opps! Aucun chauffeur avec l'id :"
                                   + id + "n'a ete trouvé dans la base donnée",
                           ErrorCodes.CHAUFFEUR_NOT_FOUND));
               chauffeurOp.setIsActive(isActive);
        return  ChauffeurDto.fromEntity(chauffeurRepository.save(chauffeurOp));
    }

    private void checkID(Integer id, String msgLog, String msgException) {
        if (id == null) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    ErrorCodes.CHAUFFEUR_NOT_VALID_OPERATION);
        }
    }

    private void checkEmail(String email, String msgLog, String msgException) {
        if (!StringUtils.hasLength(email)) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    ErrorCodes.CHAUFFEUR_NOT_VALID_OPERATION);
        }
    }


    private void checkDto(ChauffeurDto dto, String msgLog, String msgException) {
        List<String> errors = ChauffeurValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.CHAUFFEUR_NOT_VALID,errors);
        }
    }
}
