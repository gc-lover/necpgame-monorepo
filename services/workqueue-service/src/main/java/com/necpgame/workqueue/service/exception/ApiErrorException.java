package com.necpgame.workqueue.service.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class ApiErrorException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    private final List<String> requirements;
    private final List<ApiErrorDetail> details;

    public ApiErrorException(HttpStatus status, String code, String message, List<String> requirements, List<ApiErrorDetail> details) {
        super(message);
        this.status = status;
        this.code = code;
        this.requirements = requirements == null ? List.of() : List.copyOf(requirements);
        this.details = details == null ? List.of() : List.copyOf(details);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public List<String> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

    public List<ApiErrorDetail> getDetails() {
        return Collections.unmodifiableList(details);
    }
}

