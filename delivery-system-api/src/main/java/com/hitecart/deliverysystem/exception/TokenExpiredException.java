package com.hitecart.deliverysystem.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class TokenExpiredException extends RuntimeException{

    private ErrorCodes errorCodes;
    private List<String> errors;

    public TokenExpiredException(String msg,ErrorCodes errorCodes) {
        super(msg);
        this.errorCodes = errorCodes;
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);

    }

    public TokenExpiredException(String message, Throwable cause,ErrorCodes errorCodes) {
        super(message, cause);
        this.errorCodes=errorCodes;
    }


    public TokenExpiredException(String message,ErrorCodes errorCodes,List<String> errors) {
        super(message);
        this.errorCodes=errorCodes;
        this.errors=errors;
    }

}
