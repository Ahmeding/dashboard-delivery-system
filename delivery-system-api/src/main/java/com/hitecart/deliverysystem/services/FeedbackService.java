package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import com.hitecart.deliverysystem.dto.feedback.FeedBackResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Integer save(FeedbackRequest feedBackRequest, Authentication connectedUser);
    Integer updateFeedbackInfo(FeedbackRequest feedBackRequest, Authentication connectedUser);
    PageResponse<FeedBackResponse> findAllFeedBackByDriver(Integer driverId,  int page, int size, Authentication connectedUser);
    void delete(Integer feedbackId, Authentication connectedUser);
}
