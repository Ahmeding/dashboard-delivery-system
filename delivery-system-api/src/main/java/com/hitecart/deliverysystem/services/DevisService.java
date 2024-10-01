package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.DevisDto;

import java.util.List;

public interface DevisService {
    DevisDto save(DevisDto dto);
    DevisDto findById(Integer id);
    DevisDto findByCode(String code);
    List<DevisDto> findAll();
    void delete(Integer id);
}
