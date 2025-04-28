package com.hitecart.deliverysystem.utils;

import com.hitecart.deliverysystem.model.Chauffeur;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.Vehicule;
import lombok.*;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResult {
    private Mission mission;
    private Vehicule vehicle;
    private List<Chauffeur> drivers;

}
