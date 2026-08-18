package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.DriverRepository;
import com.hitecart.deliverysystem.dao.FeedBackRepository;
import com.hitecart.deliverysystem.dto.feedback.FeedbackRequest;
import com.hitecart.deliverysystem.dto.feedback.FeedbackResponse;
import com.hitecart.deliverysystem.dto.notification.NotificationRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.FeedbackMapper;
import com.hitecart.deliverysystem.mapper.NotificationMapper;
import com.hitecart.deliverysystem.mapper.NotificationMapperImpl;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.Feedback;
import com.hitecart.deliverysystem.model.Notification;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;
import com.hitecart.deliverysystem.services.FeedbackService;
import com.hitecart.deliverysystem.services.NotificationService;
import com.hitecart.deliverysystem.validator.FeedbackValidator;
import jakarta.validation.constraints.NotNull;
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
    private final FeedbackMapper feedBackMapper;
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    /***
     *
     * @param feedbackRequest
     * @param connectedUser
     * @return Integer
     */
    @Override
    public FeedbackResponse save(FeedbackRequest feedbackRequest, Authentication connectedUser) {
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
    private FeedbackResponse savingFeedback(FeedbackRequest feedbackRequest, Authentication connectedUser ) {

        Driver driver = driverRepository.findById(feedbackRequest.driverId())
                .orElseThrow(() -> new EntityNotFoundException("No driver found with ID:: " + feedbackRequest.driverId()));
 /*       if (!driver.isAccountLocked()) {
            throw new InvalidOperationException("You cannot give a feedback for and expired Driver or driver with lockedDriver");
        }*/

        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(driver.getId(), user.getId())) {
            throw new InvalidOperationException("You cannot give feedback to your self");
        }
        checkIfCurrentLoggedUserAlreadySetFeedbackToGivenDriver(user,feedbackRequest.driverId());
        Feedback feedbackToSave = feedBackMapper.toEntity(feedbackRequest);
        FeedbackResponse feedbackResponse = feedBackMapper.toResponse(feedBackRepository.save(feedbackToSave),user.getId());
        sendNotification(user,feedbackRequest.driverId());
        return feedbackResponse;

    }



    /**
     *
     * @param feedBackRequest
     * @param connectedUser
     * @return Integer
     */
    @Override
    public FeedbackResponse updateFeedbackInfo(Integer feedbackId, FeedbackRequest feedBackRequest, Authentication connectedUser) {
        if(feedbackId <=0){
            throw new InvalidOperationException("Invalid feedback ID" , ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
        }
        if(feedBackRequest ==null){
            throw new InvalidOperationException("Invalid feedback request" , ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
        }
        if(connectedUser == null){
            throw new InvalidOperationException("Invalid Connected user" ,ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
        }

        Feedback existingFeedback = feedBackRepository.findById(feedbackId).orElseThrow(
                ()-> new EntityNotFoundException("Feedback with ::ID : " + feedbackId +" not found.")
        );

        User currentUser =  (User) connectedUser.getPrincipal();

        if(!existingFeedback.getCreatedBy().equals(currentUser.getId())){
            throw new InvalidOperationException("You are not the owner of the feedback, you cannot editing !",
                    ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
        }
        feedBackMapper.updateFeedbackFromRequest(feedBackRequest,existingFeedback);

        Feedback updatedFeedback = feedBackRepository.save(existingFeedback);

        return feedBackMapper.toResponse(updatedFeedback,currentUser.getId());
    }

    /***
     *
     * @param driverId
     * @param page
     * @param size
     * @param connectedUser
     * @return PageResponse<FeedbackResponse>
     */
    @Override
    @Transactional
    public PageResponse<FeedbackResponse> findAllFeedBackByDriver(Integer driverId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedBackRepository.findAllByDriverId(driverId,pageable);
        List<FeedbackResponse> feedbackRespons = feedbacks.stream()
                .map(f->feedBackMapper.toResponse(f,user.getId()))
                .toList();


        return new PageResponse<>(
                feedbackRespons,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }

    @Override
    public List<FeedbackResponse> findAllFeedBackByDriver(Integer driverId, Authentication connectedUser) {
        if(connectedUser == null) {
            throw new InvalidOperationException("You are not logged in",ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
        }
        if(driverId == null || driverId <= 0) {
            throw new InvalidOperationException("Invalid driver id",ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
        }
        driverRepository.findById(driverId).orElseThrow(()-> new EntityNotFoundException("Driver with ID :: "+ driverId
                + " does not exists", ErrorCodes.DRIVER_NOT_FOUND));

        User user= (User) connectedUser.getPrincipal();
        List<Feedback> feedbacks= feedBackRepository.findFeedbackByDriverId(driverId);
        List<FeedbackResponse> feedbackRespons = feedbacks.stream()
                .map(feedback -> feedBackMapper.toResponse(feedback,user.getId()))
                .toList();
        return feedbackRespons;
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
            throw new InvalidOperationException("You cannot delete a feedback not yours", ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
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


    private void sendNotification(User user, Integer driverId) {
        notificationService.sendNotification(
                driverId,
                notificationMapper.toRequest(
                        Notification.builder()
                                .title(
                                        user.getFirstname() + " " + user.getLastname()
                                                +  " left you feedback")
                                .message("You have received new feedback from "
                                        + user.getFirstname()
                                        + " "
                                        + user.getLastname()
                                        + ". Check it out now!")
                                .status(NotificationStatus.SEND)
                                .user(User.builder().id(driverId).build())
                                .build()
                )
        );
    }

    private void checkIfCurrentLoggedUserAlreadySetFeedbackToGivenDriver(User user, Integer driverId) {
        feedBackRepository.findFeedbackByOwnerIdByDriverId(user.getId(),driverId).ifPresent(existing->{
            throw new InvalidOperationException("You've already submitted feedback for this driver. " +
                    "You can only submit one feedback per driver.",ErrorCodes.FEEDBACK_NOT_VALID_OPERATION);
        });
    }


}
