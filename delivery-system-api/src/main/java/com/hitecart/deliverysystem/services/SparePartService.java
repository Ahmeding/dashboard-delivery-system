package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import com.hitecart.deliverysystem.dto.sparepart.SparePartResponse;

import java.util.List;

public interface SparePartService {
    SparePartResponse save(SparePartRequest request);

    SparePartResponse updateSparePartInfo(Integer stopPointId ,SparePartRequest request);

    SparePartResponse findById(Integer id);

    SparePartResponse findByReference(String ref);

    List<SparePartResponse> findAll();

    void delete(Integer id);

    PageResponse<SparePartResponse> findAllSpartParts(int page, int size);
}
