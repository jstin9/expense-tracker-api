package com.jstn9.expensetracker.exception;

import com.jstn9.expensetracker.dto.exception.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Ошибки валидации (DTO @Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        ApiError apiError = new ApiError(
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.badRequest().body(apiError);
    }


    /**
     * Ошибки, у которых есть своё поле (UsernameExists, EmailExists)
     */
    @ExceptionHandler(FieldException.class)
    public ResponseEntity<ApiError> handleFieldException(FieldException ex) {

        Map<String, String> errors = Map.of(
                ex.getField(), ex.getMessage()
        );

        ApiError apiError = new ApiError(
                "Validation failed",
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                errors
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }


    /**
     * Ошибка "неправильный логин"
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return buildSimpleError(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }


    /**
     * Остальные бизнес-ошибки, без поля
     */
    @ExceptionHandler({
            CategoryNotFoundException.class,
            CategoryAlreadyExistsException.class,
    })
    public ResponseEntity<ApiError> handleBusinessExceptions(RuntimeException ex) {
        return buildSimpleError(ex.getMessage(), HttpStatus.CONFLICT);
    }


    /**
     * Любая ошибка, которую мы не обработали
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception", ex);
        return buildSimpleError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Вспомогательный метод для простого ApiError без errors{}
     */
    private ResponseEntity<ApiError> buildSimpleError(String message, HttpStatus status) {
        ApiError apiError = new ApiError(
                message,
                status.value(),
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(apiError, status);
    }
}
