package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.RepairRepository;
import com.hitecart.deliverysystem.dao.VehicleRepository;
import com.hitecart.deliverysystem.dto.vehicle.VehicleRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.VehicleMapper;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.services.UserService;
import com.hitecart.deliverysystem.services.VehicleService;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.VehicleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.utils.CapitalizeFirstLetter.*;
import static com.hitecart.deliverysystem.utils.CheckDuplicat.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final RepairRepository repairRepository;
    private final UserService userServiceImpl;
    private final VehicleMapper vehicleMapper;


    @Override
    public VehicleResponse save(VehicleRequest vehicleRequest) {
        checkDto(vehicleRequest, "Vehicle not valid",
                "Vehicle is not  valid", ErrorCodes.VEHICLE_NOT_VALID);
        return savingVehicle(vehicleRequest);
    }

    /**
     *
     * @param vehicleRequest
     * @return VehicleResponse
     */
    private VehicleResponse savingVehicle(VehicleRequest vehicleRequest) {
        checkVehicle(vehicleRequest, vehicleRepository);
        Vehicle newVehicle = vehicleMapper.toEntity(vehicleRequest);
        newVehicle.setBrand(capitalizeFirstLetter(vehicleRequest.brand()));
        newVehicle.setModel(capitalizeFirstLetter(vehicleRequest.model()));
        newVehicle.setRegistrationNbr(vehicleRequest.registrationNbr().toUpperCase());
        newVehicle.setVinNbr(vehicleRequest.vinNbr().toLowerCase());

        return vehicleMapper.toResponse(vehicleRepository.save(newVehicle));
    }

    @Override
    public VehicleResponse updateVehicleInfo(Integer vehicleId, VehicleRequest vehicleRequest) {
        /*checkDto(vehicleRequest, "Vehicle not valid",
                "Vehicle is not  valid", ErrorCodes.VEHICLE_NOT_VALID);*/

       var existingVehicle= vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new EntityNotFoundException("Vehicle with :: ID : " + vehicleId + "Not Found", ErrorCodes.VEHICLE_NOT_FOUND)
        );

       vehicleMapper.updateVehicleFromRequest(vehicleRequest, existingVehicle);
        if(StringUtils.hasLength(vehicleRequest.brand())){
           existingVehicle.setBrand(capitalizeFirstLetter(vehicleRequest.brand()));
       }
        if(StringUtils.hasLength(vehicleRequest.model())){
           existingVehicle.setModel(capitalizeFirstLetter(vehicleRequest.model()));
       }

        if(StringUtils.hasLength(vehicleRequest.registrationNbr())){
           existingVehicle.setRegistrationNbr(capitalizeFirstLetter(vehicleRequest.registrationNbr()));
       }

        if(StringUtils.hasLength(vehicleRequest.registrationNbr())){
           existingVehicle.setRegistrationNbr(vehicleRequest.registrationNbr().toUpperCase());
       }

        if(StringUtils.hasLength(vehicleRequest.vinNbr())){
           existingVehicle.setVinNbr(vehicleRequest.vinNbr().toLowerCase());
       }

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);

        return vehicleMapper.toResponse(updatedVehicle);
    }

    private void checkVinNbrExists(VehicleRequest vehicleRequest) {
        Boolean isExists = vehicleRepository.existsByVinNbr(vehicleRequest.vinNbr());
        if (isExists) {
            throw new InvalidOperationException(
                    "Vin Nbr is already in use: " + vehicleRequest.vinNbr(),
                    ErrorCodes.VEHICLE_NOT_VALID_OPERATION
            );
        }
    }

    private void checkRegisterNbrExists(VehicleRequest vehicleRequest) {
        Boolean isExists = vehicleRepository.existsByRegistrationNbr(vehicleRequest.registrationNbr());
        if (isExists) {
            throw new InvalidOperationException(
                    "Registration Nbr is already in use: " + vehicleRequest.registrationNbr(),
                    ErrorCodes.VEHICLE_NOT_VALID_OPERATION
            );
        }
    }

    @Override
    public VehicleResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Vehicle est null",
                "Impossible de trouver une vehicle avec un ID null",
                ErrorCodes.VEHICLE_NOT_VALID_OPERATION);
        Vehicle vehicleById = vehicleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aucune Vehicle avec l'id :"
                        + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.VEHICLE_NOT_FOUND)
        );
        return vehicleMapper.toResponse(vehicleById);
    }

    @Override
    public VehicleResponse findByRegistrationNbr(String registrationNbr) {
        CheckParmasUtil.checkStringParma(registrationNbr,
                "ERREUR: Le parametre Immatriculation est null",
                "Impossible de trouver un vehicle avec Immatriculation null",
                ErrorCodes.VEHICLE_NOT_VALID_OPERATION);

        Vehicle vehicleByRegistrationNbr = vehicleRepository.findByRegistrationNbr(registrationNbr).orElseThrow(
                () -> new EntityNotFoundException(
                        "Vehicle with Registration Nbr <<" + registrationNbr + ">> n'a ete trouver dans la base de donnée",
                        ErrorCodes.VEHICLE_NOT_FOUND
                )
        );
        return vehicleMapper.toResponse(vehicleByRegistrationNbr);
    }

    @Override
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer vehicleId) {

        CheckParmasUtil.checkID(vehicleId, "l'ID Vehicle  à supprimer est null",
                "Impossible de supprimer un vehicle avec un ID null", ErrorCodes.VEHICLE_NOT_VALID_OPERATION);

        List<Repair> repairs = repairRepository.findAllByVehicleId(vehicleId);
        if (!repairs.isEmpty()) {
            throw new InvalidOperationException(
                    "Impossible de supprimer un vehicle qui a deja une liste des repairs",
                    ErrorCodes.VEHICLE_NOT_VALID_OPERATION);
        }
        vehicleRepository.deleteById(vehicleId);
    }

    @Override
    public VehicleResponse updateIsActive(Integer id, Boolean isActive) {
        CheckParmasUtil.checkID(id, "l'ID Vehicle  à supprimer est null",
                "Impossible de supprimer un vehicle avec un ID null", ErrorCodes.VEHICLE_NOT_VALID_OPERATION);

        Vehicle vehicleToUpdate = vehicleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aucune Vehicle avec l'id :"
                        + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.VEHICLE_NOT_FOUND)
        );
        vehicleToUpdate.setIsActive(isActive);

        return vehicleMapper.toResponse(vehicleRepository.save(vehicleToUpdate));
    }

    @Override
    public PageResponse<VehicleResponse> findAllVehicles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Vehicle> vehicules = vehicleRepository.findAllVehicles(pageable);
        List<VehicleResponse> vehiculeDtos = vehicules.stream()
                .map(vehicleMapper::toResponse)
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

    private void checkDto(VehicleRequest vehicleRequest, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = VehicleValidator.validate(vehicleRequest);
        if (!errors.isEmpty()) {
            log.error(msgLog, vehicleRequest);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }
}

