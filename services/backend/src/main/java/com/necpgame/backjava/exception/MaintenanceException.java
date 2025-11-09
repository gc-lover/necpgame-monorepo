package com.necpgame.backjava.exception;

import com.necpgame.backjava.model.MaintenanceError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class MaintenanceException extends RuntimeException {

    private final MaintenanceError.CodeEnum code;
    private final HttpStatus status;
    private final Map<String, Object> details;

    public MaintenanceException(MaintenanceError.CodeEnum code, HttpStatus status, String message, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.status = status;
        this.details = details;
    }
}





