package com.hitecart.deliverysystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hitecart.deliverysystem.models.DayPlan;
import com.hitecart.deliverysystem.services.DayPlanService;

import java.util.List;

@RestController
@RequestMapping("/api/dayplans")
public class DayPlanController {
    @Autowired
    private DayPlanService service;

    @GetMapping
    public List<DayPlan> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public DayPlan getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public DayPlan create(@RequestBody DayPlan dayplan) {
        return service.save(dayplan);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
