package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.WeeklyPlanningApi;
import com.hitecart.deliverysystem.dto.WeeklyPlanningDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hitecart.deliverysystem.services.WeeklyPlanningService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WeeklyPlanningController implements WeeklyPlanningApi {

    private final WeeklyPlanningService weeklyPlanningService;


    @Override
    public ResponseEntity<WeeklyPlanningDto> save(WeeklyPlanningDto dto) {
        return ResponseEntity.ok(weeklyPlanningService.save(dto));
    }

    @Override
    public ResponseEntity<WeeklyPlanningDto> findById(Integer id) {
        return ResponseEntity.ok(weeklyPlanningService.findById(id));
    }

    @Override
    public ResponseEntity<List<WeeklyPlanningDto>> findAll() {
        return ResponseEntity.ok(weeklyPlanningService.findAll());
    }

    @Override
    public void deleteById(Integer id) {
        weeklyPlanningService.deleteById(id);
    }
}
