// Package server Issue: #140875381
package server

import (
	"context"
	"net/http"
	"strings"
	"sync"
	"time"

	"github.com/lestrrat-go/jwx/v2/jwt"
	"go.uber.org/zap"
)

// AuthMiddleware предоставляет middleware для аутентификации
type AuthMiddleware struct {
	jwtSecret []byte
	logger    *zap.Logger
}

// NewAuthMiddleware создает новый middleware для аутентификации
func NewAuthMiddleware(jwtSecret []byte, logger *zap.Logger) *AuthMiddleware {
	return &AuthMiddleware{
		jwtSecret: jwtSecret,
		logger:    logger,
	}
}

// Authenticate проверяет JWT токен в заголовке Authorization
func (m *AuthMiddleware) Authenticate(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Пропускаем аутентификацию для health check и некоторых публичных эндпоинтов
		if m.isPublicEndpoint(r.URL.Path) {
			next.ServeHTTP(w, r)
			return
		}

		// Извлекаем токен из заголовка
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			m.logger.Warn("Missing authorization header", zap.String("path", r.URL.Path))
			http.Error(w, "Missing authorization header", http.StatusUnauthorized)
			return
		}

		// Проверяем формат Bearer token
		if !strings.HasPrefix(authHeader, "Bearer ") {
			m.logger.Warn("Invalid authorization header format", zap.String("path", r.URL.Path))
			http.Error(w, "Invalid authorization header format", http.StatusUnauthorized)
			return
		}

		tokenString := strings.TrimPrefix(authHeader, "Bearer ")

		// Парсим и валидируем токен
		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			// Проверяем метод подписи
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				return nil, jwt.ErrSignatureInvalid
			}
			return m.jwtSecret, nil
		})

		if err != nil {
			m.logger.Warn("Invalid JWT token", zap.Error(err), zap.String("path", r.URL.Path))
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		if !token.Valid {
			m.logger.Warn("Token is not valid", zap.String("path", r.URL.Path))
			http.Error(w, "Token is not valid", http.StatusUnauthorized)
			return
		}

		// Извлекаем claims
		claims, ok := token.Claims.(jwt.MapClaims)
		if !ok {
			m.logger.Warn("Invalid token claims", zap.String("path", r.URL.Path))
			http.Error(w, "Invalid token claims", http.StatusUnauthorized)
			return
		}

		// Проверяем срок действия токена
		if exp, ok := claims["exp"].(float64); ok {
			if time.Now().Unix() > int64(exp) {
				m.logger.Warn("Token expired", zap.String("path", r.URL.Path))
				http.Error(w, "Token expired", http.StatusUnauthorized)
				return
			}
		}

		// Добавляем информацию о пользователе в контекст
		userID, _ := claims["user_id"].(string)
		username, _ := claims["username"].(string)
		role, _ := claims["role"].(string)

		ctx := context.WithValue(r.Context(), "user_id", userID)
		ctx = context.WithValue(ctx, "username", username)
		ctx = context.WithValue(ctx, "role", role)

		// Логируем успешную аутентификацию
		m.logger.Debug("User authenticated",
			zap.String("user_id", userID),
			zap.String("username", username),
			zap.String("path", r.URL.Path),
			zap.String("method", r.Method))

		// Передаем запрос дальше с обновленным контекстом
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// isPublicEndpoint проверяет, является ли эндпоинт публичным (не требует аутентификации)
func (m *AuthMiddleware) isPublicEndpoint(path string) bool {
	publicEndpoints := []string{
		"/health",
		"/metrics",
		"/api/v1/cities/regions", // публичная информация о регионах
	}

	for _, endpoint := range publicEndpoints {
		if path == endpoint {
			return true
		}
	}

	return false
}

// RequireRole проверяет, что пользователь имеет требуемую роль
func (m *AuthMiddleware) RequireRole(requiredRole string) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			role, ok := r.Context().Value("role").(string)
			if !ok {
				m.logger.Warn("Role not found in context", zap.String("path", r.URL.Path))
				http.Error(w, "Role not found", http.StatusForbidden)
				return
			}

			if role != requiredRole && role != "admin" {
				m.logger.Warn("Insufficient permissions",
					zap.String("required_role", requiredRole),
					zap.String("user_role", role),
					zap.String("path", r.URL.Path))
				http.Error(w, "Insufficient permissions", http.StatusForbidden)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// RateLimitMiddleware ограничивает количество запросов от пользователя
func (m *AuthMiddleware) RateLimitMiddleware(rps float64) func(http.Handler) http.Handler {
	// Простая реализация rate limiting (в продакшене лучше использовать redis)
	type clientInfo struct {
		lastRequest  time.Time
		requestCount int
	}

	clients := make(map[string]*clientInfo)
	var mu sync.RWMutex

	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			userID, ok := r.Context().Value("user_id").(string)
			if !ok {
				userID = r.RemoteAddr // fallback для публичных эндпоинтов
			}

			mu.Lock()
			client, exists := clients[userID]
			now := time.Now()

			if !exists {
				clients[userID] = &clientInfo{
					lastRequest:  now,
					requestCount: 1,
				}
				mu.Unlock()
				next.ServeHTTP(w, r)
				return
			}

			// Сбрасываем счетчик если прошло больше секунды
			if now.Sub(client.lastRequest) > time.Second {
				client.lastRequest = now
				client.requestCount = 1
				mu.Unlock()
				next.ServeHTTP(w, r)
				return
			}

			// Проверяем лимит
			if float64(client.requestCount) >= rps {
				mu.Unlock()
				m.logger.Warn("Rate limit exceeded",
					zap.String("user_id", userID),
					zap.String("path", r.URL.Path))
				http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
				return
			}

			client.requestCount++
			mu.Unlock()

			next.ServeHTTP(w, r)
		})
	}
}

// LoggingMiddleware логирует все запросы
func (m *AuthMiddleware) LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Создаем response writer для захвата статуса
		rw := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(rw, r)

		duration := time.Since(start)

		userID, _ := r.Context().Value("user_id").(string)
		username, _ := r.Context().Value("username").(string)

		m.logger.Info("Request completed",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.Int("status", rw.statusCode),
			zap.Duration("duration", duration),
			zap.String("user_id", userID),
			zap.String("username", username),
			zap.String("remote_addr", r.RemoteAddr))
	})
}

// responseWriter обертка для захвата статуса ответа
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}
