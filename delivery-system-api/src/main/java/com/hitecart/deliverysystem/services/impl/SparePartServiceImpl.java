package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.RepairRepository;
import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import com.hitecart.deliverysystem.dto.sparepart.SparePartResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.mapper.SparePartMapper;
import com.hitecart.deliverysystem.model.Repair;
import com.hitecart.deliverysystem.model.SparePart;
import com.hitecart.deliverysystem.dao.SparePartRepository;
import com.hitecart.deliverysystem.services.SparePartService;
import com.hitecart.deliverysystem.services.RepairService;
import com.hitecart.deliverysystem.utils.CapitalizeFirstLetter;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.SparePartValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SparePartServiceImpl implements SparePartService {

    private final SparePartRepository sparePartRepository;
    private final RepairService repairService;
    private final RepairRepository repairRepository;
    private final SparePartMapper sparePartMapper;

    @Override
    public SparePartResponse save(SparePartRequest sparePartRequest) {
        checkDto(sparePartRequest, "Spare Part not valid",
                "Spare Part not valid", ErrorCodes.SPARPART_NOT_VALID_OPERATION);
        return savingSparePart(sparePartRequest);
    }

    private SparePartResponse savingSparePart(SparePartRequest sparePartRequest) {
        SparePart sparPart = sparePartMapper.toEntity(sparePartRequest);
        Repair currentRepair = repairRepository.findById(sparePartRequest.repairId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Repair with ID  :"
                                                          + sparePartRequest.repairId() + "Not found ", ErrorCodes.REPAIR_NOT_FOUND)
                );

        sparPart.setName(CapitalizeFirstLetter.capitalizeFirstLetter(sparePartRequest.name()));
        SparePart newSparePart = sparePartRepository.save(sparPart);
        SparePartResponse sparePartResponse = sparePartMapper.toResponse(newSparePart);
        repairService.updateReparationCost(ADD_FLAG, currentRepair.getId(), sparePartResponse, null);

        return sparePartResponse;
    }

    @Override
    public SparePartResponse updateSparePartInfo(Integer sparePartId, SparePartRequest sparePartRequest) {
        checkDto(sparePartRequest, "Spare Part not valid",
                "Spare Part not valid", ErrorCodes.SPARPART_NOT_VALID_OPERATION);
        Repair currentRepair = repairRepository.findById(sparePartRequest.repairId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Repair with ID  :"
                                                          + sparePartRequest.repairId() + "Not found ", ErrorCodes.REPAIR_NOT_FOUND)
                );
        var existingSparePart = sparePartRepository.findById(sparePartId).orElseThrow(
                () -> new EntityNotFoundException("Spare part with ID  :" + sparePartId + "Not found ", ErrorCodes.SPARPART_NOT_FOUND)
        );

        sparePartMapper.updateSparePartFromRequest(sparePartRequest, existingSparePart);

        SparePart updatedSparePart = sparePartRepository.save(existingSparePart);
        SparePartResponse sparePartResponse = sparePartMapper.toResponse(updatedSparePart);
        BigDecimal delta = sparePartResponse.getPrice().subtract(existingSparePart.getPrice());

        repairService.updateReparationCost(ADD_FLAG, currentRepair.getId(), sparePartResponse, delta);
        return sparePartResponse;
    }

    @Override
    public SparePartResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID SparePart est null",
                "Impossible de trouver une pieceRechange avec un ID null",
                ErrorCodes.SPARPART_NOT_VALID_OPERATION);
        SparePart spartPartById = sparePartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aucune SparePart avec l'id :"
                                                  + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.SPARPART_NOT_FOUND)
        );
        ;

        return sparePartMapper.toResponse(spartPartById);

    }

    @Override
    public SparePartResponse findByReference(String ref) {

        CheckParmasUtil.checkStringParma(
                ref,
                "Le refrence de la piece de rechane est null",
                "Impossible de trouver une piece de rechange avec une refrence null",
                ErrorCodes.SPARPART_NOT_VALID_OPERATION
        );
        SparePart sparPartByReference = sparePartRepository.findByReference(ref).orElseThrow(
                () -> new EntityNotFoundException("Aucune piece de rechange avec la reference : "
                                                  + ref + " n'a ete trouvé dans la base de données", ErrorCodes.SPARPART_NOT_FOUND));
        return sparePartMapper.toResponse(sparPartByReference);
    }

    @Override
    public List<SparePartResponse> findAll() {
        return sparePartRepository.findAll().stream()
                .map(sparePartMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "l'ID SparePart à supprimer est null",
                "Impossible de supprimer une piece de rechange  avec un ID null", ErrorCodes.SPARPART_NOT_VALID_OPERATION);
        SparePart sparPartToDelete = sparePartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aucune SparePart avec l'id :"
                                                  + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.SPARPART_NOT_FOUND)
        );
        Integer currentRepairId = sparPartToDelete.getRepair().getId();
        repairService.updateReparationCost(DELETE_FLAG, currentRepairId, sparePartMapper.toResponse(sparPartToDelete), null);
        sparePartRepository.deleteById(id);
    }

    @Override
    public PageResponse<SparePartResponse> findAllSpartParts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<SparePart> pieceRechanges = sparePartRepository.findAllSpareParts(pageable);

        List<SparePartResponse> pieceRechangesDto = pieceRechanges.stream()
                .map(sparePartMapper::toResponse)
                .toList();

        return new PageResponse<>(
                pieceRechangesDto,
                pieceRechanges.getNumber(),
                pieceRechanges.getSize(),
                pieceRechanges.getTotalElements(),
                pieceRechanges.getTotalPages(),
                pieceRechanges.isFirst(),
                pieceRechanges.isLast()
        );
    }

    private void checkDto(SparePartRequest sparePartRequest, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = SparePartValidator.validate(sparePartRequest);
        if (!errors.isEmpty()) {
            log.error(msgLog, sparePartRequest);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }
}
