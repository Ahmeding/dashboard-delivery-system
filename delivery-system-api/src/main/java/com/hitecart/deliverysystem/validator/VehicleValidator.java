package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.vehicle.VehicleRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VehicleValidator {
    public static List<String> validate(VehicleRequest vehicleRequest){
        List<String> errors= new ArrayList<>();

        if (vehicleRequest == null) {
            errors.add("Please provide the vehicle registration number!");
            errors.add("Please provide the vehicle brand!");
            errors.add("Please provide the vehicle model!");
            errors.add("Please provide the vehicle VIN number!");
            errors.add("Please provide the vehicle's first registration date!");
            errors.add("Please provide the vehicle mileage!");
            errors.add("Please provide the vehicle fuel type!");
            errors.add("Please provide the vehicle volume!");
            errors.add("Please provide the vehicle weight capacity!");
            return errors;
        }

        if (!StringUtils.hasLength(vehicleRequest.registrationNbr())) {
            errors.add("Please provide the vehicle registration number!");
        }

        if (!StringUtils.hasLength(vehicleRequest.brand())) {
            errors.add("Please provide the vehicle brand!");
        }

        if (!StringUtils.hasLength(vehicleRequest.model())) {
            errors.add("Please provide the vehicle model!");
        }

        if (!StringUtils.hasLength(vehicleRequest.vinNbr())) {
            errors.add("Please provide the vehicle VIN number!");
        }

        if (vehicleRequest.dfcDate() == null) {
            errors.add("Please provide the vehicle's first registration date!");
        }

        if (vehicleRequest.mileage() == null) {
            errors.add("Please provide the vehicle mileage!");
        }

        if (vehicleRequest.energyType() == null) {
            errors.add("Please provide the vehicle fuel type!");
        }

        if (vehicleRequest.volumeCapacity() == null) {
            errors.add("Please provide the vehicle volume!");
        }

        if (vehicleRequest.weightCapacity() == null) {
            errors.add("Please provide the vehicle weight capacity!");
        }

        return errors;


    }
}
