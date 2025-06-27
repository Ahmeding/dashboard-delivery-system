package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.repair.RepairRequest;

import java.util.ArrayList;
import java.util.List;

public class RepairValidator {
    public static List<String> validate(RepairRequest repairRequest){
        List<String> errors= new ArrayList<>();

        if (repairRequest == null) {
            errors.add("Please provide the type of the repair!");
            errors.add("Please provide the vehicle to be repaired!");
            errors.add("Please provide the start date of the repair!");
            errors.add("Please provide the end date of the repair!");
            return errors;
        }

        if (repairRequest.type() == null) {
            errors.add("Please provide the type of the repair!");
        }

        if (repairRequest.vehicleId() == null) {
            errors.add("Please provide the vehicle to be repaired!");
        }

        if (repairRequest.startDate() == null) {
            errors.add("Please provide the start date of the repair!");
        }

        if (repairRequest.endDate() == null) {
            errors.add("Please provide the end date of the repair!");
        }

        return errors;

    }
}
