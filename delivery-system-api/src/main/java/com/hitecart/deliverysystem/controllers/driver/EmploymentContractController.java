package com.hitecart.deliverysystem.controllers.driver;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.EmploymentContractApi;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractResponse;
import com.hitecart.deliverysystem.services.EmploymentContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmploymentContractController implements EmploymentContractApi {

    private final EmploymentContractService employmentContractService;

    @Override
    public ResponseEntity<EmploymentContractResponse> saveEmploymentContract(EmploymentContractRequest employmentContractRequest) {
        return ResponseEntity.ok(employmentContractService.save(employmentContractRequest));
    }

    @Override
    public ResponseEntity<EmploymentContractResponse> updateEmploymentContractInfo(Integer contractId, EmploymentContractRequest employmentContractRequest) {
        return ResponseEntity.ok(employmentContractService.updateEmploymentContractInfo(contractId,employmentContractRequest));
    }

    @Override
    public ResponseEntity<EmploymentContractResponse> findEmploymentContractById(Integer id) {
        return ResponseEntity.ok(employmentContractService.findById(id));
    }

    @Override
    public ResponseEntity<List<EmploymentContractResponse>> findAllEmploymentContractByDriverId(Integer idChauffeur) {
        return ResponseEntity.ok(employmentContractService.findAllByIdChauffeur(idChauffeur));
    }

    @Override
    public ResponseEntity<List<EmploymentContractResponse>> findAllEmploymentContract() {
        return ResponseEntity.ok(employmentContractService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteEmploymentContract(Integer id) {
        try {
            employmentContractService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "EmploymentContract deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "EmploymentContract not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<EmploymentContractResponse>> findAllEmploymentContractsPageable(int page, int size) {
        return ResponseEntity.ok(employmentContractService.findAllContracts(page,size));
    }
}
