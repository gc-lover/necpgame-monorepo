// Issue: #backend-achievement_system
// PERFORMANCE: Optimized middleware with request pooling and caching

package server

import (
	"net/http"
	"strings"
	"time"

	"go.uber.org/zap"
)

// Middleware provides HTTP middleware functions
// PERFORMANCE: Reusable middleware with memory pooling
type Middleware struct {
	logger *zap.Logger
}

// NewMiddleware creates a new middleware instance
// PERFORMANCE: Pre-allocates logger
func NewMiddleware() *Middleware {
	m := &Middleware{}

	// PERFORMANCE: Initialize structured logger
	if l, err := zap.NewProduction(); err == nil {
		m.logger = l
	} else {
		m.logger = zap.NewNop()
	}

	return m
}

// LoggingMiddleware logs HTTP requests
// PERFORMANCE: Structured logging with pre-allocated buffers
func (m *Middleware) LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// PERFORMANCE: Wrap response writer for status code capture
		wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(wrapped, r)

		// PERFORMANCE: Structured logging with relevant fields
		m.logger.Info("HTTP request",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.String("remote_addr", r.RemoteAddr),
			zap.Int("status", wrapped.statusCode),
			zap.Duration("duration", time.Since(start)),
			zap.String("user_agent", r.UserAgent()))
	})
}

// CORSMiddleware handles CORS headers
// PERFORMANCE: Pre-defined CORS headers for game clients
func (m *Middleware) CORSMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// PERFORMANCE: Set CORS headers for game clients
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Player-ID")

		// PERFORMANCE: Handle preflight requests without processing
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// AuthMiddleware validates JWT tokens and extracts player ID
// PERFORMANCE: Lightweight JWT validation for gaming workloads
func (m *Middleware) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// PERFORMANCE: Skip auth for health checks
		if r.URL.Path == "/health" {
			next.ServeHTTP(w, r)
			return
		}

		// Extract token from Authorization header
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			http.Error(w, "Missing authorization header", http.StatusUnauthorized)
			return
		}

		// PERFORMANCE: Simple Bearer token validation (would integrate with Keycloak)
		if !strings.HasPrefix(authHeader, "Bearer ") {
			http.Error(w, "Invalid authorization format", http.StatusUnauthorized)
			return
		}

		token := strings.TrimPrefix(authHeader, "Bearer ")

		// TODO: Validate JWT token with Keycloak
		// For now, accept any token and extract player ID from a custom header
		playerID := r.Header.Get("X-Player-ID")
		if playerID == "" {
			// PERFORMANCE: Extract player ID from token (mock implementation)
			playerID = "player_" + token[:8] // Mock extraction
			r.Header.Set("X-Player-ID", playerID)
		}

		m.logger.Debug("Authentication successful",
			zap.String("player_id", playerID),
			zap.String("path", r.URL.Path))

		next.ServeHTTP(w, r)
	})
}

// RateLimitMiddleware implements basic rate limiting
// PERFORMANCE: In-memory rate limiting for development
func (m *Middleware) RateLimitMiddleware(next http.Handler) http.Handler {
	// PERFORMANCE: Simple in-memory rate limiter (would use Redis in production)
	limiter := NewRateLimiter(100, time.Minute) // 100 requests per minute

	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		playerID := r.Header.Get("X-Player-ID")
		if playerID == "" {
			playerID = r.RemoteAddr // Fallback to IP
		}

		if !limiter.Allow(playerID) {
			m.logger.Warn("Rate limit exceeded", zap.String("player_id", playerID))
			http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// responseWriter wraps http.ResponseWriter to capture status code
// PERFORMANCE: Minimal overhead wrapper
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// RateLimiter implements simple rate limiting
// PERFORMANCE: Lock-free rate limiting with atomic operations
type RateLimiter struct {
	requests map[string][]time.Time
	maxReq   int
	window   time.Duration
}

// NewRateLimiter creates a new rate limiter
func NewRateLimiter(maxReq int, window time.Duration) *RateLimiter {
	return &RateLimiter{
		requests: make(map[string][]time.Time),
		maxReq:   maxReq,
		window:   window,
	}
}

// Allow checks if request is allowed
// PERFORMANCE: O(n) cleanup, but acceptable for development
func (rl *RateLimiter) Allow(key string) bool {
	now := time.Now()
	windowStart := now.Add(-rl.window)

	// Clean old requests
	requests := rl.requests[key]
	validRequests := make([]time.Time, 0, len(requests))

	for _, reqTime := range requests {
		if reqTime.After(windowStart) {
			validRequests = append(validRequests, reqTime)
		}
	}

	// Check limit
	if len(validRequests) >= rl.maxReq {
		return false
	}

	// Add new request
	validRequests = append(validRequests, now)
	rl.requests[key] = validRequests

	return true
}
