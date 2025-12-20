// Issue: #146073424
package server

import (
	"net"
	"net/http"
	"strings"
	"sync"
	"time"

	"go.uber.org/zap"
)

// RateLimiter реализует rate limiting для защиты от DDoS
type RateLimiter struct {
	mu       sync.RWMutex
	clients  map[string]*ClientRateLimit
	rpmLimit int
	window   time.Duration
	logger   *zap.Logger
}

// ClientRateLimit содержит информацию о rate limit для конкретного клиента
type ClientRateLimit struct {
	requests   []time.Time
	blocked    bool
	blockUntil time.Time
}

// NewRateLimiter создает новый rate limiter
func NewRateLimiter(rpmLimit int) *RateLimiter {
	return &RateLimiter{
		clients:  make(map[string]*ClientRateLimit),
		rpmLimit: rpmLimit,
		window:   time.Minute,
		logger:   zap.L().Named("rate_limiter"),
	}
}

// Middleware возвращает HTTP middleware для rate limiting
func (rl *RateLimiter) Middleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		clientIP := rl.getClientIP(r)

		if rl.isBlocked(clientIP) {
			rl.logger.Warn("Request blocked by rate limiter",
				zap.String("client_ip", clientIP))
			http.Error(w, "Too many requests", http.StatusTooManyRequests)
			return
		}

		if !rl.allowRequest(clientIP) {
			rl.blockClient(clientIP)
			rl.logger.Warn("Client blocked due to rate limit violation",
				zap.String("client_ip", clientIP))
			http.Error(w, "Too many requests", http.StatusTooManyRequests)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// getClientIP извлекает IP адрес клиента
func (rl *RateLimiter) getClientIP(r *http.Request) string {
	// Проверяем X-Forwarded-For заголовок (для прокси)
	xff := r.Header.Get("X-Forwarded-For")
	if xff != "" {
		// Берем первый IP из списка
		if ip := net.ParseIP(strings.Split(xff, ",")[0]); ip != nil {
			return ip.String()
		}
	}

	// Проверяем X-Real-IP заголовок
	xri := r.Header.Get("X-Real-IP")
	if xri != "" {
		if ip := net.ParseIP(xri); ip != nil {
			return ip.String()
		}
	}

	// Используем RemoteAddr
	host, _, err := net.SplitHostPort(r.RemoteAddr)
	if err != nil {
		return r.RemoteAddr
	}
	return host
}

// allowRequest проверяет, разрешен ли запрос для данного клиента
func (rl *RateLimiter) allowRequest(clientIP string) bool {
	rl.mu.Lock()
	defer rl.mu.Unlock()

	now := time.Now()
	client, exists := rl.clients[clientIP]

	if !exists {
		client = &ClientRateLimit{
			requests: []time.Time{now},
		}
		rl.clients[clientIP] = client
		return true
	}

	// Очищаем старые запросы вне окна
	validRequests := make([]time.Time, 0)
	for _, reqTime := range client.requests {
		if now.Sub(reqTime) < rl.window {
			validRequests = append(validRequests, reqTime)
		}
	}

	// Проверяем лимит
	if len(validRequests) >= rl.rpmLimit {
		return false
	}

	// Добавляем новый запрос
	client.requests = append(validRequests, now)
	return true
}

// isBlocked проверяет, заблокирован ли клиент
func (rl *RateLimiter) isBlocked(clientIP string) bool {
	rl.mu.RLock()
	defer rl.mu.RUnlock()

	client, exists := rl.clients[clientIP]
	if !exists {
		return false
	}

	return client.blocked && time.Now().Before(client.blockUntil)
}

// blockClient блокирует клиента на определенное время
func (rl *RateLimiter) blockClient(clientIP string) {
	rl.mu.Lock()
	defer rl.mu.Unlock()

	client := rl.clients[clientIP]
	client.blocked = true
	client.blockUntil = time.Now().Add(15 * time.Minute) // 15 минут блокировки

	rl.logger.Warn("Client blocked for rate limit violation",
		zap.String("client_ip", clientIP),
		zap.Time("blocked_until", client.blockUntil))
}

// Cleanup удаляет старые записи клиентов (запускается периодически)
func (rl *RateLimiter) Cleanup() {
	rl.mu.Lock()
	defer rl.mu.Unlock()

	now := time.Now()
	cutoff := now.Add(-rl.window * 2) // Удаляем записи старше 2 окон

	for clientIP, client := range rl.clients {
		// Фильтруем старые запросы
		validRequests := make([]time.Time, 0)
		for _, reqTime := range client.requests {
			if reqTime.After(cutoff) {
				validRequests = append(validRequests, reqTime)
			}
		}
		client.requests = validRequests

		// Удаляем клиентов без активных запросов и без блокировки
		if len(validRequests) == 0 && (!client.blocked || now.After(client.blockUntil)) {
			delete(rl.clients, clientIP)
		}
	}

	rl.logger.Debug("Rate limiter cleanup completed",
		zap.Int("active_clients", len(rl.clients)))
}
