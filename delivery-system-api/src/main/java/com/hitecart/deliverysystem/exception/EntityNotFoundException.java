package com.hitecart.deliverysystem.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private ErrorCodes errorCodes;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String msg,Throwable cause){
        super(msg,cause);
    }

    public EntityNotFoundException(String msg, Throwable cause, ErrorCodes errorCodes){
        super(msg,cause);
        this.errorCodes=errorCodes;
    }
    public EntityNotFoundException(String msg, ErrorCodes errorCodes){
        super(msg);
        this.errorCodes=errorCodes;
    }
}
