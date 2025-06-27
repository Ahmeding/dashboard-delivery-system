package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.company.CompanyRequest;
import com.hitecart.deliverysystem.dto.company.CompanyResponse;

import java.util.List;

public interface CompanyService {
    CompanyResponse save(CompanyRequest dto);
    CompanyResponse updateCompanyInfo(Integer companyId,CompanyRequest dto);
    CompanyResponse findById(Integer id);
    CompanyResponse findByName(String raisonSocial);
    List<CompanyResponse> findAll();
    void delete(Integer id);

    PageResponse<CompanyResponse> findAllEntreprises(int page, int size);
}
