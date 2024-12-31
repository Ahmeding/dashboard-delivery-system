package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.DayName;
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
public class DayPlan extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private DayName name;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "id_weekly_planning")
    private WeeklyPlanning weeklyPlanning;
    @OneToMany(mappedBy = "dayPlan")
    private List<Mission> missions=new ArrayList<>();

}
