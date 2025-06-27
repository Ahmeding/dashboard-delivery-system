package com.hitecart.deliverysystem.controllers.client;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.ClientApi;
import com.hitecart.deliverysystem.dto.client.ClientRequest;
import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.client.ClientUpdateRequest;
import com.hitecart.deliverysystem.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller for client management operations.
 * <p>
 * Provides endpoints for creating, retrieving, updating, and deleting client profiles.
 * Implements the ClientApi interface for standardized OpenAPI documentation.
 * </p>
 *
 * @author Hitecart-TN Dev Team
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
public class ClientController implements ClientApi {

    private final ClientService clientService;

    /**
     * Creates a new client profile.
     *
     * @param clientRequest Client creation data
     * @return ResponseEntity containing created client details (HTTP 200)
     */
    @Override
    public ResponseEntity<ClientResponse> saveClient(ClientRequest clientRequest) {
        return ResponseEntity.ok(clientService.save(clientRequest));
    }

    /**
     * Updates an existing client's information.
     *
     * @param clientId ID of the client to update
     * @param request Data containing client updates
     * @return ResponseEntity with updated client details (HTTP 200)
     */
    @Override
    public ResponseEntity<ClientResponse> updateClientInfo(Integer clientId, ClientUpdateRequest request) {
        return ResponseEntity.ok(clientService.updateClientInfo(clientId, request));
    }

    /**
     * Retrieves a client by their unique identifier.
     *
     * @param id Client ID
     * @return ResponseEntity containing client details (HTTP 200)
     */
    @Override
    public ResponseEntity<ClientResponse> findClientById(Integer id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    /**
     * Retrieves a client by their email address.
     *
     * @param email Client's email address
     * @return ResponseEntity containing client details (HTTP 200)
     */
    @Override
    public ResponseEntity<ClientResponse> findClientByEmail(String email) {
        return ResponseEntity.ok(clientService.findByEmail(email));
    }

    /**
     * Retrieves all clients in the system.
     *
     * @return ResponseEntity containing list of all clients (HTTP 200)
     */
    @Override
    public ResponseEntity<List<ClientResponse>> findAllClient() {
        return ResponseEntity.ok(clientService.findAll());
    }

    /**
     * Updates a client's activation status.
     *
     * @param id Client ID to update
     * @param isActive New activation status (true = active, false = inactive)
     * @return ResponseEntity with updated client details (HTTP 200)
     */
    @Override
    public ResponseEntity<ClientResponse> updateClientIsActive(Integer id, Boolean isActive) {
        return ResponseEntity.ok(clientService.updateIsActive(id, isActive));
    }

    /**
     * Deletes a client profile if no dependencies exist.
     *
     * @param id Client ID to delete
     * @return Empty response with HTTP 204 status upon successful deletion
     */
    @Override
    public  ResponseEntity<?> deleteClient(Integer id) {
        try {
            clientService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Client deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Client not found with ID: " + id
                    ));
        }
    }

    /**
     * Retrieves paginated list of clients.
     *
     * @param page Page number (0-indexed)
     * @param size Number of items per page
     * @return ResponseEntity containing paginated client response (HTTP 200)
     */
    @Override
    public ResponseEntity<PageResponse<ClientResponse>> findAllClientsPageable(int page, int size) {
        return ResponseEntity.ok(clientService.findAllClients(page, size));
    }
}
