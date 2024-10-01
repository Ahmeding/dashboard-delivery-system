package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.CommandeClientDto;

import java.util.List;

public interface CommandeClientService {
    CommandeClientDto save(CommandeClientDto dto);
    CommandeClientDto findById(Integer id);
    CommandeClientDto findByCode(String email);
    List<CommandeClientDto> findAll();
    void delete(Integer id);
}
