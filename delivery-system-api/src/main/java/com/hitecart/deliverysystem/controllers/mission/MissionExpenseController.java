package com.hitecart.deliverysystem.controllers.mission;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.MissionExpenseApi;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseResponse;
import com.hitecart.deliverysystem.services.MissionExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MissionExpenseController implements MissionExpenseApi {

    private final MissionExpenseService missionExpenseService;

    @Override
    public ResponseEntity<MissionExpenseResponse> saveMissionExpense(MissionExpenseRequest missionExpenseRequest) {
        return ResponseEntity.ok( missionExpenseService.save(missionExpenseRequest));
    }

    @Override
    public ResponseEntity<MissionExpenseResponse> updateMissionExpenseInfo(Integer expenseId, MissionExpenseRequest missionExpenseRequest) {
        return ResponseEntity.ok(missionExpenseService.updateMissionExpenseInfo(expenseId, missionExpenseRequest));
    }

    @Override
    public ResponseEntity<MissionExpenseResponse> updateMissionExpenseQuantity(Integer idMF, BigDecimal qte) {
        return ResponseEntity.ok(missionExpenseService.updateQuantite(idMF,qte));
    }

   @Override
    public ResponseEntity<MissionExpenseResponse> findMissionExpenseById(Integer id) {
        return ResponseEntity.ok(missionExpenseService.findById(id));
    }

    @Override
    public ResponseEntity<MissionExpenseResponse> findMissionExpenseByIdMission(Integer idMission) {
        return ResponseEntity.ok(missionExpenseService.findByMissionId(idMission));
    }



    @Override
    public ResponseEntity<List<MissionExpenseResponse>> findAllMissionExpense() {
        return ResponseEntity.ok(missionExpenseService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteMissionExpense(Integer id) {
        try {
            missionExpenseService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Mission Expense deleted successfully"
                    ));
        }
        catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Mission Expense not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<MissionExpenseResponse>> findAllExpenseMissionsPageable(int page, int size) {
        return ResponseEntity.ok(missionExpenseService.findAllFraisMission(page,size));
    }
}
