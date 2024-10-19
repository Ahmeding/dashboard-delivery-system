package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.PieceRechangeDto;

import java.util.List;

public interface PieceRechangeService {
    PieceRechangeDto save(PieceRechangeDto dto);
    PieceRechangeDto findById(Integer id);
    PieceRechangeDto findByReference(String ref);
    List<PieceRechangeDto> findAll();
    void delete(Integer id);

    PageResponse<PieceRechangeDto> findAllPiecesRechanges(int page, int size);
}
