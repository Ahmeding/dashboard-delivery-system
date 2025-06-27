package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.DriverRepository;
import com.hitecart.deliverysystem.dao.EmploymentContractRepository;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.EmploymentContractMapper;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.EmploymentContract;
import com.hitecart.deliverysystem.services.EmploymentContractService;
import com.hitecart.deliverysystem.services.UserService;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.EmploymentContractValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class EmploymentContractServiceImpl implements EmploymentContractService {
    
    private final EmploymentContractRepository employmentContractRepository;
    private final EmploymentContractMapper employmentContractMapper;
    private final UserService userServiceImpl;
    private final DriverRepository driverRepository;

    @Override
    public EmploymentContractResponse save(EmploymentContractRequest request) {
        /*
         * todo check if driver
         *  chechk if driver has already a contract
         *  chechk how ma time rmaing for current constract
         *
         */
        checkDto(
                request,
                "EmploymentContract n'est pas valid",
                "Le contrat n'est pa valide ",
                ErrorCodes.CONTRACT_NOT_VALID);
        return savingEmploymentContract(request);

    }

    /**
     *
     * @param request
     * @return
     */
    private EmploymentContractResponse savingEmploymentContract(EmploymentContractRequest request) {
        Driver driver = driverRepository.findById(request.driverId()).orElseThrow(
                ()-> new EntityNotFoundException("Driver with ID :: " + request.driverId()+"Not Found ")
        );
        EmploymentContract employmentContract = employmentContractMapper.toEntity(request);
        employmentContract.setDriver(driver);

        return employmentContractMapper.toResponse(employmentContractRepository.save(employmentContract));
    }

    /**
     *
     * @param request
     * @return EmploymentContractResponse
     */
    @Override
    public EmploymentContractResponse updateEmploymentContractInfo(Integer contractId, EmploymentContractRequest request) {

        if(contractId == null || contractId == 0){
            throw  new InvalidOperationException("contract id is not valid", ErrorCodes.CONTRACT_NOT_VALID_OPERATION);
        }
        var existingContract = employmentContractRepository.findById(request.id()).orElseThrow(
                ()-> new EntityNotFoundException("EmploymentContract with ID ::"+ request.id()+"Not Found ")
        );

        employmentContractMapper .updateEntityFromRequest(request,existingContract);

        EmploymentContract updatedContract = employmentContractRepository.save(existingContract);

        return employmentContractMapper.toResponse(updatedContract);
    }

    @Override
    public EmploymentContractResponse findById(Integer id) {

        CheckParmasUtil.checkID(
                id,
                "ID contrat est null",
                "Immpossible de trouver un contrat avec un id null",
                ErrorCodes.CONTRACT_NOT_VALID_OPERATION);

        EmploymentContract contractById = employmentContractRepository.findById(id)   .orElseThrow(
                ()->new EntityNotFoundException(
                        "Aucun EmploymentContract avec L'ID "+id+" n'a ete trouvé dans la base des données",
                        ErrorCodes.CONTRACT_NOT_FOUND)
        );

        return employmentContractMapper.toResponse(contractById);
    }

    @Override
    public List<EmploymentContractResponse> findAllByIdChauffeur(Integer idChauffeur) {
        CheckParmasUtil.checkID(idChauffeur,
                "ERREUR: Le parametre Id driver est null",
                "Impossible de trouver la liste des contrat par chauffaur avec Id driver null",
                ErrorCodes.CONTRACT_NOT_VALID_OPERATION);
        List<EmploymentContract> employmentContracts = employmentContractRepository.findAllByDriverId(idChauffeur);
        return employmentContracts.stream()
                //.map(EmploymentContractFullDto::fromEntity)
                .map(employmentContractMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<EmploymentContractResponse> findAll() {
        return employmentContractRepository.findAll().stream()
                //.map(EmploymentContractFullDto::fromEntity)
                .map(employmentContractMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID du contrat à supprimer est null",
                "Impossible de supprimer un contrat avec un ID null",
                ErrorCodes.CLIENT_NOT_VALID_OPERATION);
        employmentContractRepository.deleteById(id);
    }

    @Override
    public PageResponse<EmploymentContractResponse> findAllContracts(int page, int size) {
        Pageable pageable
                = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<EmploymentContract> contrats = employmentContractRepository.findAllContracts(pageable);
        List<EmploymentContractResponse> employmentContractResponses = contrats.stream()
                //.map(EmploymentContractFullDto::fromEntity)
                .map(employmentContractMapper::toResponse)
                .toList();
        return new PageResponse<>(
                employmentContractResponses,
                contrats.getNumber(),
                contrats.getSize(),
                contrats.getTotalElements(),
                contrats.getTotalPages(),
                contrats.isFirst(),
                contrats.isLast()
        );
    }

    private void checkDto(EmploymentContractRequest request, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = EmploymentContractValidator.validate(request);
        if(!errors.isEmpty()){
            log.error(msgLog,request);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }



}
