// Package server Issue: #146073424
package server

import (
	"context"
	"fmt"
	"net/http"
	"strings"
	"sync"
	"time"

	"go.uber.org/zap"
)

// RateLimiter реализует rate limiting для API Gateway
type RateLimiter struct {
	requestsPerMinute int
	burstLimit        int
	clients           map[string]*clientLimiter
	mutex             sync.RWMutex
}

type clientLimiter struct {
	tokens     int
	lastRefill time.Time
}

// NewRateLimiter создает новый rate limiter
func NewRateLimiter(requestsPerMinute, burstLimit int) *RateLimiter {
	return &RateLimiter{
		requestsPerMinute: requestsPerMinute,
		burstLimit:        burstLimit,
		clients:           make(map[string]*clientLimiter),
	}
}

// Allow проверяет, разрешен ли запрос для клиента
func (rl *RateLimiter) Allow(clientID string) bool {
	rl.mutex.Lock()
	defer rl.mutex.Unlock()

	now := time.Now()
	limiter, exists := rl.clients[clientID]

	if !exists {
		// Новый клиент - создаем limiter
		limiter = &clientLimiter{
			tokens:     rl.burstLimit,
			lastRefill: now,
		}
		rl.clients[clientID] = limiter
		return true
	}

	// Пополняем токены
	timePassed := now.Sub(limiter.lastRefill)
	tokensToAdd := int(timePassed.Minutes() * float64(rl.requestsPerMinute))

	if tokensToAdd > 0 {
		limiter.tokens = min(limiter.tokens+tokensToAdd, rl.burstLimit)
		limiter.lastRefill = now
	}

	// Проверяем доступность токенов
	if limiter.tokens > 0 {
		limiter.tokens--
		return true
	}

	return false
}

// CircuitBreaker реализует circuit breaker pattern
type CircuitBreaker struct {
	state           string // "closed", "open", "half-open"
	failures        int
	lastFailureTime time.Time
	successCount    int
	mutex           sync.RWMutex

	timeout     time.Duration
	maxRequests int
	interval    time.Duration
}

// NewCircuitBreaker создает новый circuit breaker
func NewCircuitBreaker(timeout time.Duration, maxRequests int, interval time.Duration) *CircuitBreaker {
	return &CircuitBreaker{
		state:       "closed",
		timeout:     timeout,
		maxRequests: maxRequests,
		interval:    interval,
	}
}

// CanExecute проверяет, можно ли выполнить запрос
func (cb *CircuitBreaker) CanExecute() bool {
	cb.mutex.RLock()
	defer cb.mutex.RUnlock()

	switch cb.state {
	case "closed":
		return true
	case "open":
		// Проверяем, прошло ли время для перехода в half-open
		if time.Since(cb.lastFailureTime) > cb.interval {
			cb.mutex.RUnlock()
			cb.mutex.Lock()
			cb.state = "half-open"
			cb.successCount = 0
			cb.mutex.Unlock()
			cb.mutex.RLock()
			return true
		}
		return false
	case "half-open":
		return true
	default:
		return false
	}
}

// RecordResult записывает результат выполнения запроса
func (cb *CircuitBreaker) RecordResult(success bool) {
	cb.mutex.Lock()
	defer cb.mutex.Unlock()

	if success {
		switch cb.state {
		case "half-open":
			cb.successCount++
			// Если достигли порога успешных запросов, закрываем circuit
			if cb.successCount >= cb.maxRequests {
				cb.state = "closed"
				cb.failures = 0
			}
		case "closed":
			cb.failures = 0 // Сбрасываем счетчик ошибок при успехе
		}
	} else {
		cb.failures++
		cb.lastFailureTime = time.Now()

		// Переходим в состояние open при превышении порога ошибок
		if cb.failures >= cb.maxRequests {
			cb.state = "open"
		}
	}
}

// loggingMiddleware логирует HTTP запросы
func (g *APIGateway) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Создаем response writer с захватом статуса
		ww := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(ww, r)

		duration := time.Since(start)

		g.logger.Info("HTTP Request",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.String("remote_addr", r.RemoteAddr),
			zap.Int("status", ww.statusCode),
			zap.Duration("duration", duration),
			zap.String("user_agent", r.UserAgent()),
		)
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

// securityHeadersMiddleware добавляет security headers
func (g *APIGateway) securityHeadersMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Security headers
		w.Header().Set("X-Content-Type-Options", "nosniff")
		w.Header().Set("X-Frame-Options", "DENY")
		w.Header().Set("X-XSS-Protection", "1; mode=block")
		w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
		w.Header().Set("Content-Security-Policy", "default-src 'self'")
		w.Header().Set("Referrer-Policy", "strict-origin-when-cross-origin")

		next.ServeHTTP(w, r)
	})
}

// corsMiddleware обрабатывает CORS
func (g *APIGateway) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// CORS headers
		w.Header().Set("Access-Control-Allow-Origin", "*") // В продакшене ограничить
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")
		w.Header().Set("Access-Control-Max-Age", "3600")

		// Preflight request
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// rateLimitMiddleware проверяет rate limits
func (g *APIGateway) rateLimitMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Получаем client ID (IP адрес или user ID из токена)
		clientID := g.getClientID(r)

		if !g.rateLimiter.Allow(clientID) {
			g.logger.Warn("Rate limit exceeded", zap.String("client_id", clientID))
			http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// getClientID получает идентификатор клиента
func (g *APIGateway) getClientID(r *http.Request) string {
	// Сначала пытаемся получить из JWT токена
	if userID := g.getUserIDFromToken(); userID != "" {
		return "user:" + userID
	}

	// Иначе используем IP адрес
	return "ip:" + r.RemoteAddr
}

// authMiddleware проверяет аутентификацию
func (g *APIGateway) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Некоторые endpoints могут быть публичными
		if g.isPublicEndpoint(r.URL.Path) {
			next.ServeHTTP(w, r)
			return
		}

		// Проверяем JWT токен
		userID, err := g.validateJWT(r)
		if err != nil {
			g.logger.Warn("Authentication failed", zap.Error(err))
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Добавляем user ID в контекст
		ctx := context.WithValue(r.Context(), "user_id", userID)
		r = r.WithContext(ctx)

		next.ServeHTTP(w, r)
	})
}

// isPublicEndpoint проверяет, является ли endpoint публичным
func (g *APIGateway) isPublicEndpoint(path string) bool {
	publicPaths := []string{
		"/api/v1/auth/login",
		"/api/v1/auth/register",
		"/api/v1/auth/refresh",
	}

	for _, publicPath := range publicPaths {
		if strings.HasPrefix(path, publicPath) {
			return true
		}
	}

	return false
}

// validateJWT валидирует JWT токен
func (g *APIGateway) validateJWT(r *http.Request) (string, error) {
	authHeader := r.Header.Get("Authorization")
	if authHeader == "" {
		return "", fmt.Errorf("missing authorization header")
	}

	// Проверяем формат Bearer token
	tokenParts := strings.SplitN(authHeader, " ", 2)
	if len(tokenParts) != 2 || tokenParts[0] != "Bearer" {
		return "", fmt.Errorf("invalid authorization header format")
	}

	tokenString := tokenParts[1]

	// Валидируем токен (упрощенная логика)
	// В реальном приложении здесь будет полная валидация JWT
	if tokenString == "" {
		return "", fmt.Errorf("empty token")
	}

	// Для демонстрации возвращаем mock user ID
	// В реальном приложении токен нужно декодировать
	return "user_123", nil
}

// getUserIDFromToken извлекает user ID из токена (упрощенная логика)
func (g *APIGateway) getUserIDFromToken() string {
	// Для демонстрации - в реальном приложении нужно декодировать JWT
	return ""
}

// min возвращает минимальное из двух значений
func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
