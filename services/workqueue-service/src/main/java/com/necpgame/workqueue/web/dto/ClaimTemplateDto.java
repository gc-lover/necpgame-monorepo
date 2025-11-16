package com.necpgame.workqueue.web.dto;

public record ClaimTemplateDto(
        String code,
        String type,
        String title,
        String version,
        String sourcePath,
        String body
) {
}

