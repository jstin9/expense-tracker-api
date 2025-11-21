package com.jstn9.expensetracker.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jstn9.expensetracker.dto.exception.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return buildError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                errors
        );
    }


    @ExceptionHandler(FieldException.class)
    public ResponseEntity<ApiError> handleFieldException(FieldException ex) {

        Map<String, String> errors = Map.of(
                ex.getField(), ex.getMessage()
        );

        return buildError(
                "Validation failed",
                HttpStatus.CONFLICT,
                errors
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return buildError(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                null
        );
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleJsonParsing(HttpMessageNotReadableException ex) {

        Map<String, String> errors = new HashMap<>();

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {
            String field = ife.getPath().isEmpty()
                    ? "unknown"
                    : ife.getPath().get(0).getFieldName();

            errors.put(field, "Invalid value: " + ife.getValue());
        } else {
            errors.put("json", "Malformed JSON input");
        }

        return buildError(
                "Invalid request",
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception", ex);

        return buildError(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        );
    }

    private ResponseEntity<ApiError> buildError(
            String message,
            HttpStatus status,
            Map<String, String> errors
    ) {
        ApiError apiError = new ApiError(
                message,
                status.value(),
                LocalDateTime.now(),
                errors == null || errors.isEmpty() ? null : errors
        );

        return new ResponseEntity<>(apiError, status);
    }
}
