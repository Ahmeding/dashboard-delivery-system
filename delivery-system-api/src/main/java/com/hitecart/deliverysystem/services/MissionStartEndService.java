package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.mission.MissionStartEndRequest;
import com.hitecart.deliverysystem.dto.mission.MissionStatusUpdateRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.model.enums.KmFlag;
import com.hitecart.deliverysystem.model.enums.MissionStatus;

import java.util.List;

public interface MissionStartEndService {

    MissionResponse updateMileage(Integer missionId, MissionStartEndRequest missionStartEndRequest);

    MissionResponse updateMissionStatus(Integer missionId, MissionStatusUpdateRequest missionStatusUpdateRequest);

}
