package com.example.InventoryClient.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductControllerAdvice {
    @ExceptionHandler(exception = InvalidArgumentException.class)
    public ResponseEntity<String> handleInvalidException(){
        return  new ResponseEntity<>("Exception Occured", HttpStatus.BAD_REQUEST);
    }
}
