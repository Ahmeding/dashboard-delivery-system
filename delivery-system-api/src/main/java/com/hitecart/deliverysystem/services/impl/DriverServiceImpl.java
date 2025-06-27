package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.EmploymentContractRepository;
import com.hitecart.deliverysystem.dao.UserRepository;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.dto.driver.DriverResponse;
import com.hitecart.deliverysystem.dto.driver.DriverUpdateRequest;
import com.hitecart.deliverysystem.exception.*;
import com.hitecart.deliverysystem.mapper.DriverMapper;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.ContractStatus;
import com.hitecart.deliverysystem.model.enums.ContractType;
import com.hitecart.deliverysystem.dao.DriverRepository;
import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.model.enums.RoleType;
import com.hitecart.deliverysystem.services.DriverService;
import com.hitecart.deliverysystem.utils.EmailValidator;
import com.hitecart.deliverysystem.validator.DriverValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.utils.CapitalizeFirstLetter.capitalizeFirstLetter;
import static com.hitecart.deliverysystem.utils.CheckDuplicat.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final EmploymentContractRepository contratRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserServiceImpl userServiceImpl;
    private final DriverMapper driverMapper;
    private final UserRepository userRepository;
    private final ThreadPoolTaskSchedulerBuilder threadPoolTaskSchedulerBuilder;

    @Transactional
    public void initializeMissingStats() {
        // Get all drivers (empty list instead of null)
        List<Driver> allDrivers = driverRepository.findAll();

        // Return early if no drivers exist
        if (allDrivers.isEmpty()) {
            log.info("No drivers found in the database.");
            return;
        }

        // Filter drivers without stats
        List<Driver> driversWithoutStats = allDrivers.stream()
                .filter(d -> d.getStats() == null)
                .collect(Collectors.toList());

        // Return early if no drivers need stats initialization
        if (driversWithoutStats.isEmpty()) {
            log.info("All drivers already have stats initialized.");
            return;
        }

        // Initialize stats for drivers that need it
        driversWithoutStats.forEach(driver -> {
            DriverStats stats = new DriverStats();
            stats.setDriver(driver);
            driver.setStats(stats);
        });

        // Save only if there are drivers to update
        driverRepository.saveAll(driversWithoutStats);
        log.info("Initialized stats for {} drivers.", driversWithoutStats.size());

    }

    @Transactional
    @Override
    public Integer save(DriverRequest request) {
        checkDto(request, "Driver is not valid", "le Driver is not valid");
        return savingDriver(request);

    }


    /**
     * @param request
     * @return savingDriver
     */
    private Integer savingDriver(DriverRequest request) {
        Driver driverToSave = driverMapper.toEntity(request);
        checkDuplicatDriver(request, driverRepository);
        checkDuplicatUser(driverToSave, userRepository);


        var usertRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));


        driverToSave.setFirstname(capitalizeFirstLetter(request.firstname()));
        driverToSave.setLastname(capitalizeFirstLetter(request.lastname()));
        driverToSave.setEmail(request.lastname().toLowerCase());
        driverToSave.setPassword(passwordEncoder.encode(request.password()));
        driverToSave.setRoles(List.of(usertRole));

        EmploymentContract employmentContractToSave = EmploymentContract.builder()
                .contractType(ContractType.CDD)
                .contractStatus(ContractStatus.CREATED)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(6))
                .build();

        driverToSave = driverRepository.save(driverToSave);
        employmentContractToSave.setDriver(driverToSave);
        contratRepository.save(employmentContractToSave);
        return driverToSave.getId();

    }


    @Override
    public Integer updateDriverInfo(Integer driverId,DriverUpdateRequest driverUpdateRequest) {
        //checkDto(request, "Driver is not valid", "Driver is not valid");
        var existingDriver = driverRepository.findById(driverId).orElseThrow(
                () -> new EntityNotFoundException("Driver with :: ID : " + driverId + " not found", ErrorCodes.DRIVER_NOT_VALID_OPERATION)
        );

        driverMapper.updateDriverFromRequest(driverUpdateRequest, existingDriver);
        // Capitalize first/last name if they were updated (i.e., not null in DTO)
        if (StringUtils.hasText(driverUpdateRequest.firstname())) { // checks for not null and not empty/whitespace
            existingDriver.setFirstname(capitalizeFirstLetter(existingDriver.getFirstname()));
        }
        if (StringUtils.hasText(driverUpdateRequest.lastname())) {
            existingDriver.setLastname(capitalizeFirstLetter(existingDriver.getLastname()));
        }
        if (StringUtils.hasText(driverUpdateRequest.email())) {

            String newEmail = driverUpdateRequest.email().toLowerCase();
            if(!EmailValidator.isValidEmail(newEmail)){
                throw new InvalidOperationException("Invalid email", ErrorCodes.DRIVER_NOT_VALID_OPERATION);
            }
            if (!newEmail.equals(existingDriver.getEmail())) {
                // Optional: Check if the new email is already taken by another user
                userRepository.findByEmail(newEmail).ifPresent(u -> {
                    if (!u.getId().equals(existingDriver.getId())) {
                        throw new InvalidOperationException("Email already in use: " + newEmail, ErrorCodes.USER_NOT_VALID_OPERATION);
                    }
                });
                existingDriver.setEmail(newEmail);
            }
        }
        updateRole(driverUpdateRequest, existingDriver);


        Driver updatedDriver = userRepository.save(existingDriver);
        return driverMapper.toResponse(updatedDriver).getId();


    }

    private void updateRole(DriverUpdateRequest driverUpdateRequest, Driver existingDriver) {
        if (driverUpdateRequest.roles() != null) {
            List<Role> managedRoles = driverUpdateRequest.roles().stream()
                    .map(roleRequest -> {
                        if (roleRequest.name() == null) {
                            throw new IllegalArgumentException("Role name/type cannot be null in role update request.");
                        }
                        return roleRepository.findByName(roleRequest.name()) // Assuming RoleRequest.name() gives RoleType
                                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleRequest.name(), ErrorCodes.ROLE_NOT_FOUND));
                    })
                    .collect(Collectors.toList()); // Use toSet() if you prefer Set<Role> and want to ensure uniqueness
            existingDriver.setRoles(managedRoles);
        }
    }


    @Override
    public DriverResponse findById(Integer id) {
        checkID(id, "Le parametre ID driver est null",
                "Impossible de trouver un driver avec un ID null");

        Driver driverById = driverRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Opps! Aucun driver avec l'id : "
                                + id + "n'a ete trouvé dans la base donnée",
                        ErrorCodes.DRIVER_NOT_FOUND)
        );

        return driverMapper.toResponse(driverById);
    }

    @Override
    public DriverResponse findByEmail(String email) {
        checkEmail(email, "ERREUR ! Le parametre EMAIL driver est null ",
                "Impossible de trouver un driver avec un EMAIL null");

        Driver driverByEmail = driverRepository.findDriverByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        "Opps! Aucun driver avec l'email :"
                                + email + "n'a ete trouvé dans la base deonnée",
                        ErrorCodes.DRIVER_NOT_FOUND)
        );

        return driverMapper.toResponse(driverByEmail);
    }

    @Override
    public List<DriverResponse> findAll() {
        List<Driver> drivers = driverRepository.findAll();
        List<DriverResponse> driverBasicDtos = drivers.stream().map(driverMapper::toResponse).toList();
        // return driverBasicDtos;
        return driverRepository.findAll().stream()
                .map(driver -> {
                    DriverResponse driverBasicDto = driverMapper.toResponse(driver);
                    return driverBasicDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverResponse> findAllIsActive(Boolean isActive) {
        return driverRepository.findAllByEnabled(isActive).stream()
                //.map(DriverFullDto::fromEntity)
                .map(driver -> {
                    DriverResponse driverBasicDto = driverMapper.toResponse(driver);
                    return driverBasicDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<DriverResponse> findAllDrivers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Driver> drivers = driverRepository.findAllDrivers(pageable);
        List<DriverResponse> driverBasicDtos = drivers.stream()
                //.map(DriverFullDto::fromEntity)
                .map(driver -> {
                    DriverResponse driverBasicDto = driverMapper.toResponse(driver);
                    return driverBasicDto;
                })
                .toList();
        return new PageResponse<>(
                driverBasicDtos,
                drivers.getNumber(),
                drivers.getSize(),
                drivers.getTotalElements(),
                drivers.getTotalPages(),
                drivers.isFirst(),
                drivers.isLast()
        );
    }

    @Override
    public void delete(Integer id) {
        checkID(id, "l'ID du driver à supprimer est null", "Impossible de supprimer un driver avec un ID null");

        findById(id);

        // TODO CREATE QUERY DELETE FROM MANY_TO_MANY RELATION

        List<EmploymentContract> employmentContracts = contratRepository.findAllByDriverId(id);
        if (!employmentContracts.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un chaffeur qui a deja des contrat"
                    , ErrorCodes.DRIVER_NOT_VALID_OPERATION);
        }
        driverRepository.deleteById(id);
    }

    @Override
    public DriverResponse updateIsActive(Integer id, Boolean enabled) {
        checkID(id, "l'ID du driver à supprimer est null", "Impossible de supprimer un driver avec un ID null");
        Driver driverOp = driverRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Opps! Aucun driver avec l'id :"
                        + id + "n'a ete trouvé dans la base donnée",
                        ErrorCodes.DRIVER_NOT_FOUND));
        driverOp.setEnabled(enabled);
        return driverMapper.toResponse(driverRepository.save(driverOp));
    }

    private void checkID(Integer id, String msgLog, String msgException) {
        if (id == null) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    ErrorCodes.DRIVER_NOT_VALID_OPERATION);
        }
    }

    private void checkEmail(String email, String msgLog, String msgException) {
        if (!StringUtils.hasLength(email)) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    ErrorCodes.DRIVER_NOT_VALID_OPERATION);
        }
    }


    private void checkDto(DriverRequest request, String msgLog, String msgException) {
        List<String> errors = DriverValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error(msgLog, request);
            throw new InvalidEntityException(msgException, ErrorCodes.DRIVER_NOT_VALID, errors);
        }
    }


}
