package com.necpgame.authservice.service;

import com.necpgame.authservice.model.BlacklistStatusResponse;
import com.necpgame.authservice.model.Error;
import org.springframework.lang.Nullable;
import com.necpgame.authservice.model.RefreshTokenRequest;
import com.necpgame.authservice.model.TokenRefreshResponse;
import com.necpgame.authservice.model.TokenRevokeAllRequest;
import com.necpgame.authservice.model.TokenVerifyRequest;
import com.necpgame.authservice.model.TokenVerifyResponse;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for AuthService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface AuthService {

    /**
     * GET /auth/token/blacklist/{tokenId} : Проверить наличие токена в blacklist
     * Диагностическая ручка для аналитики и поддержки.
     *
     * @param tokenId  (required)
     * @return BlacklistStatusResponse
     */
    BlacklistStatusResponse getBlacklistStatus(String tokenId);

    /**
     * POST /auth/token/refresh : Обновить access token по refresh token
     * Возвращает новый комплект токенов. Лимит 5 запросов за 5 минут на refresh token.
     *
     * @param refreshTokenRequest  (required)
     * @return TokenRefreshResponse
     */
    TokenRefreshResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    /**
     * POST /auth/token/revoke-all : Завершить все сессии пользователя
     * Инвалидирует все refresh tokens пользователя (logout everywhere).
     *
     * @param tokenRevokeAllRequest  (optional)
     * @return Void
     */
    Void revokeAllTokens(TokenRevokeAllRequest tokenRevokeAllRequest);

    /**
     * POST /auth/token/revoke : Отозвать refresh token
     * Добавляет refresh token в blacklist и закрывает связанные access токены.
     *
     * @param refreshTokenRequest  (required)
     * @return Void
     */
    Void revokeToken(RefreshTokenRequest refreshTokenRequest);

    /**
     * POST /auth/token/verify : Проверить валидность токена
     * Используется API Gateway и игровыми сервисами для проверки пользовательских токенов.
     *
     * @param tokenVerifyRequest  (required)
     * @return TokenVerifyResponse
     */
    TokenVerifyResponse verifyToken(TokenVerifyRequest tokenVerifyRequest);
}

