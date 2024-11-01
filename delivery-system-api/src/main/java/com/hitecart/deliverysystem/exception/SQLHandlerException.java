package com.hitecart.deliverysystem.exception;

import java.util.List;

public class SQLHandlerException extends RuntimeException{
    private ErrorCodes errorCodes;
    private List<String> errors;

    public SQLHandlerException(String msg,ErrorCodes errorCodes) {
        super(msg);
        this.errorCodes = errorCodes;
    }

    public SQLHandlerException(String message, Throwable cause) {
        super(message, cause);

    }

    public SQLHandlerException(String message, Throwable cause,ErrorCodes errorCodes) {
        super(message, cause);
        this.errorCodes=errorCodes;
    }


    public SQLHandlerException(String message,ErrorCodes errorCodes,List<String> errors) {
        super(message);
        this.errorCodes=errorCodes;
        this.errors=errors;
    }
}
