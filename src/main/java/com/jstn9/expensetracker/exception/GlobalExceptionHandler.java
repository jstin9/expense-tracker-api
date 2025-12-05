package com.jstn9.expensetracker.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jstn9.expensetracker.dto.exception.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleFieldException(ApiException ex) {

        Map<String, String> errors = new HashMap<>();

        if(ex.getField() != null) {
            errors = Map.of(ex.getField(), ex.getErrorCode().name());
        }

        return buildError(
                ex.getErrorCode().name(),
                ex.getHttpStatus(),
                errors
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String messageCode = error.getDefaultMessage();
            errors.put(field, messageCode);
        });

        return buildError(
                ErrorCode.VALIDATION_FAILED.name(),
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {

        Map<String, String> errors = Map.of("credentials", ErrorCode.BAD_CREDENTIALS.name());

        return buildError(
                ErrorCode.BAD_CREDENTIALS.name(),
                HttpStatus.UNAUTHORIZED,
                errors
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

            errors.put(field, ErrorCode.JSON_INVALID.name());
        } else {
            errors.put("json", ErrorCode.MALFORMED_JSON.name());
        }

        return buildError(
                ErrorCode.JSON_INVALID.name(),
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<ApiError> handleNoResourceFound(NoResourceFoundException ex) {
//
//        String path = ex.getResourcePath();
//
//        Map<String, String> errors = Map.of("path", path);
//
//        return buildError(
//                ErrorCode.RESOURCE_NOT_FOUND.name(),
//                HttpStatus.NOT_FOUND,
//                errors
//        );
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
//        log.error("Unhandled exception", ex);
//
//        return buildError(
//                ErrorCode.INTERNAL_SERVER_ERROR.name(),
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                null
//        );
//    }

    private boolean isWebRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/api/");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFound(NoResourceFoundException ex,
                                                   HttpServletRequest request) {

        if (isWebRequest(request)) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> errors = Map.of("path", ex.getResourcePath());

        return buildError(
                ErrorCode.RESOURCE_NOT_FOUND.name(),
                HttpStatus.NOT_FOUND,
                errors
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex,
                                                 HttpServletRequest request) {

        if (isWebRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info(ex.getMessage(), ex);

        return buildError(
                ErrorCode.INTERNAL_SERVER_ERROR.name(),
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
