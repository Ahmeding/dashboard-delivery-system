package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.CreatedByUserDto;
import com.hitecart.deliverysystem.dto.basic.UtilisateurDto;
import com.hitecart.deliverysystem.dto.basic.VehiculeDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.dao.ReparationRepository;
import com.hitecart.deliverysystem.dao.VehiculeRepository;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.services.VehiculeService;
import com.hitecart.deliverysystem.validator.VehiculeValidator;
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
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final ReparationRepository reparationRepository;
    private final UtilisateurService utilisateurServiceImpl;


    @Override
    public VehiculeDto save(VehiculeDto dto) {
        checkDto(dto,"Vehicule n'est pas valide",
                "Le Vehicule n'est pas valide", ErrorCodes.VEHICULE_NOT_VALID_OPERATION);
        return VehiculeDto.fromEntity(vehiculeRepository.
                save(VehiculeDto.toEntity(dto)));
    }

    @Override
    public VehiculeDto findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Vehicule est null",
                "Impossible de trouver une vehicule avec un ID null",
                ErrorCodes.VEHICULE_NOT_VALID_OPERATION);
        Optional<Vehicule> vehiculeOp = vehiculeRepository.findById(id);

        VehiculeDto vehiculeDto = Optional.of(VehiculeDto.fromEntity(vehiculeOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune Vehicule avec l'id :"
                                + id +"n'a ete trouvé dans la base deonnée",ErrorCodes.VEHICULE_NOT_FOUND)
                );
        setCreatedByUser(vehiculeDto);
        return vehiculeDto;
    }

    @Override
    public VehiculeDto findByImmatriculation(String immatriculation) {
        CheckParmasUtil.checkStringParma(immatriculation,
                "ERREUR: Le parametre Immatriculation est null",
                "Impossible de trouver un vehicule avec Immatriculation null",
                ErrorCodes.VEHICULE_NOT_VALID_OPERATION);

        Optional<Vehicule> vehiculeOp = vehiculeRepository.findByImmatriculation(immatriculation);

        VehiculeDto vehiculeDto = Optional.of(VehiculeDto.fromEntity(vehiculeOp.get()))
                .orElseThrow(
                        ()-> new EntityNotFoundException(
                                "Aucun Vehicule avec l'adresse email <<"+immatriculation+">> n'a ete trouver dans la base de donnée",
                                ErrorCodes.VEHICULE_NOT_FOUND
                        )
                );
        setCreatedByUser(vehiculeDto);
        return vehiculeDto;
    }

    @Override
    public List<VehiculeDto> findAll() {
        return vehiculeRepository.findAll().stream()
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer idVehicule) {


        CheckParmasUtil.checkID(idVehicule,"l'ID Vehicule  à supprimer est null",
                "Impossible de supprimer un vehicule avec un ID null",ErrorCodes.VEHICULE_NOT_VALID_OPERATION);

        List<Reparation> reparations = reparationRepository.findAllByVehiculeId(idVehicule);
        if(!reparations.isEmpty()){
            throw  new InvalidOperationException(
                    "Impossible de supprimer un vehicule qui a deja une liste des reparations",
                    ErrorCodes.VEHICULE_NOT_VALID_OPERATION);
        }
        vehiculeRepository.deleteById(idVehicule);
    }

    @Override
    public VehiculeDto updateIsActive(Integer id, Boolean isActive) {
        CheckParmasUtil.checkID(id,"l'ID Vehicule  à supprimer est null",
                "Impossible de supprimer un vehicule avec un ID null",ErrorCodes.VEHICULE_NOT_VALID_OPERATION);

        Vehicule vehiculeOp = vehiculeRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Aucune Vehicule avec l'id :"
                + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.VEHICULE_NOT_FOUND)
        );
           vehiculeOp.setIsActive(isActive);
        return VehiculeDto.fromEntity(vehiculeRepository.save(vehiculeOp));
    }

    @Override
    public PageResponse<VehiculeDto> findAllVehicles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Vehicule> vehicules= vehiculeRepository.findAllVehicles(pageable);
        List<VehiculeDto> vehiculeDtos = vehicules.stream()
                .map(this::updatedResponse)
                .toList();

        return new PageResponse<>(
                vehiculeDtos,
                vehicules.getNumber(),
                vehicules.getSize(),
                vehicules.getTotalElements(),
                vehicules.getTotalPages(),
                vehicules.isFirst(),
                vehicules.isLast()
        );
    }

    private void checkDto(VehiculeDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = VehiculeValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }


    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }
    private void setCreatedByUser(VehiculeDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }
    private VehiculeDto updatedResponse(Vehicule car) {
        VehiculeDto carDto=VehiculeDto.fromEntity(car);
        setCreatedByUser(carDto);
        return carDto;
    }
}

