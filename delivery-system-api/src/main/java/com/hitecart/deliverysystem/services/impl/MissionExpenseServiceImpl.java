package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.MissionRepository;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.MissionExpenseMapper;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.MissionExpense;
import com.hitecart.deliverysystem.dao.MissionExpensesRepository;
import com.hitecart.deliverysystem.services.MissionExpenseService;
import com.hitecart.deliverysystem.services.MissionService;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.MissionExpenseValidator;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class MissionExpenseServiceImpl implements MissionExpenseService {

    private final MissionService missionService;
    private final MissionExpensesRepository missionExpensesRepository;
    private final MissionExpenseMapper missionExpenseMapper;
    private final MissionRepository missionRepository;

    @Override
    public MissionExpenseResponse save(MissionExpenseRequest missionExpenseRequest) {
        checkRequestValidating(missionExpenseRequest,"Conssommable n'est pas valide",
                "le MissionExpense n'est pas valide",ErrorCodes.EXPENSE_NOT_VALID_OPERATION);
        return savingMissionExpense(missionExpenseRequest);
    }

    private MissionExpenseResponse savingMissionExpense(MissionExpenseRequest missionExpenseRequest) {

      Mission currentMission = missionRepository.findById(missionExpenseRequest.missionId()).orElseThrow(
              ()-> new EntityNotFoundException("Mission not found", ErrorCodes.MISSION_NOT_FOUND)
      );
       MissionExpense missionExpenseToSave = missionExpenseMapper.toEntity(missionExpenseRequest);
       checkExpenseDate(missionExpenseToSave,currentMission);
       checkDuplication(missionExpenseToSave);
        MissionExpense savedMissionExpense = missionExpensesRepository.save(missionExpenseToSave);
        return  missionExpenseMapper.toResponse(savedMissionExpense);
    }

    private void checkExpenseDate(MissionExpense missionExpense, Mission currentMission) {
        if(missionExpense.getExpenseDate().isAfter(currentMission.getEndDate())){
            throw new InvalidOperationException("Date not valid",ErrorCodes.EXPENSE_NOT_VALID_OPERATION);
        }
    }
    private void checkDuplication(MissionExpense missionExpense) {
        Integer excludeId =
                missionExpense.getId() != null ?
                        missionExpense.getId() :
                        -1;
        missionExpensesRepository.findByNameByDateByQuantityByCost(
                missionExpense.getName(),
                missionExpense.getExpenseDate(),
                missionExpense.getQuantity(),
                missionExpense.getCost(),
                excludeId).ifPresent(existing -> {
            throw new InvalidOperationException(
                    String.format(
                            "Expenses Mission already exists for name [%s] with overlapping dates (%s) . Conflicting expense mission ID: %d",
                            missionExpense.getName(),
                            missionExpense.getExpenseDate(),
                            existing.getId()
                    ),
                    ErrorCodes.MISSION_NOT_VALID_OPERATION
            );
        });
    }

    @Override
    public MissionExpenseResponse updateMissionExpenseInfo(Integer expenseId, MissionExpenseRequest missionExpenseRequest) {

        if(expenseId == null || expenseId == 0){
            throw  new InvalidOperationException("Expense ID is Null",ErrorCodes.EXPENSE_NOT_VALID_OPERATION);
        }
        Mission currentMission = missionRepository.findById(missionExpenseRequest.missionId()).orElseThrow(
                ()-> new EntityNotFoundException("Mission not found", ErrorCodes.MISSION_NOT_FOUND)
        );
       var existingExpense = missionExpensesRepository.findById(expenseId).orElseThrow(
                ()-> new EntityNotFoundException("Mission Expensen with ::ID : " + expenseId + " not found")
        );

        missionExpenseMapper.updateMissionExpenseFromRequest(missionExpenseRequest, existingExpense);
        checkExpenseDate(existingExpense, currentMission);
        checkDuplication(existingExpense);
        MissionExpense updatedMissionExpense = missionExpensesRepository.save(existingExpense);


        return missionExpenseMapper.toResponse(updatedMissionExpense);
    }


    @Override
    public MissionExpenseResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID MissionExpense est null",
                "Impossible de trouver un un frais de mission avec un ID null",
                ErrorCodes.EXPENSE_NOT_VALID_OPERATION);
        MissionExpense fraisMissionById = missionExpensesRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Aucune MissionExpense avec l'id :"
                        + id +"n'a ete trouvé dans la base de donnée",ErrorCodes.EXPENSE_NOT_FOUND)
        );
        return  missionExpenseMapper.toResponse(fraisMissionById);
    }

    @Override
    public MissionExpenseResponse findByMissionId(Integer idMission) {
        CheckParmasUtil.checkID(idMission,
                "ERREUR: Le parametre Id mision est null",
                "Impossible de trouver la liste de missionExpense par misson avec id misson null",
                ErrorCodes.EXPENSE_NOT_VALID_OPERATION);

        MissionExpense missionExpenseByMissionId = missionExpensesRepository.findByMissionId(idMission).orElseThrow(
                ()->new EntityNotFoundException("la mission avec l'id :"
                        + idMission +"n'a pas de frais de mission",ErrorCodes.EXPENSE_NOT_FOUND)
        );
        return missionExpenseMapper.toResponse(missionExpenseByMissionId);
    }



    @Override
    public MissionExpenseResponse updateQuantite(Integer idFM, BigDecimal qte) {
        CheckParmasUtil.checkID(idFM,"l'ID MissionExpense est null",
                "Impossible de modifier un frais de Mission  avec un ID null",ErrorCodes.EXPENSE_NOT_VALID_OPERATION);

        if(qte ==null || qte.compareTo(BigDecimal.ZERO)==0){
            log.error("La qte  dans la ligne est NULL ou inferier à zero ");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCodes.EXPENSE_NOT_VALID_OPERATION);
        }
        MissionExpenseResponse missionExpenseResponse = checkFraisMission(idFM);
        missionExpenseResponse.setQuantity(qte);


        missionExpensesRepository.save(missionExpenseMapper.toEntity(missionExpenseResponse));

        return missionExpenseResponse;
    }

    private MissionExpenseResponse checkFraisMission(Integer idFM) {
        MissionExpenseResponse missionDto = findById(idFM);
        if(missionDto == null){
            throw new EntityNotFoundException("Aucun frais de mission avec l'ID "+idFM+
                    "n'a ete trouve dans la BDD",ErrorCodes.EXPENSE_NOT_FOUND);
        }
        return missionDto;
    }

    @Override
    public List<MissionExpenseResponse> findAll() {
        return missionExpensesRepository.findAll().stream()
                .map(missionExpenseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID MissionExpense  à supprimer est null",
                "Impossible de supprimer un frais de Mission  avec un ID null",
                ErrorCodes.EXPENSE_NOT_VALID_OPERATION);
       missionExpensesRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Aucun frais de mission avec l'ID "+id+
                        "n'a ete trouve dans la BDD",ErrorCodes.EXPENSE_NOT_FOUND)
        );
           missionExpensesRepository.deleteById(id);
    }

    @Override
    public PageResponse<MissionExpenseResponse> findAllFraisMission(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<MissionExpense> fraisMissions = missionExpensesRepository.findAllMissionExpenses(pageable);

        List<MissionExpenseResponse> fraisMissionsDto = fraisMissions.stream()
                .map(missionExpenseMapper::toResponse)
                .toList();

        return new PageResponse<>(
                fraisMissionsDto,
                fraisMissions.getNumber(),
                fraisMissions.getSize(),
                fraisMissions.getTotalElements(),
                fraisMissions.getTotalPages(),
                fraisMissions.isFirst(),
                fraisMissions.isLast()
        );
    }

    private void checkRequestValidating(MissionExpenseRequest request, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = MissionExpenseValidator.validate(request);
        if(!errors.isEmpty()){
            log.error(msgLog,request);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }



}
