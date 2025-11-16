package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.exception.BusinessException;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.service.exception.LockUnavailableException;
import com.necpgame.workqueue.service.exception.VersionConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ErrorResponse> handleApiError(ApiErrorException ex) {
        var details = ex.getDetails().stream()
                .map(detail -> new ErrorResponse.Detail(detail.path(), detail.reason()))
                .toList();
        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponse.withDetails(ex.getCode(), ex.getMessage(), ex.getRequirements(), details));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of("not_found", ex.getMessage()));
    }

    @ExceptionHandler({LockUnavailableException.class, VersionConflictException.class})
    public ResponseEntity<ErrorResponse> handleConflict(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of("conflict", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream().findFirst().map(error -> error.getDefaultMessage()).orElse("Validation error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of("validation_error", message));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of("business_error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of("bad_request", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of("internal_error", ex.getMessage()));
    }
}

