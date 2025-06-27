package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.mission.MissionRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MissionValidator {
    public static List<String> validate(MissionRequest missionRequest) {
        List<String> errors = new ArrayList<>();

        if (missionRequest == null) {
            errors.add("Please provide the mission title!");
            errors.add("Please provide the mission start date!");
            errors.add("Please provide the mission end date!");
            errors.add("Please provide the delivery destination!");
            return errors;
        }

        if (!StringUtils.hasLength(missionRequest.title())) {
            errors.add("Please provide the mission title!");
        }
        if (missionRequest.startDate() == null) {
            errors.add("Please provide the mission start date!");
        }
        if (missionRequest.endDate() == null) {
            errors.add("Please provide the mission end date!");
        }
        if (missionRequest.deliveryDestinationId() == null) {
            errors.add("Please provide the delivery destination!");
        }

        return errors;
    }
}
