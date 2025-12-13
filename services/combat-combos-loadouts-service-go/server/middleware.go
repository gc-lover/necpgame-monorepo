// Combat Combos Loadouts Service Middleware
// Issue: #141890005

package server

import (
	"context"
	"net/http"
	"strings"
	"time"

	"github.com/go-chi/jwtauth/v5"
	"github.com/lestrrat-go/jwx/v2/jwt"
	"github.com/rs/zerolog/log"
)

// setupAuthMiddleware configures JWT authentication middleware
func setupAuthMiddleware(cfg *Config) func(http.Handler) http.Handler {
	tokenAuth := jwtauth.New("HS256", []byte(cfg.JWTSecret), nil)

	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// Skip auth for health checks
			if r.URL.Path == "/health" || r.URL.Path == "/ready" {
				next.ServeHTTP(w, r)
				return
			}

			// Get token from Authorization header
			authHeader := r.Header.Get("Authorization")
			if authHeader == "" {
				http.Error(w, "Authorization header required", http.StatusUnauthorized)
				return
			}

			// Extract token
			tokenString := strings.TrimPrefix(authHeader, "Bearer ")
			if tokenString == authHeader {
				http.Error(w, "Invalid authorization header format", http.StatusUnauthorized)
				return
			}

			// Verify token
			token, err := tokenAuth.Decode(tokenString)
			if err != nil {
				log.Error().Err(err).Msg("Failed to decode JWT token")
				http.Error(w, "Invalid token", http.StatusUnauthorized)
				return
			}

			// Validate token
			if token == nil || jwt.Validate(token) != nil {
				http.Error(w, "Invalid token", http.StatusUnauthorized)
				return
			}

			// Add token to context
			ctx := jwtauth.NewContext(r.Context(), token, nil)
			r = r.WithContext(ctx)

			next.ServeHTTP(w, r)
		})
	}
}

// loggingMiddleware logs HTTP requests
func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Create a response writer that captures the status code
		rw := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		// Add request ID to logger
		logger := log.With().
			Str("method", r.Method).
			Str("path", r.URL.Path).
			Str("remote_addr", r.RemoteAddr).
			Logger()

		// Log request
		logger.Info().Msg("Request started")

		// Call next handler
		next.ServeHTTP(rw, r)

		// Log response
		duration := time.Since(start)
		logger.Info().
			Int("status", rw.statusCode).
			Dur("duration", duration).
			Msg("Request completed")
	})
}

// responseWriter wraps http.ResponseWriter to capture status code
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// rateLimitMiddleware implements basic rate limiting
func rateLimitMiddleware(requestsPerMinute int) func(http.Handler) http.Handler {
	// Simple in-memory rate limiter (for production, use Redis)
	type clientLimiter struct {
		requests []time.Time
	}

	limiters := make(map[string]*clientLimiter)

	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// Get client IP (in production, use proper IP extraction)
			clientIP := r.RemoteAddr

			now := time.Now()
			limiter, exists := limiters[clientIP]
			if !exists {
				limiter = &clientLimiter{requests: []time.Time{}}
				limiters[clientIP] = limiter
			}

			// Clean old requests (older than 1 minute)
			cutoff := now.Add(-time.Minute)
			validRequests := []time.Time{}
			for _, reqTime := range limiter.requests {
				if reqTime.After(cutoff) {
					validRequests = append(validRequests, reqTime)
				}
			}
			limiter.requests = validRequests

			// Check rate limit
			if len(limiter.requests) >= requestsPerMinute {
				http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
				return
			}

			// Add current request
			limiter.requests = append(limiter.requests, now)

			next.ServeHTTP(w, r)
		})
	}
}

// corsMiddleware handles CORS headers
func corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Set CORS headers
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Accept, Authorization, Content-Type, X-CSRF-Token")

		// Handle preflight requests
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// timeoutMiddleware adds timeout to requests
func timeoutMiddleware(timeout time.Duration) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			ctx, cancel := context.WithTimeout(r.Context(), timeout)
			defer cancel()

			r = r.WithContext(ctx)
			next.ServeHTTP(w, r)
		})
	}
}
