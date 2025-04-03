package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.DevisDto;
import com.hitecart.deliverysystem.dto.basic.DevisDtoResponse;

import java.util.List;

public interface DevisService {
    DevisDto save(DevisDto dto);
    DevisDtoResponse findById(Integer id);
    DevisDtoResponse findByCode(String code);
    List<DevisDtoResponse> findAll();
    void delete(Integer id);

    PageResponse<DevisDtoResponse> findAllDevis(int page, int size);
}
