package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.exception.*;
import com.hitecart.deliverysystem.model.Chauffeur;
import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.ServiceDelivery;
import com.hitecart.deliverysystem.repository.ChauffeurRepository;
import com.hitecart.deliverysystem.repository.ContratRepository;
import com.hitecart.deliverysystem.repository.MissionRepository;
import com.hitecart.deliverysystem.repository.RoleRepository;
import com.hitecart.deliverysystem.services.ChauffeurService;
import com.hitecart.deliverysystem.validator.ChauffeurValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class  ChauffeurServiceImpl implements ChauffeurService {

    private final ChauffeurRepository chauffeurRepository;
    private final ContratRepository contratRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UtilisateurServiceImpl utilisateurServiceImpl;


    @Override
    public ChauffeurDto save(ChauffeurDto dto) {
        checkDto(dto,"Chauffeur n'est valid","le Chauffeur n'est pas valid");

        var userRole = roleRepository.findByName("USER")
                .orElseThrow(()->new IllegalStateException("ROLE USER was not initialized"));
        Chauffeur chauffeurToSave = ChauffeurDto.toEntity(dto);

        chauffeurToSave.setPassword(passwordEncoder.encode(dto.getPassword()));
        chauffeurToSave.setRoles(List.of(userRole));

        try {
             chauffeurToSave = chauffeurRepository.save(
                    ChauffeurDto.toEntity(dto));
        return ChauffeurDto.fromEntity(chauffeurToSave);
        } catch (DataIntegrityViolationException e) {
            // Handle the exception when trying to insert a duplicate drivingLicenseNum
            throw new DuplicateDrivingLicenseException("Duplicate driving license number: " + ChauffeurDto.toEntity(dto).getDrivinglicenseNbr(), e);
        }
    }


    @Override
    public ChauffeurDto findById(Integer id) {
        checkID(id,"Le parametre ID chauffeur est null",
                "Impossible de trouver un chauffeur avec un ID null");

        Optional<Chauffeur> chauffeurOp= chauffeurRepository.findById(id);
        ChauffeurDto chauffeurDto=  Optional.of(ChauffeurDto.fromEntity(chauffeurOp.get()))
                .orElseThrow(
                ()->new EntityNotFoundException(
                        "Opps! Aucun chauffeur avec l'id :"
                                + id +"n'a ete trouvé dans la base donnée",
                        ErrorCodes.CHAUFFEUR_NOT_FOUND)
        );

        setCreatedByUser(chauffeurDto);
        return chauffeurDto;
    }

    @Override
    public ChauffeurDto findByEmail(String email) {
    checkEmail(email,"ERREUR ! Le parametre EMAIL chauffeur est null ",
            "Impossible de trouver un chauffeur avec un EMAIL null");

        Optional<Chauffeur> chauffeurOp= chauffeurRepository.findChauffeurByEmail(email);

        ChauffeurDto chauffeurDto= Optional.of(ChauffeurDto.fromEntity(chauffeurOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Opps! Aucun chauffeur avec l'email :"
                                        + email +"n'a ete trouvé dans la base deonnée",
                                ErrorCodes.CHAUFFEUR_NOT_FOUND)
                );
        setCreatedByUser(chauffeurDto);
        return chauffeurDto;
    }

    @Override
    public List<ChauffeurDto> findAll() {
        List<Chauffeur> chauffeurs = chauffeurRepository.findAll();
        List<ChauffeurDto> chauffeurDtos =  chauffeurs.stream().map(ChauffeurDto::fromEntity).toList();
        // return chauffeurDtos;
        return chauffeurRepository.findAll().stream()
                .map(driver->{
                    ChauffeurDto driverDto = ChauffeurDto.fromEntity(driver);
                    setCreatedByUser(driverDto);
                    return driverDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ChauffeurDto> findAllIsActive(Boolean isActive) {
        return chauffeurRepository.findAllByEnabled(isActive).stream()
                //.map(ChauffeurDto::fromEntity)
                .map(driver->{
                    ChauffeurDto driverDto=ChauffeurDto.fromEntity(driver);
                    setCreatedByUser(driverDto);
                    return driverDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ChauffeurDto> findAllDrivers(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Chauffeur> drivers = chauffeurRepository.findAllChauffeurs(pageable);
        List<ChauffeurDto> chauffeurDtos = drivers.stream()
                //.map(ChauffeurDto::fromEntity)
                .map(driver->{
                    ChauffeurDto driverDto = ChauffeurDto.fromEntity(driver);
                    setCreatedByUser(driverDto);
                    return driverDto;
                })
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
    public ChauffeurDto updateIsActive(Integer id, Boolean enabled) {
        checkID(id,"l'ID du chauffeur à supprimer est null","Impossible de supprimer un chauffeur avec un ID null");
        Chauffeur chauffeurOp= chauffeurRepository.findById(id).orElseThrow(
                                ()-> new EntityNotFoundException("Opps! Aucun chauffeur avec l'id :"
                                   + id + "n'a ete trouvé dans la base donnée",
                           ErrorCodes.CHAUFFEUR_NOT_FOUND));
               chauffeurOp.setEnabled(enabled);
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

    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(ChauffeurDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
       response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

  private ChauffeurDto updatedResponse(Chauffeur driver) {
        ChauffeurDto driverDto=ChauffeurDto.fromEntity(driver);
        setCreatedByUser(driverDto);
        return driverDto;
    }
}
