package com.hitecart.deliverysystem.utils;

import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.Vehicle;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResult {
    private Mission mission;
    private Vehicle vehicle;
    private List<Driver> drivers;

}
