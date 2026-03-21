package com.harshadcodes.EcommerceWebsite.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> myMethodNotFoundException (MethodArgumentNotValidException ex){
        Map<String,Object> map=new HashMap<>();
       ex.getBindingResult().getFieldErrors().forEach(fieldError -> map.put(fieldError.getField(),fieldError.getDefaultMessage()));
       ErrorResponse er=new ErrorResponse(HttpStatus.BAD_REQUEST.value(),LocalDateTime.now(),map);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponse> myResourceNotFoundException (ResourceNotFoundException e){
        ErrorResponse er=new ErrorResponse(e.getMessage(),HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    ResponseEntity<ErrorResponse> myResourceAAlreadyExistException (ResourceAlreadyExistException e){
        ErrorResponse er=new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> myDefaultException(DefaultException e){
        ErrorResponse er=new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ErrorResponse> myAuthenticationException(AuthenticationException e){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Bad credentials");
        map.put("status", false);
        ErrorResponse er=new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),LocalDateTime.now(),map);

        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
    }


}
