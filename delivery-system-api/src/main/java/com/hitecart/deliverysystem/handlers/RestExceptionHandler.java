package com.hitecart.deliverysystem.handlers;

import com.hitecart.deliverysystem.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;


@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {



    @ExceptionHandler(EntityNotFoundException.class)
 public ResponseEntity<ErrorDto> handleException(
         EntityNotFoundException exception,
         WebRequest webRequest
 ){
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
         final ErrorDto errorDto= ErrorDto.builder()
             .errorCodes(exception.getErrorCodes())
             .msg(exception.getMessage())
             .httpCode(notFound.value())
             .build();
     return new ResponseEntity<>(errorDto,notFound);
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
        final HttpStatus badRequet = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(ErrorCodes.BAD_CREDENTIALS)
                .httpCode(badRequet.value())
                .msg(exception.getMessage())
                .errors(Collections.singletonList("Login et / ou mot de passe incorrecte"))
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
