package com.kotyk.realtorconnect.util.exception;

import com.kotyk.realtorconnect.dto.Error;
import com.kotyk.realtorconnect.dto.apiresponse.ApiError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final FieldError fieldErrorPlug = new FieldError("null", "null", "null");

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError<Error>> internalServerError(Exception ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Internal Server Error",
                ex.fillInStackTrace().toString(),
                request.getDescription(false)
        );
        log.error("", ex);
        return wrapError(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError<Error>> methodBindException(BindException ex, WebRequest request) {
        FieldError fieldError = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .orElse(fieldErrorPlug);
        Error error = new Error(
                Instant.now(),
                "Validation failed",
                String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()),
                request.getDescription(false)
        );
        log.debug("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return badRequest(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError<Error>> methodConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Validation failed",
                ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")),
                request.getDescription(false)
        );
        log.debug("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return badRequest(error);
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<ApiError<Error>> validationFailedException(ValidationFailedException ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Validation failed",
                ex.getMessage(),
                request.getDescription(false)
        );
        log.debug("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return badRequest(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError<Error>> methodAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Forbidden",
                ex.getMessage(),
                request.getDescription(false)
        );
        log.debug("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return wrapError(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError<Error>> methodAuthenticationException(AuthenticationException ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Unauthorized",
                ex.getMessage(),
                request.getDescription(false)
        );
        log.debug("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return wrapError(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError<Error>> methodResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Not found",
                ex.getMessage(),
                request.getDescription(false)
        );
        log.debug("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return notFound(error);
    }

    @ExceptionHandler(ActionNotAllowedException.class)
    public ResponseEntity<ApiError<Error>> methodResourceNotFoundException(ActionNotAllowedException ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Action not allowed",
                ex.getMessage(),
                request.getDescription(false)
        );
        log.debug("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return badRequest(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError<Error>> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        Error error = new Error(
                Instant.now(),
                "Constraint violation",
                ex.getMessage(),
                request.getDescription(false)
        );
        log.debug("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return badRequest(error);
    }

}
