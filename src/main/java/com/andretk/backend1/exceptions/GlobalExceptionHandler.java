package com.andretk.backend1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidException(
            MethodArgumentNotValidException exception
    ){
        var errors = new HashMap<String,String>();
        exception.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(),err.getDefaultMessage()) );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Map<String,String>> handleConflictException(
            ResourceConflictException exception
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error",exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNotFoundException(
            ResourceNotFoundException exception
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",exception.getMessage()));
    }
}
