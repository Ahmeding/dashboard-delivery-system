package com.hitecart.deliverysystem.controllers.vehicle;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.RepairApi;
import com.hitecart.deliverysystem.dto.repair.RepairRequest;
import com.hitecart.deliverysystem.dto.repair.RepairResponse;
import com.hitecart.deliverysystem.services.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;


@RestController
public class RepairController implements RepairApi {

    private final RepairService repairService;

    @Autowired
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @Override
    public ResponseEntity<RepairResponse> saveRepair(RepairRequest repairRequest) {
        return ResponseEntity.ok(repairService.save(repairRequest));
    }

    @Override
    public ResponseEntity<RepairResponse> updateRepairInfo(Integer repairId ,RepairRequest repairRequest) {
        return ResponseEntity.ok(repairService.updateRepairInfo(repairId,repairRequest));
    }

    @Override
    public ResponseEntity<RepairResponse> findRepairById(Integer id) {
        return ResponseEntity.ok(repairService.findById(id));
    }

    @Override
    public ResponseEntity<List<RepairResponse>> findRepairByDate(Instant dateRep) {
        return ResponseEntity.ok(repairService.findByDate(dateRep));
    }

    @Override
    public ResponseEntity<List<RepairResponse>> findAllRepair() {
        return ResponseEntity.ok(repairService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteRepair(Integer id) {
        try {
            repairService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Repair deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Repair not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<RepairResponse>> findAllRepairsPageable(int page, int size) {
        return ResponseEntity.ok(repairService.findAllRepairs(page,size));
    }
}
