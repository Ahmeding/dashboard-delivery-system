package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.PieceRechangeDto;
import com.hitecart.deliverysystem.dto.ReparationDto;

import java.time.Instant;
import java.util.List;

public interface ReparationService {
    ReparationDto save(ReparationDto dto);
    ReparationDto findById(Integer id);
    List<ReparationDto> findByDate(Instant dateRep);
    List<ReparationDto> findAll();

    void updateCoutReparation(String flag, ReparationDto dto, PieceRechangeDto pieceRechangeDto);
      void delete(Integer id);

    PageResponse<ReparationDto> findAllRepairs(int page, int size);
}
