package com.hitecart.deliverysystem.controllers.user;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.FeedbackApi;
import com.hitecart.deliverysystem.dto.feedback.FeedBackResponse;
import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import com.hitecart.deliverysystem.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FeedbackController implements FeedbackApi {

    private final FeedbackService feedbackService;

    @Override
    public ResponseEntity<Integer> saveFeedback(FeedbackRequest feedBackRequest, Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.save(feedBackRequest, connectedUser));
    }

    @Override
    public ResponseEntity<Integer> updateFeedbackInfo(FeedbackRequest feedBackRequest, Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.updateFeedbackInfo(feedBackRequest, connectedUser));
    }

    @Override
    public ResponseEntity<PageResponse<FeedBackResponse>> findAllFeedBackByDriverPageable(Integer driverId, int page, int size, Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.findAllFeedBackByDriver(driverId, page, size, connectedUser));
    }

    @Override
    public ResponseEntity<?> deleteFeedback(Integer feedbackId, Authentication connectedUser) {
        try {
            feedbackService.delete(feedbackId, connectedUser);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Feedback deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Feedback not found with ID: " + feedbackId
                    ));
        }
    }
}
