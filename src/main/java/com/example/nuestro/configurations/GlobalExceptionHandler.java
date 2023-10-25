package com.example.nuestro.configurations;

import com.example.nuestro.models.ResponseModel;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseModel> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // Custom logic to handle the exception
        String errorMessage = "Invalid JSON format in the request body: " + ex.getMessage();
        return  ResponseModel.Fail(errorMessage, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleForbiddenException(RuntimeException ex) {
        // Handle the exception and return an appropriate response
        return new ResponseEntity<>("Access is forbidden: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    // You can add more @ExceptionHandler methods for handling other types of exceptions as well.
}
