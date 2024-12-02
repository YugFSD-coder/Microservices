package com.service.category.exception;

import com.service.category.dto.CustomMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomMessage> handleResourceNotFound(ResourceNotFoundException ex){
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage(ex.getMessage());
        customMessage.setSuccess(false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customMessage);
    }

}
