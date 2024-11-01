package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.EtapeDto;
import com.hitecart.deliverysystem.dto.EntrepriseDto;
import com.hitecart.deliverysystem.dto.EtapeDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Etape;
import com.hitecart.deliverysystem.repository.EtapeRepository;
import com.hitecart.deliverysystem.services.EntrepriseService;
import com.hitecart.deliverysystem.services.EtapeService;
import com.hitecart.deliverysystem.validator.EtapeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EtapeServiceImpl implements EtapeService {

    private final EtapeRepository etapeRepository;

    public EtapeServiceImpl(EtapeRepository etapeRepository) {
        this.etapeRepository = etapeRepository;
    }

    @Override
    public EtapeDto save(EtapeDto dto) {
        checkDto(dto,"Conssommable n'est pas valide",
                "le Etape n'est pas valide", ErrorCodes.ETAPE_NOT_VALID_OPERATION);
        return EtapeDto.fromEntity(etapeRepository.
                save(EtapeDto.toEntity(dto)));
    }

    @Override
    public EtapeDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Etape est null",
                "Impossible de trouver une etape avec un ID null",
                ErrorCodes.ETAPE_NOT_VALID_OPERATION);
        Optional<Etape> etapeOp = etapeRepository.findById(id);

        return Optional.of(EtapeDto.fromEntity(etapeOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune Etape avec l'id :"
                                + id +"n'a ete trouvé dans la base deonnée",ErrorCodes.ETAPE_NOT_FOUND)
                );
    }

    @Override
    public List<EtapeDto> findByIdMission(Integer idMission) {
        CheckParmasUtil.checkID(idMission,
                "ERREUR: Le parametre Id mision est null",
                "Impossible de trouver la liste des etapes par misson avec id misson null",
                ErrorCodes.ETAPE_NOT_VALID_OPERATION);

        List<Etape> etapes = etapeRepository.findByMissionId(idMission);

        return etapes.stream()
                .map(EtapeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtapeDto> findAll() {
        return etapeRepository.findAll().stream()
                .map(EtapeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID Etape  à supprimer est null",
                "Impossible de supprimer un frais de Mission  avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);
        etapeRepository.deleteById(id);
    }

    private void checkDto(EtapeDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = EtapeValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }

}
