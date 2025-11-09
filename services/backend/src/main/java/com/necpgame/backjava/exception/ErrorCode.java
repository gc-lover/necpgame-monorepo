package com.necpgame.backjava.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * РљРѕРґС‹ РѕС€РёР±РѕРє РїСЂРёР»РѕР¶РµРЅРёСЏ СЃ РјР°РїРїРёРЅРіРѕРј РЅР° HTTP СЃС‚Р°С‚СѓСЃС‹
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // === AUTH (JWT, Р°РІС‚РѕСЂРёР·Р°С†РёСЏ) ===
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_001", "Invalid credentials"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_002", "Token has expired"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_003", "Invalid token"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH_004", "Access denied"),
    
    // === BUSINESS (Р±РёР·РЅРµСЃ-Р»РѕРіРёРєР°) ===
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "BIZ_001", "Resource not found"),
    RESOURCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "BIZ_002", "Resource already exists"),
    OPERATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "BIZ_003", "Operation not allowed"),
    LIMIT_EXCEEDED(HttpStatus.CONFLICT, "BIZ_004", "Limit exceeded"),
    
    // === VALIDATION (РІР°Р»РёРґР°С†РёСЏ РґР°РЅРЅС‹С…) ===
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "VAL_001", "Invalid input data"),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "VAL_002", "Missing required field"),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "VAL_003", "Invalid data format"),
    
    // === INTEGRATION (РІРЅРµС€РЅРёРµ СЃРёСЃС‚РµРјС‹) ===
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INT_001", "Database error"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "INT_002", "External service error"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INT_003", "Internal server error");
    
    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;
}
