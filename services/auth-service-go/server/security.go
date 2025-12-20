// Package server Issue: #136
package server

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/auth-service-go/pkg/api"
)

// SecurityHandler реализует интерфейс SecurityHandler для JWT аутентификации
type SecurityHandler struct {
	jwtSecret []byte
	logger    *zap.Logger
}

// NewSecurityHandler создает новый security handler

// HandleBearerAuth обрабатывает Bearer токен аутентификации
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	s.logger.Debug("Processing Bearer token authentication", zap.String("operation", operationName))

	// Парсим и валидируем токен
	token, err := jwt.Parse(t.Token, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return s.jwtSecret, nil
	})

	if err != nil || !token.Valid {
		s.logger.Warn("Invalid JWT token", zap.Error(err))
		return ctx, fmt.Errorf("unauthorized")
	}

	// Извлекаем claims
	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		s.logger.Warn("Invalid token claims")
		return ctx, fmt.Errorf("unauthorized")
	}

	// Извлекаем userID
	userIDStr, ok := claims["user_id"].(string)
	if !ok {
		s.logger.Warn("Missing user_id in token")
		return ctx, fmt.Errorf("unauthorized")
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.logger.Warn("Invalid user_id format in token", zap.Error(err))
		return ctx, fmt.Errorf("unauthorized")
	}

	// Проверяем тип токена (access/refresh)
	tokenType, ok := claims["type"].(string)
	if !ok {
		s.logger.Warn("Missing token type in claims")
		return ctx, fmt.Errorf("unauthorized")
	}

	// Добавляем информацию в контекст
	ctx = context.WithValue(ctx, "user_id", userID)
	ctx = context.WithValue(ctx, "token_type", tokenType)

	s.logger.Debug("JWT token validated successfully",
		zap.String("user_id", userID.String()),
		zap.String("token_type", tokenType))

	return ctx, nil
}

// BearerAuth предоставляет токен для клиента (для тестирования)
func (s *SecurityHandler) BearerAuth(ctx context.Context, _ api.OperationName) (api.BearerAuth, error) {
	// Получаем токен из контекста (для тестирования)
	token, ok := ctx.Value("bearer_token").(string)
	if !ok {
		return api.BearerAuth{}, fmt.Errorf("bearer token not found in context")
	}

	return api.BearerAuth{Token: token}, nil
}

// GenerateTestToken генерирует тестовый JWT токен (для тестирования)
func (s *SecurityHandler) GenerateTestToken(userID uuid.UUID, tokenType string) (string, error) {
	var claims jwt.MapClaims
	var expiresAt *jwt.NumericDate

	if tokenType == "access" {
		expiresAt = jwt.NewNumericDate(time.Now().Add(time.Hour))
	} else if tokenType == "refresh" {
		expiresAt = jwt.NewNumericDate(time.Now().Add(7 * 24 * time.Hour))
	} else {
		return "", fmt.Errorf("invalid token type: %s", tokenType)
	}

	claims = jwt.MapClaims{
		"user_id": userID.String(),
		"type":    tokenType,
		"exp":     expiresAt,
		"iat":     jwt.NewNumericDate(time.Now()),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString(s.jwtSecret)
}

// ValidateToken валидирует JWT токен и возвращает userID
func (s *SecurityHandler) ValidateToken(tokenString string) (uuid.UUID, string, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return s.jwtSecret, nil
	})

	if err != nil || !token.Valid {
		return uuid.Nil, "", fmt.Errorf("invalid token: %w", err)
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return uuid.Nil, "", fmt.Errorf("invalid token claims")
	}

	userIDStr, ok := claims["user_id"].(string)
	if !ok {
		return uuid.Nil, "", fmt.Errorf("missing user_id in token")
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		return uuid.Nil, "", fmt.Errorf("invalid user_id format: %w", err)
	}

	tokenType, ok := claims["type"].(string)
	if !ok {
		return uuid.Nil, "", fmt.Errorf("missing token type in token")
	}

	return userID, tokenType, nil
}

// GenerateSecureToken генерирует безопасный токен для email верификации или сброса пароля
func (s *SecurityHandler) GenerateSecureToken() (string, error) {
	bytes := make([]byte, 32)
	if _, err := rand.Read(bytes); err != nil {
		s.logger.Error("Failed to generate secure token", zap.Error(err))
		return "", fmt.Errorf("failed to generate secure token: %w", err)
	}
	return hex.EncodeToString(bytes), nil
}
