package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.PieceRechangeDto;
import com.hitecart.deliverysystem.dto.PieceRechangeDto;
import com.hitecart.deliverysystem.dto.ReparationDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.PieceRechange;
import com.hitecart.deliverysystem.repository.PieceRechangeRepository;
import com.hitecart.deliverysystem.services.PieceRechangeService;
import com.hitecart.deliverysystem.services.ReparationService;
import com.hitecart.deliverysystem.utils.Constants;
import com.hitecart.deliverysystem.validator.PieceRechangeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.utils.Constants.*;

@Service
@Slf4j
public class PieceRechangeServiceImpl implements PieceRechangeService {

    private final PieceRechangeRepository pieceRechangeRepository;
    private final ReparationService reparationService;

    public PieceRechangeServiceImpl(PieceRechangeRepository pieceRechangeRepository, ReparationService reparationService) {
        this.pieceRechangeRepository = pieceRechangeRepository;
        this.reparationService = reparationService;
    }

    @Override
    public PieceRechangeDto save(PieceRechangeDto dto) {
        checkDto(dto,"Piece de Rechange n'est pas valide",
                "le PieceRechange n'est pas valide", ErrorCodes.PIECERECHANGE_NOT_VALID_OPERATION);
        ReparationDto reparationDto =  reparationService.findById(dto.getReparationDto().getId());
        reparationService.updateCoutReparation(ADD_FLAG, reparationDto,dto);
        return PieceRechangeDto.fromEntity(pieceRechangeRepository.
                save(PieceRechangeDto.toEntity(dto)));
    }



    @Override
    public PieceRechangeDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID PieceRechange est null",
                "Impossible de trouver une pieceRechange avec un ID null",
                ErrorCodes.PIECERECHANGE_NOT_VALID_OPERATION);
        Optional<PieceRechange> pieceRechangeOp = pieceRechangeRepository.findById(id);

        return Optional.of(PieceRechangeDto.fromEntity(pieceRechangeOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune PieceRechange avec l'id :"
                                + id +"n'a ete trouvé dans la base deonnée",ErrorCodes.PIECERECHANGE_NOT_FOUND)
                );
    }

    @Override
    public PieceRechangeDto findByReference(String ref) {

        CheckParmasUtil.checkStringParma(
                ref,
                "Le refrence de la piece de rechane est null",
                "Impossible de trouver une piece de rechange avec une refrence null",
                ErrorCodes.PIECERECHANGE_NOT_VALID_OPERATION
        );
        Optional<PieceRechange> pieceRechangeOp = pieceRechangeRepository.findByReference(ref);
        return Optional.of(PieceRechangeDto.fromEntity(pieceRechangeOp.get()))
                .orElseThrow(()->new EntityNotFoundException("Aucune piece de rechange avec la reference : "
                +ref+" n'a ete trouvé dans la base de données",ErrorCodes.PIECERECHANGE_NOT_FOUND));
    }


    @Override
    public List<PieceRechangeDto> findAll() {
        return pieceRechangeRepository.findAll().stream()
                .map(PieceRechangeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID PieceRechange à supprimer est null",
                "Impossible de supprimer une piece de rechange  avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);
        Optional<PieceRechange> pieceRechange = pieceRechangeRepository.findById(id);
        ReparationDto reparationDto = ReparationDto.fromEntity(pieceRechange.get().getReparation());

        reparationService.updateCoutReparation( DELETE_FLAG,reparationDto,PieceRechangeDto.fromEntity(pieceRechange.get()));
        pieceRechangeRepository.deleteById(id);
    }

    private void checkDto(PieceRechangeDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = PieceRechangeValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }
}
