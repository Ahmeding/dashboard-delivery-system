package com.hitecart.deliverysystem.dto;

import java.time.LocalDateTime;

public interface MissionAssignmentDto {
    Integer getMissionId();
    String getMissionReference();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    Integer getVehicleId();
}
