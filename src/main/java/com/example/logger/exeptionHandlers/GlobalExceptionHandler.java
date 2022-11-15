package com.example.logger.exeptionHandlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.management.InvalidAttributeValueException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> customValidationErrorHandling(MethodArgumentNotValidException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Validation Error",
                exception.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> conflictHandler(RuntimeException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Validation Error",
                exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidAttributeValueException.class})
    public ResponseEntity<?> invalidAttributeValueHandler(InvalidAttributeValueException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/clients?page=0");
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> illegalArgumentHandler(IllegalArgumentException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Validation Error",
                exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
    }
}


