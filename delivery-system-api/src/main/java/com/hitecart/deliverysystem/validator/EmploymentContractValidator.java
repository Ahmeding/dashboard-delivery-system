package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;

import java.util.ArrayList;
import java.util.List;

public class EmploymentContractValidator {
    public static List<String> validate(EmploymentContractRequest request){
        List<String> errors= new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the contract start date!");
            errors.add("Please provide the contract type!");
            errors.add("Please provide the contract end date!");
            return errors;
        }

        if (request.driverId() == null) {
            errors.add("Please specify the associated driver!");
        }
        if (request.contractType() == null) {
            errors.add("Please provide the contract type!");
        }
        if (request.startDate() == null) {
            errors.add("Please provide the contract start date!");
        }
        if (request.endDate() == null) {
            errors.add("Please provide the contract end date!");
        }

        return errors;

    }
}
