package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import com.hitecart.deliverysystem.dto.feedback.FeedbackResponse;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.Feedback;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedbackMapper {

    @Mapping(target = "driver",source = "driverId")
    Feedback toEntity(FeedbackRequest request);

    @Mapping(target = "ownFeedback",
            expression = "java(feedback.getCreatedBy().equals(connectedUserId))")
    FeedbackResponse toResponse(Feedback feedback, Integer connectedUserId);

    default Driver mapDriver(Integer driverId) {
        if (driverId == null) return null;
        return Driver.builder().id(driverId).build();
    }
    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateFeedbackFromRequest(FeedbackRequest request, @MappingTarget Feedback entity);
}
