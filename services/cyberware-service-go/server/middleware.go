// Issue: #2226
// PERFORMANCE: Middleware optimized for cyberware implant operations

package server

import (
	"net/http"
	"time"

	"go.uber.org/zap"
)

// LoggingMiddleware logs HTTP requests
// PERFORMANCE: Optimized logging with structured fields
func LoggingMiddleware(logger *zap.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// PERFORMANCE: Pre-allocate logger with common fields
			logger.Info("Request started",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.String("remote_addr", r.RemoteAddr))

			next.ServeHTTP(w, r)

			// PERFORMANCE: Log request duration
			logger.Info("Request completed",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Duration("duration", time.Since(start)))
		})
	}
}

// CORSMiddleware handles CORS for web clients
// PERFORMANCE: Optimized CORS headers for implant UI access
func CORSMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// PERFORMANCE: Set CORS headers for cyberware UI
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
}

// RecoveryMiddleware recovers from panics
// PERFORMANCE: Panic recovery to prevent service crashes
func RecoveryMiddleware(logger *zap.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if err := recover(); err != nil {
					logger.Error("Panic recovered",
						zap.Any("panic", err),
						zap.String("method", r.Method),
						zap.String("path", r.URL.Path))

					w.WriteHeader(http.StatusInternalServerError)
					w.Write([]byte(`{"error": "Internal server error"}`))
				}
			}()

			next.ServeHTTP(w, r)
		})
	}
}

// RateLimitMiddleware implements rate limiting for cyberware operations
// PERFORMANCE: Prevents abuse of implant activation endpoints
func RateLimitMiddleware() func(http.Handler) http.Handler {
	// TODO: Implement proper rate limiting with Redis
	// For now, simple in-memory rate limiting
	requests := make(map[string][]time.Time)

	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// PERFORMANCE: Simple rate limiting for hot paths
			key := r.RemoteAddr
			now := time.Now()

			// Clean old requests
			var recent []time.Time
			for _, t := range requests[key] {
				if now.Sub(t) < time.Minute {
					recent = append(recent, t)
				}
			}
			requests[key] = recent

			// Check rate limit (100 requests per minute)
			if len(recent) >= 100 {
				w.WriteHeader(http.StatusTooManyRequests)
				w.Write([]byte(`{"error": "Rate limit exceeded"}`))
				return
			}

			// Add current request
			requests[key] = append(requests[key], now)

			next.ServeHTTP(w, r)
		})
	}
}

// TimeoutMiddleware adds timeout to requests
// PERFORMANCE: Prevents hanging connections for implant operations
func TimeoutMiddleware(timeout time.Duration) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// PERFORMANCE: Set timeout for long-running operations
			ctx, cancel := context.WithTimeout(r.Context(), timeout)
			defer cancel()

			r = r.WithContext(ctx)
			next.ServeHTTP(w, r)
		})
	}
}
