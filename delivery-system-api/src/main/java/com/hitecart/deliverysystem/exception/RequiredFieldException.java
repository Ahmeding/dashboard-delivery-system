package com.hitecart.deliverysystem.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class RequiredFieldException extends RuntimeException{

    private ErrorCodes errorCode;
    private List<String> errors;

    public RequiredFieldException(String message) {super(message);}
    public RequiredFieldException(String message,Throwable cause) {super(message,cause);}
    public RequiredFieldException(String message,Throwable cause,ErrorCodes errorCodes) {
        super(message,cause);
        this.errorCode = errorCodes;
    }
    public RequiredFieldException(String message,ErrorCodes errorCodes, List<String> errors) {
        super(message);
        this.errorCode = errorCodes;
        this.errors = errors;
    }
    public RequiredFieldException(String message,ErrorCodes errorCodes) {super(message);
    this.errorCode = errorCodes;

    }

}
