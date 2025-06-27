package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MissionExpenseValidator {
    public static List<String> validate(MissionExpenseRequest request){
        List<String> errors= new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the title of the mission expense!");
            errors.add("Please enter at least one mission expense!");
            errors.add("Please provide the type of the mission expense!");
            errors.add("Please provide the quantity of the mission expense!");
            errors.add("Please provide the amount!");
            errors.add("Please provide the mission date!");
            errors.add("Please specify which mission this expense is for!");
            return errors;
        }

        if (!StringUtils.hasLength(request.name())) {
            errors.add("Please provide the name of the mission expense!");
        }

        if (request.type() == null) {
            errors.add("Please provide the type of the mission expense!");
        }

        if (request.quantity() == null) {
            errors.add("Please provide the quantity of the mission expense!");
        } else if (request.quantity().doubleValue() <= 0) {
            errors.add("The quantity must be greater than 0!");
        }

        if (request.cost() == null) {
            errors.add("Please provide the amount!");
        } else if (request.cost().doubleValue() <= 0) {
            errors.add("The amount must be greater than 0!");
        }
        LocalDateTime today = LocalDateTime.now();
        if (request.expenseDate() == null) {
            errors.add("Please provide the date!");
        }

        if (request.missionId() == null) {
            errors.add("Please specify which mission this expense is for!");
        }

        return errors;

    }
}
