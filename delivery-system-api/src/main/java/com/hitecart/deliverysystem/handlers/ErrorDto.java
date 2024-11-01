package com.hitecart.deliverysystem.handlers;

import com.hitecart.deliverysystem.exception.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
    private Integer httpCode;
    private ErrorCodes errorCodes;
    private String msg;
    private List<String> errors = new ArrayList<>();



}
