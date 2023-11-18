package com.kotyk.realtorconnect.util.exception;

import com.kotyk.realtorconnect.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Optional;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.badRequest;
import static com.kotyk.realtorconnect.util.ApiResponseUtil.wrapError;

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
        return badRequest(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> methodAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Forbidden",
                ex.getMessage(),
                request.getDescription(false)
        );
        return wrapError(error, HttpStatus.FORBIDDEN);
    }

}
