package com.necpgame.workqueue.service.exception;

public record ApiErrorDetail(
        String path,
        String reason
) {
}

