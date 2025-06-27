package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import com.hitecart.deliverysystem.dto.feedback.FeedBackResponse;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.Feedback;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class FeedBackMapper {

   public Feedback toFeedback(FeedbackRequest request){
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .driver(Driver.builder()
                        .id(request.driverId())
                        .build())
                .build();
    }

    public FeedBackResponse toFeedBackResponse(Feedback feedback,Integer id){
        return FeedBackResponse.builder()
                .comment(feedback.getComment())
                .note(feedback.getNote())
                .ownFeedback(Objects.equals( feedback.getCreatedBy(),id))
                .build();
    }
}
