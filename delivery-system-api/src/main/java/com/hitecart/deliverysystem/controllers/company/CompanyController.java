package com.hitecart.deliverysystem.controllers.company;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.CompanyApi;
import com.hitecart.deliverysystem.dto.company.CompanyRequest;
import com.hitecart.deliverysystem.dto.company.CompanyResponse;
import com.hitecart.deliverysystem.services.CompanyService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CompanyController implements CompanyApi {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public ResponseEntity<CompanyResponse> saveCompany(CompanyRequest companyRequest) {
        return ResponseEntity.ok(companyService.save(companyRequest));
    }

    @Override
    public ResponseEntity<CompanyResponse> updateCompanyInfo(Integer companyId,CompanyRequest companyRequest) {
        return ResponseEntity.ok(companyService.updateCompanyInfo(companyId,companyRequest));
    }

    @Override
    public ResponseEntity<CompanyResponse> findCompanyById(Integer id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @Override
    public ResponseEntity<CompanyResponse> findCompanyByName(String name) {
        return ResponseEntity.ok(companyService.findByName(name));
    }

    @Override
    public ResponseEntity<List<CompanyResponse>> findAllCompany() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteCompany(Integer id) {
        try {
            companyService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Company deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Company not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<CompanyResponse>> findAllCompaniesPageable(int page, int size) {
        return ResponseEntity.ok(companyService.findAllEntreprises(page,size));
    }
}
