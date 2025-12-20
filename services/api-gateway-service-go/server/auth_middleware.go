// Issue: #146073424
package server

import (
	"context"
	"net/http"
	"strings"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"
)

// AuthMiddleware предоставляет аутентификацию для API Gateway
type AuthMiddleware struct {
	jwtSecret []byte
	logger    *zap.Logger
}

// NewAuthMiddleware создает новый auth middleware
func NewAuthMiddleware(jwtSecret string, logger *zap.Logger) *AuthMiddleware {
	return &AuthMiddleware{
		jwtSecret: []byte(jwtSecret),
		logger:    logger,
	}
}

// JWTAuth middleware для проверки JWT токенов
func (am *AuthMiddleware) JWTAuth(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Извлекаем токен из заголовка Authorization
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			am.logger.Warn("Missing authorization header",
				zap.String("path", r.URL.Path),
				zap.String("method", r.Method))
			http.Error(w, `{"error": "Unauthorized", "message": "Missing authorization header"}`, http.StatusUnauthorized)
			return
		}

		// Проверяем формат Bearer token
		tokenParts := strings.SplitN(authHeader, " ", 2)
		if len(tokenParts) != 2 || tokenParts[0] != "Bearer" {
			am.logger.Warn("Invalid authorization header format",
				zap.String("path", r.URL.Path),
				zap.String("header", authHeader))
			http.Error(w, `{"error": "Unauthorized", "message": "Invalid authorization header format"}`, http.StatusUnauthorized)
			return
		}

		tokenString := tokenParts[1]

		// Валидируем токен
		claims, err := am.validateAccessToken(tokenString)
		if err != nil {
			am.logger.Warn("Invalid token",
				zap.String("path", r.URL.Path),
				zap.Error(err))
			http.Error(w, `{"error": "Unauthorized", "message": "Invalid token"}`, http.StatusUnauthorized)
			return
		}

		// Добавляем информацию о пользователе в контекст
		ctx := context.WithValue(r.Context(), "user_id", claims.UserID)
		ctx = context.WithValue(ctx, "user_role", claims.Role)
		ctx = context.WithValue(ctx, "user_email", claims.Email)

		// Передаем запрос дальше с обновленным контекстом
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// validateAccessToken валидирует access токен
func (am *AuthMiddleware) validateAccessToken(tokenString string) (*TokenClaims, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		// Проверяем метод подписи
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, jwt.ErrSignatureInvalid
		}
		return am.jwtSecret, nil
	})

	if err != nil {
		return nil, err
	}

	// Проверяем валидность токена
	if !token.Valid {
		return nil, jwt.ErrTokenNotValidYet
	}

	// Извлекаем claims
	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return nil, jwt.ErrTokenInvalidClaims
	}

	// Проверяем тип токена
	tokenType, ok := claims["type"].(string)
	if !ok || tokenType != "access" {
		return nil, jwt.ErrTokenInvalidClaims
	}

	// Извлекаем обязательные поля
	userID, ok := claims["user_id"].(string)
	if !ok {
		return nil, jwt.ErrTokenInvalidClaims
	}

	role, _ := claims["role"].(string)
	email, _ := claims["email"].(string)

	// Проверяем срок действия
	exp, ok := claims["exp"].(float64)
	if !ok {
		return nil, jwt.ErrTokenInvalidClaims
	}

	if time.Now().Unix() > int64(exp) {
		return nil, jwt.ErrTokenExpired
	}

	return &TokenClaims{
		UserID:    userID,
		Role:      role,
		Email:     email,
		ExpiresAt: time.Unix(int64(exp), 0),
	}, nil
}

// TokenClaims представляет claims токена
type TokenClaims struct {
	UserID    string
	Role      string
	Email     string
	ExpiresAt time.Time
}
