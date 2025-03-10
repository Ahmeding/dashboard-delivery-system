package com.hitecart.deliverysystem.handlers;

import com.hitecart.deliverysystem.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.Collections;


@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(EntityNotFoundException exception,WebRequest webRequest){
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        final ErrorDto errorDto= ErrorDto.builder()
                .errorCodes(exception.getErrorCodes())
                .msg(exception.getMessage())
                .httpCode(notFound.value())
                .build();
        return new ResponseEntity<>(errorDto,notFound);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorDto> handleException(TokenExpiredException exception, WebRequest request) {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(exception.getErrorCodes())
                .msg(exception.getMessage())
                .httpCode(status.value())
                .errors(Collections.singletonList("Token expired! Renew it!"))
                .build();
        return new ResponseEntity<>(errorDto, status);
    }



    @ExceptionHandler(DuplicateDrivingLicenseException.class)
    public ResponseEntity<ErrorDto> handleException(DuplicateDrivingLicenseException exception,WebRequest request) {
        final HttpStatus DuplicateDLException = HttpStatus.CONFLICT;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(ErrorCodes.DUPLICATE_DRIVING_LICENSE_EXCEPTION)
                .msg(exception.getMessage())
                .errors(Collections.singletonList(exception.getMessage()))
                .httpCode(DuplicateDLException.value())
                .build();
        return new ResponseEntity<>(errorDto, DuplicateDLException);
    }
    @ExceptionHandler(SQLHandlerException.class)
    public ResponseEntity<ErrorDto> handleException(SQLException exception,WebRequest request) {

        final HttpStatus sqlException = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(ErrorCodes.DUPLICATE_DRIVING_LICENSE_EXCEPTION)
                .msg(exception.getMessage())
                .httpCode(sqlException.value())
                .build();
        return new ResponseEntity<>(errorDto, sqlException);
    }


 @ExceptionHandler(InvalidEntityException.class)
 public ResponseEntity<ErrorDto> handleException(InvalidEntityException exception,WebRequest webRequest){
     final HttpStatus badRequet = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(exception.getErrorCodes())
                .msg(exception.getMessage())
                .httpCode(badRequet.value())
                .errors(exception.getErrors())
                .build();

        return new ResponseEntity<>(errorDto,badRequet);
 }



    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorDto> handleException(InvalidOperationException exception,WebRequest webRequest){
        final HttpStatus badRequet = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(exception.getErrorCodes())
                .msg(exception.getMessage())
                .errors(exception.getErrors())
                .httpCode(badRequet.value())
                .build();

        return new ResponseEntity<>(errorDto,badRequet);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleException(BadCredentialsException exception,WebRequest webRequest){
        final HttpStatus badRequet = HttpStatus.UNAUTHORIZED;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(ErrorCodes.BAD_CREDENTIALS)
                .httpCode(badRequet.value())
                .msg(exception.getMessage())
                .errors(Collections.singletonList("Please check your username and password."))
                .build();

        return new ResponseEntity<>(errorDto,badRequet);
    }

    @ExceptionHandler(RequiredFieldException.class)
    public ResponseEntity<ErrorDto> handleException(RequiredFieldException exception,WebRequest webRequest){
        final HttpStatus badRequet = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(ErrorCodes.BAD_CREDENTIALS)
                .httpCode(badRequet.value())
                .msg(exception.getMessage())
                .errors(exception.getErrors())
                .build();
        return new ResponseEntity<>(errorDto,badRequet);
    }
}
