package com.kotyk.realtorconnect.util.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final FieldError fieldErrorPlug = new FieldError("null", "null", "null");

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> methodBindException(BindException ex, WebRequest request) {
        FieldError fieldError = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .orElse(fieldErrorPlug);
        Error error = new Error(
                Instant.now(),
                "Validation failed",
                String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()),
                request.getDescription(false)
        );
        return ResponseEntity.badRequest().body(error);
    }

}
