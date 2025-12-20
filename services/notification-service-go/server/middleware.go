// Issue: #140874394
package server

import (
	"context"
	"net/http"
	"strings"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"

	"necpgame/services/notification-service-go/pkg/api"
)

// AuthMiddleware представляет middleware для аутентификации и реализует api.SecurityHandler
type AuthMiddleware struct {
	jwtSecret []byte
	logger    *zap.Logger
}

// NewAuthMiddleware создает новый middleware для аутентификации
func NewAuthMiddleware(logger *zap.Logger, jwtSecret string) *AuthMiddleware {
	return &AuthMiddleware{
		logger:    logger,
		jwtSecret: []byte(jwtSecret),
	}
}

// JWTAuth middleware для проверки JWT токенов в заголовке Authorization
func (m *AuthMiddleware) JWTAuth(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Извлекаем токен из заголовка Authorization
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			m.logger.Warn("Missing authorization header")
			http.Error(w, "Missing authorization header", http.StatusUnauthorized)
			return
		}

		// Проверяем формат Bearer token
		tokenParts := strings.SplitN(authHeader, " ", 2)
		if len(tokenParts) != 2 || tokenParts[0] != "Bearer" {
			m.logger.Warn("Invalid authorization header format")
			http.Error(w, "Invalid authorization header format", http.StatusUnauthorized)
			return
		}

		tokenString := tokenParts[1]

		// Валидируем токен
		claims, err := m.validateAccessToken(tokenString)
		if err != nil {
			m.logger.Warn("Invalid token", zap.Error(err))
			http.Error(w, "Invalid token", http.StatusUnauthorized)
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
func (m *AuthMiddleware) validateAccessToken(tokenString string) (*TokenClaims, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		// Проверяем метод подписи
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, jwt.ErrSignatureInvalid
		}
		return m.jwtSecret, nil
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

	// Проверяем обязательные поля
	userID, ok := claims["user_id"].(string)
	if !ok {
		return nil, jwt.ErrTokenInvalidClaims
	}

	role, ok := claims["role"].(string)
	if !ok {
		return nil, jwt.ErrTokenInvalidClaims
	}

	email, _ := claims["email"].(string) // опционально

	// Проверяем expiration
	exp, ok := claims["exp"].(float64)
	if !ok {
		return nil, jwt.ErrTokenExpired
	}

	expiresAt := time.Unix(int64(exp), 0)
	if time.Now().After(expiresAt) {
		return nil, jwt.ErrTokenExpired
	}

	return &TokenClaims{
		UserID:    userID,
		Role:      role,
		Email:     email,
		ExpiresAt: expiresAt,
	}, nil
}

// CORSMiddleware добавляет CORS заголовки
func (m *AuthMiddleware) CORSMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// CORS headers для веб-клиентов
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		// Preflight запросы
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// LoggingMiddleware логирует HTTP запросы
func (m *AuthMiddleware) LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Логируем входящий запрос
		m.logger.Info("HTTP Request",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.String("remote_addr", r.RemoteAddr),
			zap.String("user_agent", r.UserAgent()),
		)

		// Создаем response writer для захвата статуса
		rw := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		// Выполняем запрос
		next.ServeHTTP(rw, r)

		// Логируем завершение
		duration := time.Since(start)
		m.logger.Info("HTTP Response",
			zap.Int("status", rw.statusCode),
			zap.Duration("duration", duration),
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
		)
	})
}

// RecoveryMiddleware перехватывает паники
func (m *AuthMiddleware) RecoveryMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if err := recover(); err != nil {
				m.logger.Error("Panic recovered",
					zap.Any("error", err),
					zap.String("method", r.Method),
					zap.String("path", r.URL.Path),
				)
				http.Error(w, "Internal server error", http.StatusInternalServerError)
			}
		}()

		next.ServeHTTP(w, r)
	})
}

// SecurityHeadersMiddleware добавляет security headers
func (m *AuthMiddleware) SecurityHeadersMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Security headers
		w.Header().Set("X-Content-Type-Options", "nosniff")
		w.Header().Set("X-Frame-Options", "DENY")
		w.Header().Set("X-XSS-Protection", "1; mode=block")
		w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")

		next.ServeHTTP(w, r)
	})
}

// responseWriter оборачивает ResponseWriter для захвата статуса
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// TokenClaims представляет claims токена
type TokenClaims struct {
	UserID    string
	Email     string
	Role      string
	ExpiresAt time.Time
}

// ProfilingAuth middleware для ограничения доступа к profiling endpoints
func (m *AuthMiddleware) ProfilingAuth(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// В production проверять специальные права на profiling
		// Для development разрешаем с любым Bearer токеном
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			m.logger.Warn("Missing authorization for profiling endpoint")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Проверяем Bearer token
		tokenParts := strings.SplitN(authHeader, " ", 2)
		if len(tokenParts) != 2 || tokenParts[0] != "Bearer" {
			m.logger.Warn("Invalid authorization header format for profiling")
			http.Error(w, "Invalid authorization format", http.StatusUnauthorized)
			return
		}

		// Валидируем токен (базовая проверка)
		_, err := m.validateAccessToken(tokenParts[1])
		if err != nil {
			m.logger.Warn("Invalid token for profiling access", zap.Error(err))
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// HandleBearerAuth реализует SecurityHandler интерфейс для ogen
func (m *AuthMiddleware) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Валидируем токен
	claims, err := m.validateAccessToken(t.Token)
	if err != nil {
		m.logger.Warn("Invalid Bearer token",
			zap.String("operation", string(operationName)),
			zap.Error(err))
		return ctx, err
	}

	// Добавляем информацию о пользователе в контекст
	ctx = context.WithValue(ctx, "user_id", claims.UserID)
	ctx = context.WithValue(ctx, "user_role", claims.Role)
	ctx = context.WithValue(ctx, "user_email", claims.Email)

	m.logger.Debug("Bearer auth successful",
		zap.String("operation", string(operationName)),
		zap.String("user_id", claims.UserID))

	return ctx, nil
}
