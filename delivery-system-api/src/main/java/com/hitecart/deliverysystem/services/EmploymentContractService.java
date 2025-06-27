package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractResponse;

import java.util.List;

public interface EmploymentContractService {

    EmploymentContractResponse save(EmploymentContractRequest dto);
    EmploymentContractResponse updateEmploymentContractInfo(Integer contractId, EmploymentContractRequest employmentContractRequest);
    EmploymentContractResponse findById(Integer id);
    List<EmploymentContractResponse> findAllByIdChauffeur(Integer idChauffeur);
    List<EmploymentContractResponse> findAll();
    void delete(Integer id);

    PageResponse<EmploymentContractResponse> findAllContracts(int page, int size);

}
