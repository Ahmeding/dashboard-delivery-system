package com.hitecart.deliverysystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hitecart.deliverysystem.models.WeeklyPlanning;
import com.hitecart.deliverysystem.services.WeeklyPlanningService;

import java.util.List;

@RestController
@RequestMapping("/api/weeklyplannings")
public class WeeklyPlanningController {
    @Autowired
    private WeeklyPlanningService service;

    @GetMapping
    public List<WeeklyPlanning> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public WeeklyPlanning getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public WeeklyPlanning create(@RequestBody WeeklyPlanning weeklyplanning) {
        return service.save(weeklyplanning);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
