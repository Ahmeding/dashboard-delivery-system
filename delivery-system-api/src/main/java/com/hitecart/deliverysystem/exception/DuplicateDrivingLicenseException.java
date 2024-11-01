package com.hitecart.deliverysystem.exception;

public class DuplicateDrivingLicenseException extends RuntimeException {

    private ErrorCodes errorCodes;

    public DuplicateDrivingLicenseException(String message) {
        super(message);
    }

    public DuplicateDrivingLicenseException(String msg,Throwable cause){
        super(msg,cause);
    }

    public DuplicateDrivingLicenseException(String msg, Throwable cause, ErrorCodes errorCodes){
        super(msg,cause);
        this.errorCodes=errorCodes;
    }
    public DuplicateDrivingLicenseException(String msg, ErrorCodes errorCodes){
        super(msg);
        this.errorCodes=errorCodes;
    }
}
