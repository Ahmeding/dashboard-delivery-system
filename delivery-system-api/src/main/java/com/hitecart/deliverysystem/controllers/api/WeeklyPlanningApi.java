package com.hitecart.deliverysystem.controllers.api;


import com.hitecart.deliverysystem.dto.basic.WeeklyPlanningDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(WEEKLY_PLANNING_ENDPOINT)
@Tag( name = "Weekly Planning")
public interface WeeklyPlanningApi {

    @PostMapping(value = CREATE_WEEKLY_PLANNING_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<WeeklyPlanningDto>  save(@RequestBody WeeklyPlanningDto dto);
    @GetMapping(value = FIND_WEEKLY_PLANNING_BY_ID_ENDPOINT)
    ResponseEntity<WeeklyPlanningDto> findById(@PathVariable("id") Integer id);
    @GetMapping(value = FIND_ALL_WEEKLY_PLANNING_ENDPOINT)
    ResponseEntity<List<WeeklyPlanningDto>> findAll();
    @DeleteMapping(value = DELETE_WEEKLY_PLANNING_ENDPOINT)
    void deleteById(@PathVariable("id") Integer id);
}
