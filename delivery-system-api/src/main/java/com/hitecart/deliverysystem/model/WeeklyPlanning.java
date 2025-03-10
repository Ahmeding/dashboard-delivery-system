package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
public class WeeklyPlanning extends AbstractEntity {

    private String title;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    @OneToMany(mappedBy = "weeklyPlanning", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DayPlan> daysPlan;

    // Getters and Setters
}
