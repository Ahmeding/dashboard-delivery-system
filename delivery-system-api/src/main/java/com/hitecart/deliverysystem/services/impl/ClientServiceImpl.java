package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.dto.client.ClientRequest;
import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.client.ClientUpdateRequest;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.ClientMapper;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.RoleType;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.utils.*;
import com.hitecart.deliverysystem.validator.ClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.*;
import com.hitecart.deliverysystem.dto.client.ClientRequest;
import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.client.ClientUpdateRequest;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.user.UserUpdateRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.ClientMapper;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.model.enums.RoleType;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.utils.CapitalizeFirstLetter;
import com.hitecart.deliverysystem.utils.CheckDuplicat;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.ClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for client management operations.
 * <p>
 * Handles business logic for client lifecycle including creation, retrieval,
 * update, and deletion. Ensures data integrity through validation and
 * business rule enforcement.
 * </p>
 *
 * @author Hitecart-TN Dev Team
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final QuoteRepository quoteRepository;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryPointRepository deliveryPointRepository;

    /**
     * Creates a new client based on the provided request.
     *
     * @param request Client creation request data
     * @return Response containing created client details
     * @throws InvalidEntityException If validation fails
     * @throws InvalidOperationException If duplicate client/user is detected
     */
    @Override
    public ClientResponse save(ClientRequest request) {
        checkRequestValidation(request, "Client not valid", "Client validation failed");
        return this.savingClient(request);
    }

    /**
     * Updates an existing client's information.
     *
     * @param clientId ID of the client to update
     * @param clientUpdateRequest Data containing updates
     * @return Response containing updated client details
     * @throws EntityNotFoundException If client not found
     * @throws InvalidOperationException If email conflict or invalid role
     */
    @Override
    public ClientResponse updateClientInfo(Integer clientId, ClientUpdateRequest clientUpdateRequest) {

        checkRequestValidation(clientUpdateRequest, "Client not valid", "Client validation failed");

        var existingClient = clientRepository.findById(clientId).orElseThrow(
                () -> new EntityNotFoundException("Client with ID " + clientId + " not found.", ErrorCodes.CLIENT_NOT_FOUND)
        );

        // Map update fields to existing client
        clientMapper.updateClientFromRequest(clientUpdateRequest, existingClient);

        // Apply capitalization to names if updated
        if (StringUtils.hasText(clientUpdateRequest.firstname())) {
            existingClient.setFirstname(CapitalizeFirstLetter.capitalizeFirstLetter(existingClient.getFirstname()));
        }
        if (StringUtils.hasText(clientUpdateRequest.lastname())) {
            existingClient.setLastname(CapitalizeFirstLetter.capitalizeFirstLetter(existingClient.getLastname()));
        }

        // Validate and update email if changed
        if (StringUtils.hasText(clientUpdateRequest.email())) {
            String newEmail = clientUpdateRequest.email().toLowerCase();
            if (!newEmail.equals(existingClient.getEmail())) {
                userRepository.findByEmail(newEmail).ifPresent(u -> {
                    if (!u.getId().equals(existingClient.getId())) {
                        throw new InvalidOperationException("Email already in use: " + newEmail, ErrorCodes.USER_NOT_VALID_OPERATION);
                    }
                });
                existingClient.setEmail(newEmail);
            }
        }

        // Update roles if provided
        updateRole(clientUpdateRequest, existingClient);
        checkExists(existingClient);

        Client updatedClient = userRepository.save(existingClient);
        return clientMapper.toClientResponse(updatedClient);
    }


    /**
     * Updates client roles based on request data.
     *
     * @param clientUpdateRequest Update request containing roles
     * @param existingClient Client entity to update
     * @throws EntityNotFoundException If any role is not found
     */
    private void updateRole(ClientUpdateRequest clientUpdateRequest, Client existingClient) {
        if (clientUpdateRequest.roles() != null) {
            List<Role> managedRoles = clientUpdateRequest.roles().stream()
                    .map(roleRequest -> {
                        if (roleRequest.name() == null) {
                            throw new IllegalArgumentException("Role name cannot be null");
                        }
                        return roleRepository.findByName(roleRequest.name())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Role not found: " + roleRequest.name(),
                                        ErrorCodes.ROLE_NOT_FOUND
                                ));
                    })
                    .collect(Collectors.toList());
            existingClient.setRoles(managedRoles);
        }
    }

    /**
     * Handles client creation logic.
     *
     * @param request Client creation request
     * @return Response with created client
     */
    private ClientResponse savingClient(ClientRequest request) {
        Client clientToSave = clientMapper.toClient(request);

        // Check for duplicates
        CheckDuplicat.checkDuplicatClient(request, clientRepository);
        CheckDuplicat.checkDuplicatUser(clientToSave, userRepository);

        // Assign roles
        if (request.roles() != null && !request.roles().isEmpty()) {
            List<Role> managedRoles = new ArrayList<>();
            for (RoleRequest roleReq : request.roles()) {
                RoleType roleTypeToFind = roleReq.name();
                Role managedRole = roleRepository.findByName(roleTypeToFind)
                        .orElseThrow(() -> new InvalidOperationException(
                                "Role " + roleTypeToFind + " not found",
                                ErrorCodes.ROLE_NOT_FOUND
                        ));
                managedRoles.add(managedRole);
            }
            clientToSave.setRoles(managedRoles);
        } else {
            clientToSave.setRoles(Collections.emptyList());
        }
        checkExists(clientToSave);
        // Format and secure data
        clientToSave.setFirstname(CapitalizeFirstLetter.capitalizeFirstLetter(clientToSave.getFirstname()));
        clientToSave.setLastname(CapitalizeFirstLetter.capitalizeFirstLetter(clientToSave.getLastname()));
       if(!EmailValidator.isValidEmail(clientToSave.getEmail().toLowerCase()))
        clientToSave.setEmail(clientToSave.getEmail().toLowerCase());
        clientToSave.setPassword(passwordEncoder.encode(clientToSave.getPassword()));

        Client savedClient = clientRepository.save(clientToSave);
        return clientMapper.toClientResponse(savedClient);
    }

    private void checkExists(Client clientToSave) {
        Integer excludeId=
                clientToSave.getId() != null?
                   clientToSave.getId():
                   -1;

        clientRepository.findByEmail(clientToSave.getEmail(),excludeId).ifPresent(
                existingClient -> {
                    throw new InvalidOperationException("Client already exists");
                }
        );

    }

    /**
     * Retrieves a client by ID.
     *
     * @param id Client identifier
     * @return Client response data
     * @throws EntityNotFoundException If client not found
     * @throws IllegalArgumentException If ID is invalid
     */
    @Override
    public ClientResponse findById(Integer id) {
        CheckParmasUtil.checkID(id, "Client ID parameter is null",
                "Cannot find client with null ID", ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        Client clientById = clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Client not found with ID: " + id,
                        ErrorCodes.CLIENT_NOT_FOUND
                )
        );
        return clientMapper.toClientResponse(clientById);
    }

    /**
     * Retrieves a client by email address.
     *
     * @param email Client's email address
     * @return Client response data
     * @throws EntityNotFoundException If client not found
     * @throws IllegalArgumentException If email is invalid
     */
    @Override
    public ClientResponse findByEmail(String email) {
        CheckParmasUtil.checkStringParma(email, "Email parameter is invalid",
                "Cannot search with empty email", ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        Client clientByEmail = clientRepository.findClientByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        "Client not found with email: " + email,
                        ErrorCodes.CLIENT_NOT_FOUND
                )
        );
        return clientMapper.toClientResponse(clientByEmail);
    }

    /**
     * Retrieves all clients.
     *
     * @return List of client responses
     */
    @Override
    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toClientResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a client by ID if no dependencies exist.
     *
     * @param id Client identifier
     * @throws EntityNotFoundException If client not found
     * @throws InvalidOperationException If client has orders or quotes
     * @throws IllegalArgumentException If ID is invalid
     */
    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "Invalid client ID",
                "Cannot delete with null ID", ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        this.findById(id); // Verify existence

        List<DeliveryPoint> deliveryPoints =  deliveryPointRepository.findAllByClientId(id);
        // Check for orders
        List<Order> orders = orderRepository.findAllByClientId(id);
        if (!orders.isEmpty()) {
            throw new InvalidOperationException(
                    "Cannot delete client with existing orders",
                    ErrorCodes.CLIENT_NOT_VALID_OPERATION
            );
        }

        // Check for quotes
        List<Quote> quotes = quoteRepository.findAllByClientId(id);
        if (!quotes.isEmpty()) {
            throw new InvalidOperationException(
                    "Cannot delete client with existing quotes",
                    ErrorCodes.CLIENT_NOT_VALID_OPERATION
            );
        }

        clientRepository.deleteById(id);
    }

    /**
     * Updates client's activation status.
     *
     * @param id Client identifier
     * @param enabled New activation status
     * @return Updated client response
     * @throws EntityNotFoundException If client not found
     * @throws IllegalArgumentException If ID is invalid
     */
    @Override
    public ClientResponse updateIsActive(Integer id, Boolean enabled) {
        CheckParmasUtil.checkID(id, "Client ID parameter is null",
                "Cannot update status with null ID", ErrorCodes.CLIENT_NOT_VALID_OPERATION);

        Client client = clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Client not found with ID: " + id,
                        ErrorCodes.CLIENT_NOT_FOUND
                )
        );
        client.setEnabled(enabled);
        return clientMapper.toClientResponse(clientRepository.save(client));
    }

    /**
     * Retrieves paginated list of clients.
     *
     * @param page Page number (0-indexed)
     * @param size Number of items per page
     * @return Paginated response of clients
     */
    @Override
    public PageResponse<ClientResponse> findAllClients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Client> clients = clientRepository.findAllClients(pageable);

        List<ClientResponse> clientsDto = clients.stream()
                .map(clientMapper::toClientResponse)
                .toList();

        return new PageResponse<>(
                clientsDto,
                clients.getNumber(),
                clients.getSize(),
                clients.getTotalElements(),
                clients.getTotalPages(),
                clients.isFirst(),
                clients.isLast()
        );
    }

    /**
     * Validates client request data.
     *
     * @param request Client request to validate
     * @param msgLog Log message for validation failure
     * @param msgException Exception message for validation failure
     * @throws InvalidEntityException If validation fails
     */
    private void checkRequestValidation(ClientRequest request, String msgLog, String msgException) {
        List<String> errors = ClientValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error(msgLog, request);
            throw new InvalidEntityException(msgException, ErrorCodes.CLIENT_NOT_VALID, errors);
        }
    }

    private void checkRequestValidation(ClientUpdateRequest request, String msgLog, String msgException) {
        List<String> errors = ClientValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error(msgLog, request);
            throw new InvalidEntityException(msgException, ErrorCodes.CLIENT_NOT_VALID, errors);
        }
    }

}
