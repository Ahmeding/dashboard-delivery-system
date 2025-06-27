package com.hitecart.deliverysystem.validator;

import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RoleValidator {

    public static List<String> validate(RoleRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Please provide the name of the role!");
            return errors;
        }
        if (!StringUtils.hasLength(request.name().name())) {
            errors.add("Please provide the name of the role!");
        }
        return errors;
    }
}
