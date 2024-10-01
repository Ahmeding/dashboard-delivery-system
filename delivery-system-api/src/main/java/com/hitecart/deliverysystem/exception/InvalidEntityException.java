 package com.hitecart.deliverysystem.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidEntityException extends RuntimeException{
    private ErrorCodes errorCodes;
    private List<String> errors;

    public InvalidEntityException(String msg,ErrorCodes errorCodes) {
        super(msg);
        this.errorCodes = errorCodes;
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);

    }

    public InvalidEntityException(String message, Throwable cause,ErrorCodes errorCodes) {
        super(message, cause);
        this.errorCodes=errorCodes;
    }


    public InvalidEntityException(String message,ErrorCodes errorCodes,List<String> errors) {
        super(message);
        this.errorCodes=errorCodes;
        this.errors=errors;
    }


}
