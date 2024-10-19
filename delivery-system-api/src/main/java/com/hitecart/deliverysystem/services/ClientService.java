package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.ClientDto;

import java.util.List;

public interface ClientService {
    ClientDto save(ClientDto dto);
    ClientDto findById(Integer id);
    ClientDto findByEmail(String email);
    List<ClientDto> findAll();
    void delete(Integer id);

    ClientDto updateIsActive(Integer id, Boolean isActive);

    PageResponse<ClientDto> findAllClients(int page, int size);
}
