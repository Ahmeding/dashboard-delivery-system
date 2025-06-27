package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StopPointValidator {
    public static List<String> validate(StopPointRequest stopPointRequest){
        List<String> errors= new ArrayList<>();

        if (stopPointRequest == null) {
            errors.add("Please provide the departure location of the stop!");
            errors.add("Please provide the arrival date of the stop!");
            errors.add("Please provide the departure date of the stop!");
            errors.add("Please provide the scheduled delivery time of the stop!");
            return errors;
        }

        if (!StringUtils.hasLength(stopPointRequest.title())) {
            errors.add("Please provide the title of the stop!");
        }
        if (!StringUtils.hasLength(stopPointRequest.arrivalLocation())) {
            errors.add("Please provide the arrival location of the stop!");
        }
        if (!StringUtils.hasLength(stopPointRequest.departureLocation())) {
            errors.add("Please provide the departure location of the stop!");
        }
        if (stopPointRequest.arrivalDate() == null) {
            errors.add("Please provide the arrival date of the stop!");
        }
        if (stopPointRequest.departureDate() == null) {
            errors.add("Please provide the departure date of the stop!");
        }
        if (stopPointRequest.scheduledDeliveryTime() == null) {
            errors.add("Please provide the scheduled delivery time of the stop!");
        }

        return errors;
    }
}
