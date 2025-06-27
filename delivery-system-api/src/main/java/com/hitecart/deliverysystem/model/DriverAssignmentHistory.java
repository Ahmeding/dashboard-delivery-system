package com.hitecart.deliverysystem.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class DriverAssignmentHistory extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private LocalDateTime assignmentDate;
    //private DriverRole role;
}

