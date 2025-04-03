package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.CommandeClientDto;
import com.hitecart.deliverysystem.dto.basic.CommandeClientResponse;

import java.util.List;

public interface CommandeClientService {
    CommandeClientDto save(CommandeClientDto dto);
    CommandeClientResponse findById(Integer id);
    CommandeClientResponse findByCode(String email);
    List<CommandeClientResponse> findAll();
    void delete(Integer id);

    PageResponse<CommandeClientResponse> findAllCmdClient(int page, int size);
}
