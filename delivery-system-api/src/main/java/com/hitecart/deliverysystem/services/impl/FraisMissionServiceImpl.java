package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.FraisMissionDto;
import com.hitecart.deliverysystem.dto.LigneFraisMissionDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.FraisMission;
import com.hitecart.deliverysystem.model.LigneFraisMission;
import com.hitecart.deliverysystem.repository.FraisMissionRepository;
import com.hitecart.deliverysystem.repository.LigneFraisMissionRepository;
import com.hitecart.deliverysystem.services.FraisMissionService;
import com.hitecart.deliverysystem.validator.FraisMissionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FraisMissionServiceImpl implements FraisMissionService {

    private final FraisMissionRepository fraisMissionRepository;
    private final LigneFraisMissionRepository lignefraisMissionRepository;

    public FraisMissionServiceImpl(FraisMissionRepository fraisMissionRepository, LigneFraisMissionRepository lignefraisMissionRepository) {
        this.fraisMissionRepository = fraisMissionRepository;
        this.lignefraisMissionRepository = lignefraisMissionRepository;
    }

    @Override
    public FraisMissionDto updateFraisMission(FraisMissionDto dto) {

        return null;
    }

    @Override
    public FraisMissionDto save(FraisMissionDto dto) {
        checkDto(dto,"Conssommable n'est pas valide",
                "le FraisMission n'est pas valide",ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);

        FraisMission savedFraisMission = fraisMissionRepository.save(FraisMissionDto.toEntity(dto));


        if(dto.getLignesFraisMissionDto()!=null){
            dto.getLignesFraisMissionDto().forEach(
                    fm->{
                        LigneFraisMission lignefraisMissionToSave = LigneFraisMissionDto.toEntity(fm);
                        lignefraisMissionToSave.setFraisMission(savedFraisMission);
                        lignefraisMissionRepository.save(lignefraisMissionToSave);
                    }
            );
        }



        return FraisMissionDto.fromEntity(savedFraisMission);
    }


    @Override
    public FraisMissionDto deleteLigneFraisMission(Integer idFraisMission, Integer idLigneFraisMission) {
        CheckParmasUtil.checkID(idFraisMission,
                "Le parametre ID FraisMission est null",
                "Impossible de trouver un un frais de mission avec un ID null",
                ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);
        CheckParmasUtil.checkID(idLigneFraisMission,
                "Le parametre ID ligne du frais de mission est null",
                "Impossible de trouver une ligne du frais de mission avec un ID null",
                ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);
        FraisMissionDto fraisMissionDto =  checkFraisMission(idFraisMission);
        findLigneFraisMission(idLigneFraisMission);

        lignefraisMissionRepository.deleteById(idLigneFraisMission);

        return fraisMissionDto;
    }

    @Override
    public FraisMissionDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID FraisMission est null",
                "Impossible de trouver un un frais de mission avec un ID null",
                ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);
        Optional<FraisMission> fraisMissionOp = fraisMissionRepository.findById(id);

        return Optional.of(FraisMissionDto.fromEntity(fraisMissionOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune FraisMission avec l'id :"
                                + id +"n'a ete trouvé dans la base de donnée",ErrorCodes.FRAISMISSION_NOT_FOUND)
                );
    }

    @Override
    public FraisMissionDto findByIdMission(Integer idMission) {
        CheckParmasUtil.checkID(idMission,
                "ERREUR: Le parametre Id mision est null",
                "Impossible de trouver la liste de fraisMission par misson avec id misson null",
                ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);

        FraisMission fraisMission = fraisMissionRepository.findByMissionId(idMission);

        return Optional.of(FraisMissionDto.fromEntity( fraisMission)).orElseThrow(
                ()->new EntityNotFoundException("la mission avec l'id :"
                        + idMission +"n'a pas de frais de mission",ErrorCodes.FRAISMISSION_NOT_FOUND)
        );
    }

    @Override
    public List<LigneFraisMissionDto> findAllLignesFraisMissionByFraisMissionId(Integer idFraisMission) {
        CheckParmasUtil.checkID(idFraisMission,
                "ERREUR: Le parametre Id  frais mision est null",
                "Impossible de trouver la liste des lignes frais Mission par frais de misson avec id misson null",
                ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);




        return lignefraisMissionRepository.findAllByFraisMissionId(idFraisMission).stream()
                .map(LigneFraisMissionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public FraisMissionDto updateQuantite(Integer idFM, Integer idLFM, BigDecimal qte) {
        CheckParmasUtil.checkID(idFM,"l'ID FraisMission est null",
                "Impossible de modifier un frais de Mission  avec un ID null",ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);
        CheckParmasUtil.checkID(idLFM,"l'ID Ligne Frais de Mission est null",
                "Impossible de modifier une ligne du frais de Mission  avec un ID null",ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);

        if(qte ==null || qte.compareTo(BigDecimal.ZERO)==0){
            log.error("La qte  dans la ligne est NULL ou inferier à zero ");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);
        }
        FraisMissionDto fraisMissionDto = checkFraisMission(idFM);
        Optional <LigneFraisMission> ligneFraisMissionOp = findLigneFraisMission(idLFM);


        LigneFraisMission ligneFraisMission = ligneFraisMissionOp.get();
        ligneFraisMission.setQuantite(qte);
        lignefraisMissionRepository.save(ligneFraisMission);

        return fraisMissionDto;
    }

    private FraisMissionDto checkFraisMission(Integer idFM) {
        FraisMissionDto missionDto = findById(idFM);
        if(missionDto == null){
            throw new EntityNotFoundException("Aucun frais de mission avec l'ID "+idFM+ "n'a ete trouve dans la BDD",ErrorCodes.FRAISMISSION_NOT_FOUND);
        }
        return missionDto;
    }

    private Optional <LigneFraisMission> findLigneFraisMission(Integer idLFM) {
        CheckParmasUtil.checkID(idLFM,
                "l'ID Ligne Frais de Mission null",
                "Impossible de trouver une ligne frais de Mission  avec un ID null",ErrorCodes.LIGNE_FRAISMISSION_NOT_VALID_OPERATION);
        Optional <LigneFraisMission> ligneFraisMissionOptional= lignefraisMissionRepository.findById(idLFM);
        if(ligneFraisMissionOptional.isEmpty()){
            throw new EntityNotFoundException("Aucune ligne n'a ete trouve avec l'ID " + idLFM, ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND);
        }

        return ligneFraisMissionOptional;
    }

    @Override
    public List<FraisMissionDto> findAll() {
        return fraisMissionRepository.findAll().stream()
                .map(FraisMissionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID FraisMission  à supprimer est null",
                "Impossible de supprimer un frais de Mission  avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);

        List<LigneFraisMission> lignesFM = lignefraisMissionRepository.findAllByFraisMissionId(id);
        if(lignesFM.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un frais de mission qui a deja un ou plusieur ligne des frais",ErrorCodes.FRAISMISSION_NOT_VALID_OPERATION);
        }

        fraisMissionRepository.deleteById(id);
    }

    private void checkDto(FraisMissionDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = FraisMissionValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }
}
