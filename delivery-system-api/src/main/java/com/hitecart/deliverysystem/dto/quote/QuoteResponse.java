package com.hitecart.deliverysystem.dto.quote;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.quoteline.QuoteLineResponse;
import com.hitecart.deliverysystem.model.enums.QuoteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuoteResponse {

    private Integer id;
    private String reference;
    private String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime quoteDate;
    private BigDecimal totalAmountHT;
    private BigDecimal totalAmountTTC;
    private QuoteStatus quoteStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDate expDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;

    private ClientResponse client;

    private List<QuoteLineResponse> quoteLines;
}
