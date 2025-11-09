package com.necpgame.backjava.exception;

import com.necpgame.backjava.model.MaintenanceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class MaintenanceExceptionHandler {

    @ExceptionHandler(MaintenanceException.class)
    public ResponseEntity<MaintenanceError> handleMaintenanceException(MaintenanceException exception) {
        MaintenanceError body = new MaintenanceError()
            .code(exception.getCode())
            .message(exception.getMessage())
            .details(exception.getDetails());
        log.warn("Maintenance error [{}]: {}", exception.getCode().getValue(), exception.getMessage());
        return ResponseEntity.status(exception.getStatus()).body(body);
    }
}





