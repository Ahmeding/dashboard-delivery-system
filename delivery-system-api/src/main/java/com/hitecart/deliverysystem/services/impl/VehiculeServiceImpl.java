package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.VehiculeDto;
import com.hitecart.deliverysystem.dto.VehiculeDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.PieceRechange;
import com.hitecart.deliverysystem.model.Reparation;
import com.hitecart.deliverysystem.model.Vehicule;
import com.hitecart.deliverysystem.repository.ReparationRepository;
import com.hitecart.deliverysystem.repository.VehiculeRepository;
import com.hitecart.deliverysystem.services.VehiculeService;
import com.hitecart.deliverysystem.validator.VehiculeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final ReparationRepository reparationRepository;

    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, ReparationRepository reparationRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.reparationRepository = reparationRepository;
    }

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

        return Optional.of(VehiculeDto.fromEntity(vehiculeOp.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException("Aucune Vehicule avec l'id :"
                                + id +"n'a ete trouvé dans la base deonnée",ErrorCodes.VEHICULE_NOT_FOUND)
                );
    }

    @Override
    public VehiculeDto findByImmatriculation(String immatriculation) {
        CheckParmasUtil.checkStringParma(immatriculation,
                "ERREUR: Le parametre Immatriculation est null",
                "Impossible de trouver un vehicule avec Immatriculation null",
                ErrorCodes.VEHICULE_NOT_VALID_OPERATION);

        Optional<Vehicule> vehiculeOp = vehiculeRepository.findByImmatriculation(immatriculation);

        return Optional.of(VehiculeDto.fromEntity(vehiculeOp.get()))
                .orElseThrow(
                        ()-> new EntityNotFoundException(
                                "Aucun Vehicule avec l'adresse email <<"+immatriculation+">> n'a ete trouver dans la base de donnée",
                                ErrorCodes.VEHICULE_NOT_FOUND
                        )
                );
    }

    @Override
    public List<VehiculeDto> findAll() {
        return vehiculeRepository.findAll().stream()
                .map(VehiculeDto::fromEntity)
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
        System.out.println("test....calls.......");
        vehiculeRepository.deleteById(idVehicule);
    }

    private void checkDto(VehiculeDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = VehiculeValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }
}

