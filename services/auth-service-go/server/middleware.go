// Package server Issue: #136
package server

import (
	"context"
	"fmt"
	"net/http"
	"strings"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// AuthMiddleware предоставляет middleware для аутентификации
type AuthMiddleware struct {
	jwtSecret []byte
	logger    *zap.Logger
}

// NewAuthMiddleware создает новый middleware аутентификации

// Authenticate аутентифицирует запрос по JWT токену
func (m *AuthMiddleware) Authenticate(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Извлекаем токен из заголовка Authorization
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			m.logger.Warn("Missing Authorization header")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Проверяем формат Bearer token
		parts := strings.SplitN(authHeader, " ", 2)
		if len(parts) != 2 || parts[0] != "Bearer" {
			m.logger.Warn("Invalid Authorization header format")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		tokenString := parts[1]

		// Парсим и валидируем токен
		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
			}
			return m.jwtSecret, nil
		})

		if err != nil || !token.Valid {
			m.logger.Warn("Invalid JWT token", zap.Error(err))
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Извлекаем claims
		claims, ok := token.Claims.(jwt.MapClaims)
		if !ok {
			m.logger.Warn("Invalid token claims")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Извлекаем userID
		userIDStr, ok := claims["user_id"].(string)
		if !ok {
			m.logger.Warn("Missing user_id in token")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		userID, err := uuid.Parse(userIDStr)
		if err != nil {
			m.logger.Warn("Invalid user_id format in token", zap.Error(err))
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Добавляем userID в контекст
		ctx := context.WithValue(r.Context(), "user_id", userID)
		ctx = context.WithValue(ctx, "token_type", claims["type"])

		// Продолжаем выполнение с обновленным контекстом
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// OptionalAuth делает аутентификацию опциональной
func (m *AuthMiddleware) OptionalAuth(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			// Нет токена - продолжаем без аутентификации
			next.ServeHTTP(w, r)
			return
		}

		// Проверяем формат Bearer token
		parts := strings.SplitN(authHeader, " ", 2)
		if len(parts) != 2 || parts[0] != "Bearer" {
			// Неправильный формат - продолжаем без аутентификации
			next.ServeHTTP(w, r)
			return
		}

		tokenString := parts[1]

		// Парсим токен
		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
			}
			return m.jwtSecret, nil
		})

		ctx := r.Context()

		if err == nil && token.Valid {
			// Токен валиден - добавляем информацию в контекст
			if claims, ok := token.Claims.(jwt.MapClaims); ok {
				if userIDStr, ok := claims["user_id"].(string); ok {
					if userID, err := uuid.Parse(userIDStr); err == nil {
						ctx = context.WithValue(ctx, "user_id", userID)
						ctx = context.WithValue(ctx, "token_type", claims["type"])
					}
				}
			}
		}

		// Продолжаем выполнение
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// GetUserIDFromContext извлекает userID из контекста

// GetTokenTypeFromContext извлекает тип токена из контекста
