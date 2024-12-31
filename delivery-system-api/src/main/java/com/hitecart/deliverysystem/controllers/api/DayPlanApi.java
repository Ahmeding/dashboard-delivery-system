package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.dto.DayPlanDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(DAY_PLAN_ENDPOINT)
@Tag( name = "Day-Plan")
public interface DayPlanApi {

    @PostMapping(value = CREATE_DAY_PLAN_ENDPOINT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DayPlanDto>  save(@RequestBody DayPlanDto dto);
    @GetMapping(value = FIND_DAY_PLAN_BY_ID_ENDPOINT)
    ResponseEntity<DayPlanDto> findById(@PathVariable("id") Integer id);
    @GetMapping(value = FIND_ALL_DAY_PLAN_ENDPOINT)
    ResponseEntity<List<DayPlanDto>> findAll();
    @GetMapping(value = FIND_ALL_DAY_PLAN_BY_WEEKLY_PLANNING_ID_ENDPOINT)
    ResponseEntity<List<DayPlanDto>> findAllByWeeklyPlanningId(@PathVariable("weekly_pId") Integer weeklyPlanningId);
    @DeleteMapping(value = DELETE_DAY_PLAN_ENDPOINT)
    void deleteById(@PathVariable("id") Integer id);
}
