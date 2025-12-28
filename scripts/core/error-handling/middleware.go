// Package middleware provides HTTP middleware for error handling and logging
package middleware

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"runtime/debug"
	"strings"
	"time"

	"github.com/go-chi/chi/v5/middleware"
	"go.uber.org/zap"

	"github.com/your-org/necpgame/scripts/core/error-handling"
)

// HTTPErrorResponse represents structured error response
type HTTPErrorResponse struct {
	Error   string                 `json:"error"`
	Type    string                 `json:"type,omitempty"`
	Code    string                 `json:"code,omitempty"`
	Details string                 `json:"details,omitempty"`
	Fields  map[string]interface{} `json:"fields,omitempty"`
}

// HTTPSuccessResponse represents structured success response
type HTTPSuccessResponse struct {
	Data   interface{} `json:"data,omitempty"`
	Meta   interface{} `json:"meta,omitempty"`
	Status string      `json:"status"`
}

// ResponseWriter wraps http.ResponseWriter to capture status code
type ResponseWriter struct {
	http.ResponseWriter
	status int
	size   int
	body   *bytes.Buffer
}

func (rw *ResponseWriter) WriteHeader(code int) {
	rw.status = code
	rw.ResponseWriter.WriteHeader(code)
}

func (rw *ResponseWriter) Write(b []byte) (int, error) {
	if rw.status == 0 {
		rw.status = http.StatusOK
	}
	size, err := rw.ResponseWriter.Write(b)
	rw.size += size

	if rw.body != nil {
		rw.body.Write(b)
	}

	return size, err
}

// NewResponseWriter creates a new ResponseWriter
func NewResponseWriter(w http.ResponseWriter, captureBody bool) *ResponseWriter {
	rw := &ResponseWriter{
		ResponseWriter: w,
		status:         0,
		size:           0,
	}

	if captureBody {
		rw.body = &bytes.Buffer{}
	}

	return rw
}

// ErrorHandler middleware provides comprehensive error handling
func ErrorHandler(logger *logging.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if err := recover(); err != nil {
					logger.Errorw("Panic recovered",
						"panic", err,
						"stack", string(debug.Stack()),
						"url", r.URL.String(),
						"method", r.Method,
					)

					w.Header().Set("Content-Type", "application/json")
					w.WriteHeader(http.StatusInternalServerError)

					response := HTTPErrorResponse{
						Error:   "Internal server error",
						Type:    string(errors.ErrorTypeInternal),
						Code:    "INTERNAL_ERROR",
						Details: "An unexpected error occurred",
					}

					json.NewEncoder(w).Encode(response)
				}
			}()

			next.ServeHTTP(w, r)
		})
	}
}

// LoggingMiddleware provides structured request/response logging
func LoggingMiddleware(logger *logging.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			requestID := middleware.GetReqID(r.Context())

			// Add request ID to context
			ctx := logging.NewContextWithRequestID(r.Context(), requestID)
			r = r.WithContext(ctx)

			// Create response writer wrapper
			rw := NewResponseWriter(w, false)

			// Log request
			logger.WithRequestID(requestID).Infow("Request started",
				"method", r.Method,
				"path", r.URL.Path,
				"query", r.URL.RawQuery,
				"user_agent", r.Header.Get("User-Agent"),
				"remote_addr", r.RemoteAddr,
			)

			// Call next handler
			next.ServeHTTP(rw, r)

			// Log response
			duration := time.Since(start)
			logger.WithRequestID(requestID).LogRequest(
				r.Method,
				r.URL.Path,
				r.Header.Get("User-Agent"),
				r.RemoteAddr,
				rw.status,
				duration,
			)

			// Log slow requests
			if duration > 5*time.Second {
				logger.WithRequestID(requestID).Warnw("Slow request detected",
					"duration", duration,
					"method", r.Method,
					"path", r.URL.Path,
				)
			}
		})
	}
}

// RecoveryMiddleware provides panic recovery with structured logging
func RecoveryMiddleware(logger *logging.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if err := recover(); err != nil {
					requestID := middleware.GetReqID(r.Context())

					logger.WithRequestID(requestID).Errorw("Panic recovered",
						"panic", err,
						"stack", string(debug.Stack()),
						"url", r.URL.String(),
						"method", r.Method,
						"headers", sanitizeHeaders(r.Header),
					)

					http.Error(w, "Internal Server Error", http.StatusInternalServerError)
				}
			}()

			next.ServeHTTP(w, r)
		})
	}
}

// TimeoutMiddleware adds timeout handling
func TimeoutMiddleware(timeout time.Duration) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			ctx, cancel := context.WithTimeout(r.Context(), timeout)
			defer cancel()

			r = r.WithContext(ctx)

			done := make(chan struct{})
			panicChan := make(chan interface{}, 1)

			go func() {
				defer func() {
					if p := recover(); p != nil {
						panicChan <- p
					}
				}()
				next.ServeHTTP(w, r)
				close(done)
			}()

			select {
			case <-done:
				// Request completed normally
			case p := <-panicChan:
				panic(p) // Re-panic to let recovery middleware handle it
			case <-ctx.Done():
				if ctx.Err() == context.DeadlineExceeded {
					http.Error(w, "Request timeout", http.StatusRequestTimeout)
				}
			}
		})
	}
}

// RateLimitMiddleware provides rate limiting with proper error responses
func RateLimitMiddleware(logger *logging.Logger) func(http.Handler) http.Handler {
	// Simple in-memory rate limiter (for production, use Redis)
	rateLimiter := NewRateLimiter(100, time.Minute) // 100 requests per minute

	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			requestID := middleware.GetReqID(r.Context())

			// Simple rate limiting by IP (in production, use user ID or API key)
			key := r.RemoteAddr

			if !rateLimiter.Allow(key) {
				logger.WithRequestID(requestID).Warnw("Rate limit exceeded",
					"remote_addr", r.RemoteAddr,
					"path", r.URL.Path,
				)

				respondWithGameError(w, errors.NewRateLimitError("RATE_LIMIT_EXCEEDED", "Rate limit exceeded"))
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// AuthMiddleware provides JWT authentication with proper error handling
func AuthMiddleware(logger *logging.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			requestID := middleware.GetReqID(r.Context())

			authHeader := r.Header.Get("Authorization")
			if authHeader == "" {
				logger.WithRequestID(requestID).Warnw("Missing authorization header")
				respondWithGameError(w, errors.NewAuthenticationError("MISSING_AUTH", "Missing authorization header"))
				return
			}

			if !strings.HasPrefix(authHeader, "Bearer ") {
				logger.WithRequestID(requestID).Warnw("Invalid authorization format")
				respondWithGameError(w, errors.NewAuthenticationError("INVALID_FORMAT", "Invalid authorization format"))
				return
			}

			token := strings.TrimPrefix(authHeader, "Bearer ")
			if token == "" {
				logger.WithRequestID(requestID).Warnw("Empty token")
				respondWithGameError(w, errors.NewAuthenticationError("EMPTY_TOKEN", "Empty token"))
				return
			}

			// TODO: Implement proper JWT validation
			// For now, just check if token exists
			// In production: validate JWT signature, expiration, claims, etc.

			next.ServeHTTP(w, r)
		})
	}
}

// MetricsMiddleware provides request metrics collection
func MetricsMiddleware(logger *logging.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			requestID := middleware.GetReqID(r.Context())

			rw := NewResponseWriter(w, false)

			next.ServeHTTP(rw, r)

			duration := time.Since(start)

			// Log metrics
			logger.WithRequestID(requestID).LogPerformanceMetric(
				"http_request_duration",
				duration.Seconds(),
				map[string]string{
					"method": r.Method,
					"path":   r.URL.Path,
					"status": fmt.Sprintf("%d", rw.status),
				},
			)
		})
	}
}

// respondWithGameError sends a structured error response
func respondWithGameError(w http.ResponseWriter, err *errors.GameError) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(err.HTTPStatus)

	response := HTTPErrorResponse{
		Error:   err.Message,
		Type:    string(err.Type),
		Code:    err.Code,
		Details: err.Details,
		Fields:  err.Fields,
	}

	if err := json.NewEncoder(w).Encode(response); err != nil {
		// If JSON encoding fails, send plain text error
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}

// sanitizeHeaders removes sensitive headers from logs
func sanitizeHeaders(headers http.Header) map[string]string {
	sanitized := make(map[string]string)

	for key, values := range headers {
		// Skip sensitive headers
		if strings.ToLower(key) == "authorization" || strings.ToLower(key) == "cookie" {
			sanitized[key] = "[REDACTED]"
		} else {
			sanitized[key] = strings.Join(values, ", ")
		}
	}

	return sanitized
}

// RateLimiter provides simple in-memory rate limiting
type RateLimiter struct {
	requests map[string][]time.Time
	rate     int
	window   time.Duration
}

func NewRateLimiter(rate int, window time.Duration) *RateLimiter {
	return &RateLimiter{
		requests: make(map[string][]time.Time),
		rate:     rate,
		window:   window,
	}
}

func (rl *RateLimiter) Allow(key string) bool {
	now := time.Now()

	// Clean old requests
	if requests, exists := rl.requests[key]; exists {
		var valid []time.Time
		for _, t := range requests {
			if now.Sub(t) < rl.window {
				valid = append(valid, t)
			}
		}
		rl.requests[key] = valid
	}

	// Check rate limit
	if len(rl.requests[key]) >= rl.rate {
		return false
	}

	// Add current request
	rl.requests[key] = append(rl.requests[key], now)
	return true
}
