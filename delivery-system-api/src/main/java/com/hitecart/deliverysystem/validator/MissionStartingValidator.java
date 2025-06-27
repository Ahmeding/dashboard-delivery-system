package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.mission.MissionStartEndRequest;

import java.util.ArrayList;
import java.util.List;

public class MissionStartingValidator {
    public static List<String> validate(MissionStartEndRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the start mileage!");
            errors.add("Please provide the context of the request!");
            errors.add("Please provide the vehicle");
            return errors;
        }
        if (request.startMileage()<=0) {
            errors.add("Please provide the start mileage!");
        }
        if (request.flag()==null) {
            errors.add("Please provide the context of the request!");
        }
        if (request.vehicleId()==null) {
            errors.add("Please provide the vehicle");
        }
        return errors;
    }
}
