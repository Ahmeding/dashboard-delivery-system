package com.hitecart.deliverysystem.dto.company;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.address.AddressResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyResponse {
    private Integer id;
    private String name;
    private String taxNbr;
    private String phoneNbr;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private AddressResponse address;

}
