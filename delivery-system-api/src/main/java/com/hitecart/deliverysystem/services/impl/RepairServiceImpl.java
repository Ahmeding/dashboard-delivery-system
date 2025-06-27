package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.VehicleRepository;
import com.hitecart.deliverysystem.dto.repair.RepairRequest;
import com.hitecart.deliverysystem.dto.repair.RepairResponse;
import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import com.hitecart.deliverysystem.dto.sparepart.SparePartResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.RepairMapper;
import com.hitecart.deliverysystem.model.Repair;
import com.hitecart.deliverysystem.model.SparePart;
import com.hitecart.deliverysystem.dao.SparePartRepository;
import com.hitecart.deliverysystem.dao.RepairRepository;
import com.hitecart.deliverysystem.model.Vehicle;
import com.hitecart.deliverysystem.services.*;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.RepairValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepairServiceImpl implements RepairService {


    private final RepairRepository repairRepository;

    private final VehicleService vehicleService;
    private final SparePartRepository sparePartRepository;
    private final UserService userServiceImpl;
    private final VehicleRepository vehicleRepository;
    private final RepairMapper repairMapper;

    /**
     *
     * @param repairRequest
     * @return
     */
    @Override
    public RepairResponse save(RepairRequest repairRequest) {
        checkDto(repairRequest,"Repair not valid",
                "Repair not valid", ErrorCodes.REPAIR_NOT_VALID_OPERATION);
        return  savingRepair(repairRequest);
    }

    /**
     *
     * @param repairRequest
     * @return RepairResponse
     */
    private RepairResponse savingRepair(RepairRequest repairRequest) {
        Vehicle vehiculeToRepair = vehicleRepository.findById(repairRequest.vehicleId()).orElseThrow(
                ()-> new EntityNotFoundException("Vehicle with ::ID" + repairRequest.vehicleId() + " not found",ErrorCodes.VEHICLE_NOT_FOUND)
        );
        Repair newRepair = repairMapper.toEntity(repairRequest);
        newRepair.setVehicle(vehiculeToRepair);
        BigDecimal totalCost = getTotalCost(repairRequest.spareParts());
        newRepair.setCost(totalCost);
        newRepair =repairRepository.save(newRepair);

        List<SparePart> sparePartList = new ArrayList<>();

        if(!newRepair.getSpareParts().isEmpty()){
            Repair currentRepair = newRepair;
            newRepair.getSpareParts().forEach(sparePart -> {
                SparePart sparePt = SparePart.builder()
                        .quantity(sparePart.getQuantity())
                        .name(sparePart.getName())
                        .price(sparePart.getPrice())
                        .repair(currentRepair)
                        .build();
                sparePartList.add(sparePt);
            });

            sparePartRepository.saveAll(sparePartList);
        }

        return repairMapper.toResponse(newRepair);
    }

    /**
     *
     * @param sparePartRequests
     * @return
     */
    private BigDecimal getTotalCost(List<SparePartRequest> sparePartRequests) {
        if (sparePartRequests == null || sparePartRequests.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return sparePartRequests.stream()
                .map(request -> request.price().multiply(
                       BigDecimal.valueOf(request.quantity())
                )).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public RepairResponse updateRepairInfo(Integer repairId ,RepairRequest repairRequest) {
        if(repairId==null || repairId<=0){
            throw new InvalidOperationException("ID not valid" , ErrorCodes.REPAIR_NOT_VALID_OPERATION);
        }
        Vehicle vehicleToRepair = vehicleRepository.findById(repairRequest.vehicleId()).orElseThrow(
                ()-> new EntityNotFoundException("Vehicle with ::ID" + repairRequest.vehicleId() + " not found",ErrorCodes.VEHICLE_NOT_FOUND)
        );
        var existingRepair= repairRepository.findById(repairId).orElseThrow(
                ()-> new EntityNotFoundException("Repair with ::ID : "+repairId +" not found",ErrorCodes.REPAIR_NOT_FOUND)
        );

        repairMapper.updateRepairFromRequest(repairRequest,existingRepair);
        BigDecimal totalCost = getTotalCost(repairRequest.spareParts());
        existingRepair.setCost(totalCost);
        existingRepair.setVehicle(vehicleToRepair);
        Repair updatedRepair = repairRepository.save(existingRepair);

        return repairMapper.toResponse(updatedRepair);
    }

    /**
     * @param flag
     * @param repairId
     * @param sparePartResponse
     * @param delta
     */
    public void updateReparationCost(String flag, Integer repairId, SparePartResponse sparePartResponse, BigDecimal delta) {
       if(repairId == null){
           throw new InvalidOperationException("Repair id is null", ErrorCodes.REPAIR_NOT_VALID_OPERATION);
       }
       if(sparePartResponse == null){
           throw new InvalidOperationException("Spare Part is null", ErrorCodes.SPARPART_NOT_VALID_OPERATION);
       }
        Repair repairToUpdate = repairRepository.findById(repairId).orElseThrow(
                ()->new EntityNotFoundException("Aucune Repair avec l'id :"
                        + repairId +"n'a ete trouvé dans la base deonnée",ErrorCodes.REPAIR_NOT_FOUND)
        );

        if(flag.equals(DELETE_FLAG)){
            BigDecimal currentCost = repairToUpdate.getCost();
            repairToUpdate.setCost(currentCost.subtract(sparePartResponse.getPrice()));
        }if(flag.equals(ADD_FLAG)){
            BigDecimal currentCost= repairToUpdate.getCost();
            if(delta == null){
                log.info("delta is null");
            repairToUpdate.setCost(currentCost.add(sparePartResponse.getPrice()));
            }else{
                log.info("delta is not null",delta.longValue());
            repairToUpdate.setCost(currentCost.add(delta));
            }
        }


        repairRepository.save(repairToUpdate);
    }


    @Override
    public RepairResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Repair est null",
                "Impossible de trouver une repair avec un ID null",
                ErrorCodes.REPAIR_NOT_VALID_OPERATION);
        Repair repairById = repairRepository.findById(id) .orElseThrow(
                ()->new EntityNotFoundException("Aucune Repair avec l'id :"
                        + id +"n'a ete trouvé dans la base deonnée",ErrorCodes.REPAIR_NOT_FOUND)
        );

        return repairMapper.toResponse(repairById);
    }

    @Override
    public List<RepairResponse> findByDate(Instant dateRep) {
        CheckParmasUtil.checkDate(dateRep,
                "ERREUR: Le parametre date de repartion est null",
                "Impossible de trouver la liste des repairs par date de repartion avec date de repair null",
                ErrorCodes.REPAIR_NOT_VALID_OPERATION);

        List<Repair> repairs = repairRepository.findByStartDate(dateRep);

        return repairs.stream()
                .map(repairMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RepairResponse> findAll() {
        return repairRepository.findAll().stream()
                //.map(RepairFullDto::fromEntity)
                .map(repairMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID Repair  à supprimer est null",
                "Impossible de supprimer un frais de Mission  avec un ID null",
                ErrorCodes.REPAIR_NOT_VALID_OPERATION);

        this.findById(id);

         List<SparePart> spareParts = sparePartRepository.findAllByRepairId(id);
         if(!spareParts.isEmpty()){
             throw  new InvalidOperationException(
                     "Impossible de supprimer une repair qui a deja des pieces de rechange",
                     ErrorCodes.REPAIR_NOT_VALID_OPERATION);
         }
        repairRepository.deleteById(id);
    }

    @Override
    public PageResponse<RepairResponse> findAllRepairs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Repair> reparations= repairRepository.findAllRepairs(pageable);
        List<RepairResponse> repairBasicDtos = reparations.stream()
               // .map(RepairFullDto::fromEntity)
                .map(repairMapper::toResponse)
                .toList();

        return new PageResponse<>(
                repairBasicDtos,
                reparations.getNumber(),
                reparations.getSize(),
                reparations.getTotalElements(),
                reparations.getTotalPages(),
                reparations.isFirst(),
                reparations.isLast()
        );
    }

    private void checkDto(RepairRequest repairRequest, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = RepairValidator.validate(repairRequest);
        if(!errors.isEmpty()){
            log.error(msgLog,repairRequest);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }
}
