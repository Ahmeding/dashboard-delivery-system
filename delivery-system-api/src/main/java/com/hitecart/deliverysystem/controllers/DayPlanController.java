package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.DayPlanApi;
import com.hitecart.deliverysystem.dto.basic.DayPlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hitecart.deliverysystem.services.DayPlanService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DayPlanController implements DayPlanApi {

    private final DayPlanService dayPlanService;

    @Override
    public ResponseEntity<DayPlanDto> save(DayPlanDto dto) {
        return ResponseEntity.ok(dayPlanService.save(dto));
    }

    @Override
    public ResponseEntity<DayPlanDto> findById(Integer id) {
        return ResponseEntity.ok(dayPlanService.findById(id));
    }

    @Override
    public ResponseEntity<List<DayPlanDto>> findAll() {
        return ResponseEntity.ok(dayPlanService.findAll());
    }

    @Override
    public ResponseEntity<List<DayPlanDto>> findAllByWeeklyPlanningId(Integer weeklyPlanningId) {
        return ResponseEntity.ok(dayPlanService.findAllByWeeklyPlanningId(weeklyPlanningId));
    }

    @Override
    public void deleteById(Integer id) {
        dayPlanService.deleteById(id);
    }
}
