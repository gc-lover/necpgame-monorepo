package com.necpgame.workqueue.web;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        OffsetDateTime timestamp,
        List<String> requirements,
        List<Detail> details
) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, OffsetDateTime.now(), List.of(), List.of());
    }

    public static ErrorResponse withDetails(String code, String message, List<String> requirements, List<Detail> details) {
        List<String> safeRequirements = requirements == null ? List.of() : List.copyOf(requirements);
        List<Detail> safeDetails = details == null ? List.of() : List.copyOf(details);
        return new ErrorResponse(code, message, OffsetDateTime.now(), safeRequirements, safeDetails);
    }

    public record Detail(
            String path,
            String reason
    ) {
    }
}


