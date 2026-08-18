package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import com.hitecart.deliverysystem.dto.feedback.FeedbackResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse save(FeedbackRequest feedBackRequest, Authentication connectedUser);
    FeedbackResponse updateFeedbackInfo(Integer feedbackId, FeedbackRequest feedBackRequest, Authentication connectedUser);
    PageResponse<FeedbackResponse> findAllFeedBackByDriver(Integer driverId, int page, int size, Authentication connectedUser);
    List<FeedbackResponse> findAllFeedBackByDriver(Integer driverId, Authentication connectedUser);
    void delete(Integer feedbackId, Authentication connectedUser);
}
