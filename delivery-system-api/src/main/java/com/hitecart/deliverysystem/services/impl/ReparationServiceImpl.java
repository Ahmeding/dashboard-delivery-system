package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.dto.ReparationDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.PieceRechange;
import com.hitecart.deliverysystem.model.Reparation;
import com.hitecart.deliverysystem.repository.PieceRechangeRepository;
import com.hitecart.deliverysystem.repository.ReparationRepository;
import com.hitecart.deliverysystem.services.*;
import com.hitecart.deliverysystem.validator.ReparationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReparationServiceImpl implements ReparationService {


    private final ReparationRepository reparationRepository;

    private final VehiculeService vehiculeService;
    private final PieceRechangeRepository pieceRechangeRepository;
    private final UtilisateurService utilisateurServiceImpl;

    @Override
    public ReparationDto save(ReparationDto reparationDto) {
        checkDto(reparationDto,"Reparation n'est pas valide",
                "La Reparation n'est pas valide", ErrorCodes.REPARATION_NOT_VALID_OPERATION);

        VehiculeDto vehiculeDto = vehiculeService.findById(reparationDto.getVehiculeDto().getId());
        reparationDto.setVehiculeDto(vehiculeDto);


        return ReparationDto.fromEntity(reparationRepository.
                save(ReparationDto.toEntity(reparationDto)));
    }
    @Override
    public void updateCoutReparation(String flag,ReparationDto reparationDto, PieceRechangeDto pieceRechangeDto) {
        checkDto(reparationDto,"Reparation n'est pas valide",
                "La Reparation n'est pas valide", ErrorCodes.REPARATION_NOT_VALID_OPERATION);

        if(flag.equals(DELETE_FLAG)){
        reparationDto.setCout(reparationDto.getCout().subtract(pieceRechangeDto.getPrix()));
        }if(flag.equals(ADD_FLAG)){
        reparationDto.setCout(reparationDto.getCout().add(pieceRechangeDto.getPrix()));
        }

        reparationRepository.save(ReparationDto.toEntity(reparationDto));
    }


    @Override
    public ReparationDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Reparation est null",
                "Impossible de trouver une reparation avec un ID null",
                ErrorCodes.REPARATION_NOT_VALID_OPERATION);
        Optional<Reparation> reparationOp = reparationRepository.findById(id);

        ReparationDto reparationDto = Optional.of(ReparationDto.fromEntity(reparationOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune Reparation avec l'id :"
                                + id +"n'a ete trouvé dans la base deonnée",ErrorCodes.REPARATION_NOT_FOUND)
                );

        setCreatedByUser(reparationDto);
        return reparationDto;
    }

    @Override
    public List<ReparationDto> findByDate(Instant dateRep) {
        CheckParmasUtil.checkDate(dateRep,
                "ERREUR: Le parametre date de repartion est null",
                "Impossible de trouver la liste des reparations par date de repartion avec date de reparation null",
                ErrorCodes.REPARATION_NOT_VALID_OPERATION);

        List<Reparation> reparations = reparationRepository.findByDateDebut(dateRep);

        return reparations.stream()
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReparationDto> findAll() {
        return reparationRepository.findAll().stream()
                //.map(ReparationDto::fromEntity)
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }



    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID Reparation  à supprimer est null",
                "Impossible de supprimer un frais de Mission  avec un ID null",ErrorCodes.COMMANDECLIENT_NOT_VALID_OPERATION);


         List<PieceRechange> pieceRechanges = pieceRechangeRepository.findAllByReparationId(id);
         if(!pieceRechanges.isEmpty()){
             throw  new InvalidOperationException(
                     "Impossible de supprimer une reparation qui a deja des pieces de rechange",
                     ErrorCodes.REPARATION_NOT_VALID_OPERATION);
         }
        reparationRepository.deleteById(id);
    }

    @Override
    public PageResponse<ReparationDto> findAllRepairs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reparation> reparations= reparationRepository.findAllRepairs(pageable);
        List<ReparationDto> reparationDtos = reparations.stream()
               // .map(ReparationDto::fromEntity)
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                reparationDtos,
                reparations.getNumber(),
                reparations.getSize(),
                reparations.getTotalElements(),
                reparations.getTotalPages(),
                reparations.isFirst(),
                reparations.isLast()
        );
    }

    private void checkDto(ReparationDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = ReparationValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }

    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(ReparationDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }
    private ReparationDto updatedResponse(Reparation reparation) {
        ReparationDto reparationDto=ReparationDto.fromEntity(reparation);
        setCreatedByUser(reparationDto);
        return reparationDto;
    }

}
