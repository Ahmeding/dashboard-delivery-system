package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.DriverRepository;
import com.hitecart.deliverysystem.dao.FeedBackRepository;
import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import com.hitecart.deliverysystem.dto.feedback.FeedBackResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.FeedBackMapper;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.Feedback;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.services.FeedbackService;
import com.hitecart.deliverysystem.validator.ClientValidator;
import com.hitecart.deliverysystem.validator.FeedbackValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedBackServiceImpl implements FeedbackService {

    private final FeedBackRepository feedBackRepository;
    private final DriverRepository driverRepository;
    private final FeedBackMapper feedBackMapper;

    /***
     *
     * @param feedbackRequest
     * @param connectedUser
     * @return Integer
     */
    @Override
    public Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        checkRequest(
                feedbackRequest,
                "Feedback n'est pas valid",
                "Le Feedback n'est pa valide ",
                ErrorCodes.FEEDBACK_NOT_VALID);
        return savingFeedback(feedbackRequest,connectedUser);
    }


    /**
     *
     * @param feedbackRequest
     * @param connectedUser
     * @return Integer
     */
    private Integer savingFeedback(FeedbackRequest feedbackRequest, Authentication connectedUser ) {

        Driver driver = driverRepository.findById(feedbackRequest.driverId())
                .orElseThrow(() -> new EntityNotFoundException("No driver found with ID:: " + feedbackRequest.driverId()));
        if (driver.isAccountNonExpired() || !driver.isAccountLocked()) {
            throw new InvalidOperationException("You cannot give a feedback for and expired Driver or driver with lockedDriver");
        }

        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(driver.getId(), user.getId())) {
            throw new InvalidOperationException("You cannot give feedback to your self");
        }
        Feedback feedback = feedBackMapper.toFeedback(feedbackRequest);
        return feedBackRepository.save(feedback).getId();
    }

    /**
     *
     * @param feedBackRequest
     * @param connectedUser
     * @return Integer
     */
    @Override
    public Integer updateFeedbackInfo(FeedbackRequest feedBackRequest, Authentication connectedUser) {
        feedBackRepository.findById(feedBackRequest.id()).orElseThrow(
                ()-> new EntityNotFoundException("Feedback with ::ID : " + feedBackRequest.id() +" not found.")
        );


        return savingFeedback(feedBackRequest,connectedUser);
    }

    /***
     *
     * @param driverId
     * @param page
     * @param size
     * @param connectedUser
     * @return PageResponse<FeedBackResponse>
     */
    @Override
    @Transactional
    public PageResponse<FeedBackResponse> findAllFeedBackByDriver(Integer driverId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedBackRepository.findAllByDriverId(driverId,pageable);
        List<FeedBackResponse> feedBackResponses = feedbacks.stream()
                .map(f->feedBackMapper.toFeedBackResponse(f,user.getId()))
                .toList();


        return new PageResponse<>(
                feedBackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }

    @Override
    public void delete(Integer feedbackId, Authentication connectedUser) {
        if(feedbackId == null || feedbackId <= 0){
            throw new InvalidOperationException("Invalid feedback ID",ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
        }
        Feedback feedback=  feedBackRepository.findById(feedbackId).orElseThrow(
                ()-> new EntityNotFoundException("No feedback found with ID:: " + feedbackId,ErrorCodes.FEEDBACK_NOT_FOUND)
        );

        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(feedback.getCreatedBy(), user.getId())){
            throw new InvalidOperationException("You cannot delete a feedback not yours");
        }

        feedBackRepository.deleteById(feedbackId);
    }

    /**
     *
     * @param request
     * @param msgLog
     * @param msgException
     * @param errorCode
     */
    private void checkRequest(FeedbackRequest request, String msgLog, String msgException, ErrorCodes errorCode) {
        List<String> errors = FeedbackValidator.validate(request);
        if(!errors.isEmpty()){
            log.error(msgLog,request);
            throw new InvalidEntityException(msgException, errorCode,errors);
        }
    }
}
