package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.UtilisateurDto;
import com.hitecart.deliverysystem.dto.UtilisateurDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.repository.UtilisateurRepository;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.validator.UtilisateurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {


    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
        checkDto(dto,"Utilisateur n'est pas valide",
                "l'Utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID_OPERATION);
        return UtilisateurDto.fromEntity(utilisateurRepository.
                save(UtilisateurDto.toEntity(dto)));
    }

    @Override
    public UtilisateurDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Utilisateur est null",
                "Impossible de trouver une utilisateur avec un ID null",
                ErrorCodes.UTILISATEUR_NOT_VALID_OPERATION);
        Optional<Utilisateur> utilisateurOp = utilisateurRepository.findById(id);

        return Optional.of(UtilisateurDto.fromEntity(utilisateurOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune Utilisateur avec l'id :"
                                + id +"n'a ete trouvé dans la base deonnée",ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        CheckParmasUtil.checkStringParma(email,
                "ERREUR: Le parametre email est null",
                "Impossible de trouver un utilisateur avec email null",
                ErrorCodes.UTILISATEUR_NOT_VALID_OPERATION);

        Optional<Utilisateur> utilisateurOp = utilisateurRepository.findByEmail(email);

        return Optional.of(UtilisateurDto.fromEntity(utilisateurOp.get()))
                .orElseThrow(
                        ()-> new EntityNotFoundException(
                                "Aucun Utilisateur avec l'adresse email <<"+email+">> n'a ete trouver dans la base de donnée",
                                ErrorCodes.UTILISATEUR_NOT_FOUND
                        )
                );
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

    private void checkDto(UtilisateurDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = UtilisateurValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }

}
