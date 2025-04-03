package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.UtilisateurDto;

import java.util.List;

public interface UtilisateurService {
    UtilisateurDto save(UtilisateurDto dto);
    UtilisateurDto findById(Integer id);
    UtilisateurDto findByEmail(String email);
    List<UtilisateurDto> findAll();
    void delete(Integer id);

    PageResponse<UtilisateurDto> findAllUsers(int page, int size);
}
