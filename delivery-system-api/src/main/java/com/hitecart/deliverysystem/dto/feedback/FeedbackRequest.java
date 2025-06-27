package com.hitecart.deliverysystem.dto.feedback;

import jakarta.validation.constraints.*;

public record FeedbackRequest(
        Integer id,
        @NotNull(message = "Rating is required")
        @DecimalMin(value = "0", inclusive = true, message = "Rating must be at least 0")
        @DecimalMax(value = "5", inclusive = true, message = "Rating must be at most 5")
        Double note,

        @NotBlank(message = "Comment is required")
        @Size(max = 1000, message = "Comment must be at most 1000 characters")
        String comment,

        @NotNull(message = "Driver ID is required")
        Integer driverId

) {
}
