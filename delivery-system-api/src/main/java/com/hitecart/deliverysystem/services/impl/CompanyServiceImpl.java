package com.hitecart.deliverysystem.services.impl;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.company.CompanyRequest;
import com.hitecart.deliverysystem.dto.company.CompanyResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.CompanyMapper;
import com.hitecart.deliverysystem.model.Company;
import com.hitecart.deliverysystem.dao.CompanyRepository;
import com.hitecart.deliverysystem.model.DeliveryDestination;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.services.CompanyService;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.CompanyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;


    @Override
    public CompanyResponse save(CompanyRequest request) {

        checkDto(
                request,
                "Company n'est pas valid",
                "L'entreprise n'est pa valide ",
                ErrorCodes.COMPANY_NOT_VALID_OPERATION);
        if(!companyRepository.findAll().isEmpty()){
            throw new InvalidOperationException("You have already a company", ErrorCodes.COMPANY_NOT_VALID_OPERATION);
        }
        return savingCompany(request);
    }

    /**
     * @param request
     * @return
     */
    private CompanyResponse savingCompany(CompanyRequest request) {

        Company newCompany =companyMapper.toCompany(request);

        Company formattingCompany = formatingCampany(newCompany);
        checkExists(formattingCompany);

        return companyMapper.toCompanyResponse(this.companyRepository.save(formattingCompany));
    }

    private void checkExists(Company company) {
        Integer excludeId =
                company.getId() != null ?
                        company.getId() :
                        -1;
        companyRepository.findCompanyByName(company.getName(),excludeId)
                .ifPresent(existing->{
                    throw new InvalidOperationException("Company already exist " +excludeId,
                            ErrorCodes.COMPANY_NOT_VALID_OPERATION);
                });
    }

    private Company formatingCampany(Company newCompany) {
        return Company.builder()
                .name(capitalizeCompanyName(newCompany.getName()))
                .email(newCompany.getEmail().trim().toLowerCase())
                .taxNbr(validateTaxNumber(newCompany.getTaxNbr()))
                .phoneNbr(validatePhoneNbr(newCompany.getPhoneNbr()))
                .address(newCompany.getAddress())
                .build();
    }

    private String validatePhoneNbr(String phoneNbr) {
        if(phoneNbr == null || phoneNbr.length() != 12){
            throw  new InvalidOperationException("phone number not vlaid");
        }

        return phoneNbr;
    }

    @Override
    public CompanyResponse updateCompanyInfo(Integer companyId, CompanyRequest request) {

        if (companyId == null || companyId <= 0) {
            throw new InvalidOperationException("Company ID Not valid", ErrorCodes.COMPANY_NOT_VALID_OPERATION);
        }
        Company existingCompany = companyRepository.findById(companyId).orElseThrow(
                () -> new EntityNotFoundException("Company with id " + companyId + " not found.")
        );

        companyMapper.updateCompanyFromRequest(request, existingCompany);
        existingCompany.setName(capitalizeCompanyName(existingCompany.getName()));
        existingCompany.setEmail(existingCompany.getEmail().trim().toLowerCase());
        existingCompany .setTaxNbr(validateTaxNumber(existingCompany.getTaxNbr()));
        existingCompany.setPhoneNbr(validatePhoneNbr(existingCompany.getPhoneNbr()));
        checkExists(existingCompany);
        Company updatedCompany = companyRepository.save(existingCompany);

        return companyMapper.toCompanyResponse(updatedCompany);
    }

    @Override
    public CompanyResponse findById(Integer id) {

        CheckParmasUtil.checkID(
                id,
                "ID entreprise est null",
                "Immpossible de trouver un entreprise avec un id null",
                ErrorCodes.COMPANY_NOT_VALID);

        Company entrepriseOp = companyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Aucun Company avec L'ID " + id + "n'a ete trouvé dans la base des données",
                        ErrorCodes.COMPANY_NOT_FOUND)
        );

        return companyMapper.toCompanyResponse(entrepriseOp);

    }

    @Override
    public CompanyResponse findByName(String raisonSocial) {
        CheckParmasUtil.checkStringParma(raisonSocial,
                "ERREUR: Le parametre nom est null",
                "Impossible de trouver une entreprise avec nom entreprise null",
                ErrorCodes.COMPANY_NOT_VALID_OPERATION);

        Company entrepriseOp = companyRepository.findCompaniesByName(raisonSocial).orElseThrow(
                () -> new EntityNotFoundException("Aucune entreperise avec le raison socila " + raisonSocial + "n'a ete trouve dans la abse de onnées"
                        , ErrorCodes.COMPANY_NOT_FOUND)
        );
        ;
        return companyMapper.toCompanyResponse(entrepriseOp);
    }

    @Override
    public List<CompanyResponse> findAll() {

        return companyRepository.findAll().stream()
                .map(companyMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "l'ID du entreprise à supprimer est null",
                "Impossible de supprimer un entreprise avec un ID null", ErrorCodes.CLIENT_NOT_VALID_OPERATION);
        if(id == null || id <= 0) {
            throw new InvalidEntityException("::ID not valid.", ErrorCodes.DELIVERY_SERVICE_NOT_VALID_OPERATION);
        }
        this.findById(id);
        companyRepository.deleteById(id);
    }

    @Override
    public PageResponse<CompanyResponse> findAllEntreprises(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Company> companies = companyRepository.findAllCompanies(pageable);

        List<CompanyResponse> companyResponses = companies.stream()
                .map(companyMapper::toCompanyResponse)
                .toList();

        return new PageResponse<>(
                companyResponses,
                companies.getNumber(),
                companies.getSize(),
                companies.getTotalElements(),
                companies.getTotalPages(),
                companies.isFirst(),
                companies.isLast()
        );
    }

    private void checkDto(CompanyRequest request, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = CompanyValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error(msgLog, request);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }

    private String capitalizeCompanyName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidOperationException("Company name is required");
        }
        return WordUtils.capitalizeFully(name.trim());
    }

    private  String validateTaxNumber(String taxNbr) {
        if (taxNbr == null) {
            throw new InvalidOperationException("Tax number is required");
        }

        String cleanTaxNbr = taxNbr.replaceAll("[^\\d]", "");

        if (cleanTaxNbr.length() != 12) {
            throw new InvalidOperationException("Tax number must be exactly 12 digits");
        }
        return taxNbr;
    }

}




