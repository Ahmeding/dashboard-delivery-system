package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.EntrepriseDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Entreprise;
import com.hitecart.deliverysystem.dao.EntrepriseRepository;
import com.hitecart.deliverysystem.services.EntrepriseService;
import com.hitecart.deliverysystem.validator.EntrepriseValidator;
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
public class EntrepriseServiceImpl implements EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }

    @Override
    public EntrepriseDto save(EntrepriseDto dto) {

        checkDto(
                dto,
                "Entreprise n'est pas valid",
                "L'entreprise n'est pa valide ",
                ErrorCodes.CONTRAT_NOT_VALID);
        return null;
    }

    @Override
    public EntrepriseDto findById(Integer id) {

        CheckParmasUtil.checkID(
                id,
                "ID entreprise est null",
                "Immpossible de trouver un entreprise avec un id null",
                ErrorCodes.CONTRAT_NOT_VALID_OPERATION);

        Optional<Entreprise> entrepriseOp = entrepriseRepository.findById(id);

        return Optional.of(EntrepriseDto.fromEntity(entrepriseOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Aucun Entreprise avec L'ID "+id+"n'a ete trouvé dans la base des données",
                                ErrorCodes.CONTRAT_NOT_FOUND)
                );
    }

    @Override
    public EntrepriseDto findByRaisonSocial(String raisonSocial) {
        CheckParmasUtil.checkStringParma(raisonSocial,
                "ERREUR: Le parametre nom est null",
                "Impossible de trouver une entreprise avec nom entreprise null",
                ErrorCodes.CONTRAT_NOT_VALID_OPERATION);

        Optional<Entreprise> entrepriseOp = entrepriseRepository.findEntrepriseByRaisonSocial(raisonSocial);

        return Optional.of(EntrepriseDto.fromEntity(entrepriseOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune entreperise avec le raison socila "+raisonSocial+"n'a ete trouve dans la abse de onnées"
                        ,ErrorCodes.ENTREPRISE_NOT_FOUND)
                );

    }

    @Override
    public List<EntrepriseDto> findAll() {

        return entrepriseRepository.findAll().stream()
                .map(EntrepriseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID du entreprise à supprimer est null",
                "Impossible de supprimer un entreprise avec un ID null",ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        entrepriseRepository.deleteById(id);
    }

    @Override
    public PageResponse<EntrepriseDto> findAllEntreprises(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Entreprise> entreprises = entrepriseRepository.findAllEntreprises(pageable);

        List<EntrepriseDto> entreprisesDto = entreprises.stream()
                .map(EntrepriseDto::fromEntity)
                .toList();

        return new PageResponse<>(
                entreprisesDto,
                entreprises.getNumber(),
                entreprises.getSize(),
                entreprises.getTotalElements(),
                entreprises.getTotalPages(),
                entreprises.isFirst(),
                entreprises.isLast()
        );
    }

    private void checkDto(EntrepriseDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = EntrepriseValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }
}
