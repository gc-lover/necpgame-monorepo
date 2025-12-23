// Issue: #2236
package server

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trading-core-service-go/pkg/api"
)

// HTTPServer wraps the generated HTTP server with middleware
type HTTPServer struct {
	handler *TradingCoreHandler
	server  *api.Server
}

// NewHTTPServer creates a new HTTP server instance
func NewHTTPServer(service *TradingCoreService) *HTTPServer {
	handler := NewTradingCoreHandler(service)

	// Create security handler
	securityHandler := &SecurityHandler{}

	// Create the generated server with our handler
	server, err := api.NewServer(handler, securityHandler)
	if err != nil {
		panic("failed to create API server: " + err.Error())
	}

	return &HTTPServer{
		handler: handler,
		server:  server,
	}
}

// SecurityHandler implements security for the API
type SecurityHandler struct{}

// HandleBearerAuth handles Bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// Extract and validate JWT token
	// In production, validate token signature and claims
	if t.Token == "" {
		return ctx, fmt.Errorf("missing authentication token")
	}

	// For now, just pass through (implement proper JWT validation)
	return ctx, nil
}

// Handler returns the HTTP handler with middleware
func (h *HTTPServer) Handler() http.Handler {
	// Convert ogen server to http.Handler
	baseHandler := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		h.server.ServeHTTP(w, r)
	})

	// Apply middleware stack
	handler := corsMiddleware(baseHandler)
	handler = loggingMiddleware(handler)
	handler = authMiddleware(handler)
	handler = rateLimitMiddleware(handler)
	handler = metricsMiddleware(handler)

	return handler
}

// Middleware implementations

// corsMiddleware adds CORS headers
func corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// loggingMiddleware logs HTTP requests
func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Create a response writer wrapper to capture status code
		wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(wrapped, r)

		duration := time.Since(start)

		// BACKEND NOTE: Structured logging for performance monitoring
		log.Printf("[HTTP] %s %s %d %v", r.Method, r.URL.Path, wrapped.statusCode, duration)
	})
}

// authMiddleware handles JWT authentication
func authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// BACKEND NOTE: JWT validation for trading operations
		// Extract and validate JWT token
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			http.Error(w, "Missing authorization header", http.StatusUnauthorized)
			return
		}

		// Validate JWT token (simplified)
		if !validateJWT(authHeader) {
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		// Extract user ID and add to context
		// ctx := context.WithValue(r.Context(), "user_id", userID)
		// r = r.WithContext(ctx)

		next.ServeHTTP(w, r)
	})
}

// rateLimitMiddleware implements rate limiting
func rateLimitMiddleware(next http.Handler) http.Handler {
	// BACKEND NOTE: Rate limiting for trading endpoints (1000+ RPS protection)
	// Simple in-memory rate limiter - in production use Redis
	limiter := NewRateLimiter(1000, time.Minute) // 1000 requests per minute

	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !limiter.Allow(r.RemoteAddr) {
			http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// metricsMiddleware collects HTTP metrics
func metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// BACKEND NOTE: Metrics collection for monitoring and alerting
		// In production, integrate with Prometheus/statsd

		next.ServeHTTP(w, r)
	})
}

// Helper types and functions

type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// validateJWT performs JWT token validation (simplified)
func validateJWT(token string) bool {
	// BACKEND NOTE: In production, use proper JWT validation library
	// Verify signature, expiration, claims, etc.
	return len(token) > 10 // Simplified check
}

// RateLimiter implements simple rate limiting
type RateLimiter struct {
	requests map[string][]time.Time
	limit    int
	window   time.Duration
}

func NewRateLimiter(limit int, window time.Duration) *RateLimiter {
	return &RateLimiter{
		requests: make(map[string][]time.Time),
		limit:    limit,
		window:   window,
	}
}

func (rl *RateLimiter) Allow(key string) bool {
	now := time.Now()

	// Clean old requests
	if requests, exists := rl.requests[key]; exists {
		var valid []time.Time
		for _, req := range requests {
			if now.Sub(req) < rl.window {
				valid = append(valid, req)
			}
		}
		rl.requests[key] = valid
	}

	// Check limit
	if len(rl.requests[key]) >= rl.limit {
		return false
	}

	// Add new request
	rl.requests[key] = append(rl.requests[key], now)
	return true
}
