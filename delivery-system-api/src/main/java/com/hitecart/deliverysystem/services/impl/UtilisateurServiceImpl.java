package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.dto.UtilisateurDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Devis;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.repository.UtilisateurRepository;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.validator.UtilisateurValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
public class UtilisateurServiceImpl implements UtilisateurService {


    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
        checkDto(dto,"Utilisateur n'est pas valide",
                "l'Utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID_OPERATION);
        return UtilisateurDto.fromEntity(utilisateurRepository.
                save(UtilisateurDto.toEntity(dto)));
    }

    @Override
    @Cacheable("users")
    public UtilisateurDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Utilisateur est null",
                "Impossible de trouver une utilisateur avec un ID null",
                ErrorCodes.UTILISATEUR_NOT_VALID_OPERATION);
        Optional<Utilisateur> utilisateurOp = utilisateurRepository.findById(id);

        UtilisateurDto userDto = Optional.of(UtilisateurDto.fromEntity(utilisateurOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune Utilisateur avec l'id :"
                                + id +"n'a ete trouvé dans la base deonnée",ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
        //setCreatedByUser(userDto);
        return
                userDto;
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        CheckParmasUtil.checkStringParma(email,
                "ERREUR: Le parametre email est null",
                "Impossible de trouver un utilisateur avec email null",
                ErrorCodes.UTILISATEUR_NOT_VALID_OPERATION);

        Optional<Utilisateur> utilisateurOp = utilisateurRepository.findByEmail(email);

        UtilisateurDto userDto = Optional.of(UtilisateurDto.fromEntity(utilisateurOp.get()))
                .orElseThrow(
                        ()-> new EntityNotFoundException(
                                "Aucun Utilisateur avec l'adresse email <<"+email+">> n'a ete trouver dans la base de donnée",
                                ErrorCodes.UTILISATEUR_NOT_FOUND
                        )
                );
        //setCreatedByUser(userDto);
        return userDto;
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID Utilisateur  à supprimer est null",
                "Impossible de supprimer un frais de Mission  avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);
        utilisateurRepository.deleteById(id);
    }

    @Override
    public PageResponse<UtilisateurDto> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Utilisateur> utilisateurs= utilisateurRepository.findAllUsers(pageable);
        List<UtilisateurDto> utilisateurDtos = utilisateurs.stream()
                .map(UtilisateurDto::fromEntity)
                .toList();

        return new PageResponse<>(
                utilisateurDtos,
                utilisateurs.getNumber(),
                utilisateurs.getSize(),
                utilisateurs.getTotalElements(),
                utilisateurs.getTotalPages(),
                utilisateurs.isFirst(),
                utilisateurs.isLast()
        );
    }

    private void checkDto(UtilisateurDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = UtilisateurValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }

 /*   private UtilisateurDto getCreatedBy(Integer userid) {
        Utilisateur utilisateur = utilisateurRepository.findById(1).get();
        return UtilisateurDto.fromEntity(utilisateur);
    }
    private void setCreatedByUser(UtilisateurDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }
    private UtilisateurDto updatedResponse(Utilisateur user) {
        UtilisateurDto userDto=UtilisateurDto.fromEntity(user);
        setCreatedByUser(userDto);
        return userDto;
    }*/
}
