package com.necpgame.authservice.service;

import com.necpgame.authservice.model.Error;
import com.necpgame.authservice.model.LoginRequest;
import com.necpgame.authservice.model.LoginResponse;
import com.necpgame.authservice.model.Register201Response;
import com.necpgame.authservice.model.RegisterRequest;
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
     * POST /auth/login : Вход в систему
     * Аутентификация игрока по email или username и паролю. Возвращает JWT токен.
     *
     * @param loginRequest  (required)
     * @return LoginResponse
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * POST /auth/register : Регистрация нового аккаунта
     * Создает новый аккаунт игрока. Проверяет уникальность email и username.
     *
     * @param registerRequest  (required)
     * @return Register201Response
     */
    Register201Response register(RegisterRequest registerRequest);
}

