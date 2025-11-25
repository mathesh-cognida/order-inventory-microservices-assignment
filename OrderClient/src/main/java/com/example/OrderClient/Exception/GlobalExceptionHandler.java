package com.example.OrderClient.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderFailedException.class)
    public ResponseEntity<ExceptionResponse> handleOrderFailedException(OrderFailedException orderFailedException){
        ExceptionResponse er = new ExceptionResponse(
                orderFailedException.getMessage(),
                "NOT_FOUND",
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

}
