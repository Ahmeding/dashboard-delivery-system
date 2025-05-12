package com.hitecart.deliverysystem.handlers;

import com.hitecart.deliverysystem.exception.ErrorCodes;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
    private Integer httpCode;
    private ErrorCodes errorCodes;
    private String msg;
    private List<String> errors = new ArrayList<>();



}
