package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.client.ClientRequest;
import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.client.ClientUpdateRequest;

import java.util.List;

public interface ClientService {
    ClientResponse save(ClientRequest request);
    ClientResponse updateClientInfo(Integer clientId,ClientUpdateRequest request);
    ClientResponse findById(Integer id);
    ClientResponse findByEmail(String email);
    List<ClientResponse> findAll();
    void delete(Integer id);

    ClientResponse updateIsActive(Integer id, Boolean isActive);

    PageResponse<ClientResponse> findAllClients(int page, int size);
}
