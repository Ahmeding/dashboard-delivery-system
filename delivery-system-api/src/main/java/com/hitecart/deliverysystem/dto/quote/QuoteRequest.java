package com.hitecart.deliverysystem.dto.quote;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.quoteline.QuoteLineRequest;
import com.hitecart.deliverysystem.model.enums.QuoteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Represents a request for creating or updating a quote.
 * <p>
 * Includes business details like pricing, validity period, and associated items.
 * All monetary amounts are in EUR and must be non-negative.
 * </p>
 *
 * @param id              Unique identifier for existing quotes (null for new quotes)
 * @param reference       Human-readable reference identifier
 * @param code            System-generated quote identifier
 * @param quoteDate       Date/time when quote was generated
 * @param totalAmountHT   Total amount before tax (exclusive)
 * @param totalAmountTTC  Total amount including tax (inclusive)
 * @param quoteStatus     Current workflow status of the quote
 * @param expDate         Date/time when quote expires
 * @param clientId        Associated client identifier
 * @param quoteLines      List of quoted items/services
 */
@Schema(
        name = "QuoteRequest",
        description = "Request payload for creating or updating quotes",
        accessMode = Schema.AccessMode.READ_WRITE
)
public record QuoteRequest(
        Integer id,
        @NotBlank(message = "Reference is required")
        @Size(max = 50, message = "Reference must be at most 50 characters")
        String reference,

        @NotBlank(message = "Code is required")
        @Size(max = 20, message = "Code must be at most 20 characters")
        String code,

        @NotNull(message = "Quote date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
        @PastOrPresent(message = "Quote date cannot be in the future")
        LocalDateTime quoteDate,

        @NotNull(message = "Total amount HT is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Total amount HT cannot be negative")
        BigDecimal totalAmountHT,

        @NotNull(message = "Total amount TTC is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Total amount TTC cannot be negative")
        BigDecimal totalAmountTTC,

        @NotNull(message = "Quote status is required")
        QuoteStatus quoteStatus,

        @NotNull(message = "Expiration date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
        @FutureOrPresent(message = "Expiration date cannot be in the past")
        LocalDateTime expDate,

        @NotNull(message = "Client ID is required")
        Integer clientId,

        @Valid
        @NotEmpty(message = "At least one quote line must be provided")
        List<QuoteLineRequest> quoteLines
){}
