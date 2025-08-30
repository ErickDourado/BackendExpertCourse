package com.erick.authserviceapi.controller.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import models.exceptions.StandardError;
import models.exceptions.ValidationError;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<StandardError> handleBadCredentialsException(final BadCredentialsException ex,
                                                                final HttpServletRequest request) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                StandardError.builder()
                        .timestamp(now())
                        .status(UNAUTHORIZED.value())
                        .error(UNAUTHORIZED.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ValidationError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex,
                                                                          final HttpServletRequest request) {
        ValidationError validationError = ValidationError.builder()
                .timestamp(now())
                .status(BAD_REQUEST.value())
                .error("Validation exception")
                .message("Exception in validation attributes")
                .path(request.getRequestURI())
                .errors(new ArrayList<>())
                .build();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> validationError.addError(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST).body(validationError);
    }

}
