package com.hitecart.deliverysystem.controllers.vehicle;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.SparePartApi;
import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import com.hitecart.deliverysystem.dto.sparepart.SparePartResponse;
import com.hitecart.deliverysystem.services.SparePartService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SparePartController implements SparePartApi {

    private final SparePartService sparePartService;

    @Override
    public ResponseEntity<SparePartResponse> saveSparePart(SparePartRequest sparePartRequest) {
        return ResponseEntity.ok(sparePartService.save(sparePartRequest));
    }

    @Override
    public ResponseEntity<SparePartResponse> updateSparePartInfo(Integer sparePartId, SparePartRequest request) {
        return ResponseEntity.ok(sparePartService.updateSparePartInfo(sparePartId,request));
    }

    @Override
    public ResponseEntity<SparePartResponse> findSparePartById(Integer id) {
        return ResponseEntity.ok(sparePartService.findById(id));
    }

    @Override
    public ResponseEntity<SparePartResponse> findSparePartByReference(String ref) {
        return ResponseEntity.ok(sparePartService.findByReference(ref));
    }

    @Override
    public ResponseEntity<List<SparePartResponse>> findAllSparePart() {
        return ResponseEntity.ok(sparePartService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteSparePart(Integer id) {
        try {
            sparePartService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "SparePart deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "SparePart not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<SparePartResponse>> findAllSparePartsPageable(int page, int size) {
        return ResponseEntity.ok(sparePartService.findAllSpartParts(page,size));
    }
}
