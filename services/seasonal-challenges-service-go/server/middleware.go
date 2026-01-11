// Enterprise-grade middleware for MMOFPS performance and security
// Issue: #1506
package server

import (
	"net/http"
	"time"

	"go.uber.org/zap"
)

// Middleware represents a middleware function
type Middleware func(http.HandlerFunc) http.HandlerFunc

// Chain applies middleware in order
func Chain(handler http.HandlerFunc, middlewares ...Middleware) http.HandlerFunc {
	for i := len(middlewares) - 1; i >= 0; i-- {
		handler = middlewares[i](handler)
	}
	return handler
}

// LoggingMiddleware - structured logging for MMOFPS monitoring
func LoggingMiddleware(logger *zap.Logger) Middleware {
	return func(next http.HandlerFunc) http.HandlerFunc {
		return func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Create response writer wrapper for status code capture
			rw := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

			// Log request start
			logger.Info("Request started",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.String("remote_addr", r.RemoteAddr),
				zap.String("user_agent", r.UserAgent()),
			)

			// Call next handler
			next(rw, r)

			// Log request completion with performance metrics
			duration := time.Since(start)
			logger.Info("Request completed",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Int("status", rw.statusCode),
				zap.Duration("duration", duration),
				zap.Int64("content_length", rw.contentLength),
			)
		}
	}
}

// CORSMiddleware - CORS handling for web clients
func CORSMiddleware() Middleware {
	return func(next http.HandlerFunc) http.HandlerFunc {
		return func(w http.ResponseWriter, r *http.Request) {
			w.Header().Set("Access-Control-Allow-Origin", "*")
			w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
			w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")
			w.Header().Set("Access-Control-Max-Age", "86400")

			if r.Method == "OPTIONS" {
				w.WriteHeader(http.StatusOK)
				return
			}

			next(w, r)
		}
	}
}

// SecurityMiddleware - security headers for MMOFPS protection
func SecurityMiddleware() Middleware {
	return func(next http.HandlerFunc) http.HandlerFunc {
		return func(w http.ResponseWriter, r *http.Request) {
			// Security headers
			w.Header().Set("X-Content-Type-Options", "nosniff")
			w.Header().Set("X-Frame-Options", "DENY")
			w.Header().Set("X-XSS-Protection", "1; mode=block")
			w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
			w.Header().Set("Content-Security-Policy", "default-src 'self'")

			// Rate limiting headers (placeholder)
			w.Header().Set("X-RateLimit-Limit", "1000")
			w.Header().Set("X-RateLimit-Remaining", "950")
			w.Header().Set("X-RateLimit-Reset", time.Now().Add(time.Hour).Format(time.RFC3339))

			next(w, r)
		}
	}
}

// TimeoutMiddleware - prevents slow requests in MMOFPS environment
func TimeoutMiddleware(timeout time.Duration) Middleware {
	return func(next http.HandlerFunc) http.HandlerFunc {
		return func(w http.ResponseWriter, r *http.Request) {
			done := make(chan bool, 1)

			go func() {
				next(w, r)
				done <- true
			}()

			select {
			case <-done:
				// Handler completed normally
			case <-time.After(timeout):
				// Handler timed out
				w.WriteHeader(http.StatusGatewayTimeout)
				w.Write([]byte(`{"error": "Request timeout"}`))
			}
		}
	}
}

// MetricsMiddleware - Prometheus metrics collection
func MetricsMiddleware() Middleware {
	return func(next http.HandlerFunc) http.HandlerFunc {
		return func(w http.ResponseWriter, r *http.Request) {
			// TODO: Implement Prometheus metrics collection
			// - request_count_total{endpoint, method, status}
			// - request_duration_seconds{endpoint, method}
			// - active_connections gauge

			next(w, r)
		}
	}
}

// responseWriter wraps http.ResponseWriter to capture status code and content length
type responseWriter struct {
	http.ResponseWriter
	statusCode    int
	contentLength int64
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

func (rw *responseWriter) Write(data []byte) (int, error) {
	if rw.statusCode == 0 {
		rw.statusCode = http.StatusOK
	}
	size, err := rw.ResponseWriter.Write(data)
	rw.contentLength += int64(size)
	return size, err
}