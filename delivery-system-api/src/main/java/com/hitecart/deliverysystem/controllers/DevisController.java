package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.DevisApi;
import com.hitecart.deliverysystem.dto.DevisDto;
import com.hitecart.deliverysystem.dto.DevisDtoResponse;
import com.hitecart.deliverysystem.services.DevisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DevisController implements DevisApi {

    private final DevisService devisService;

    @Override
    public ResponseEntity<DevisDto> save(DevisDto dto) {
        return ResponseEntity.ok(devisService.save(dto));
    }

    @Override
    public ResponseEntity<DevisDtoResponse> findById(Integer id) {
        return ResponseEntity.ok(devisService.findById(id));
    }

    @Override
    public ResponseEntity<DevisDtoResponse> findByCode(String code) {
        return ResponseEntity.ok(devisService.findByCode(code));
    }

    @Override
    public ResponseEntity<List<DevisDtoResponse>> findAll() {
        return ResponseEntity.ok(devisService.findAll());
    }

    @Override
    public void delete(Integer id) {
            devisService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<DevisDtoResponse>> findAllDevis(int page, int size) {
        return ResponseEntity.ok(devisService.findAllDevis(page,size));
    }
}
