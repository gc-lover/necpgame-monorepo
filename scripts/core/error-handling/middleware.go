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
	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"

	"github.com/your-org/necpgame/scripts/core/error-handling"
)

// JWTConfig holds JWT configuration
type JWTConfig struct {
	Secret           string        `json:"secret"`
	Issuer           string        `json:"issuer"`
	Audience         string        `json:"audience"`
	Expiration       time.Duration `json:"expiration"`
	RefreshExpiration time.Duration `json:"refresh_expiration"`
}

// JWTClaims represents JWT claims structure
type JWTClaims struct {
	UserID   string `json:"user_id"`
	UserType string `json:"user_type,omitempty"`
	jwt.RegisteredClaims
}

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
func AuthMiddleware(logger *logging.Logger, jwtConfig *JWTConfig) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			requestID := middleware.GetReqID(r.Context())

			authHeader := r.Header.Get("Authorization")
			if authHeader == "" {
				logger.WithRequestID(requestID).Warnw("Missing authorization header",
					"path", r.URL.Path,
					"method", r.Method)
				respondWithGameError(w, errors.NewAuthenticationError("MISSING_AUTH", "Missing authorization header"))
				return
			}

			if !strings.HasPrefix(authHeader, "Bearer ") {
				logger.WithRequestID(requestID).Warnw("Invalid authorization format",
					"header", authHeader[:min(20, len(authHeader))]+"...")
				respondWithGameError(w, errors.NewAuthenticationError("INVALID_FORMAT", "Invalid authorization format"))
				return
			}

			tokenString := strings.TrimPrefix(authHeader, "Bearer ")
			if tokenString == "" {
				logger.WithRequestID(requestID).Warnw("Empty token")
				respondWithGameError(w, errors.NewAuthenticationError("EMPTY_TOKEN", "Empty token"))
				return
			}

			// Parse and validate JWT token
			token, err := jwt.ParseWithClaims(tokenString, &JWTClaims{}, func(token *jwt.Token) (interface{}, error) {
				// Validate signing method
				if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
					return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
				}
				return []byte(jwtConfig.Secret), nil
			})

			if err != nil {
				logger.WithRequestID(requestID).Warnw("JWT parsing failed",
					"error", err.Error(),
					"token_prefix", tokenString[:min(10, len(tokenString))]+"...")
				respondWithGameError(w, errors.NewAuthenticationError("INVALID_TOKEN", "Invalid JWT token"))
				return
			}

			if !token.Valid {
				logger.WithRequestID(requestID).Warnw("Invalid JWT token")
				respondWithGameError(w, errors.NewAuthenticationError("INVALID_TOKEN", "Invalid JWT token"))
				return
			}

			// Extract claims
			claims, ok := token.Claims.(*JWTClaims)
			if !ok {
				logger.WithRequestID(requestID).Errorw("Failed to extract JWT claims")
				respondWithGameError(w, errors.NewAuthenticationError("INVALID_CLAIMS", "Invalid token claims"))
				return
			}

			// Validate standard claims
			if err := claims.Valid(); err != nil {
				logger.WithRequestID(requestID).Warnw("JWT claims validation failed",
					"error", err.Error())
				respondWithGameError(w, errors.NewAuthenticationError("EXPIRED_TOKEN", "Token has expired"))
				return
			}

			// Validate issuer if configured
			if jwtConfig.Issuer != "" && claims.Issuer != jwtConfig.Issuer {
				logger.WithRequestID(requestID).Warnw("Invalid token issuer",
					"expected", jwtConfig.Issuer,
					"actual", claims.Issuer)
				respondWithGameError(w, errors.NewAuthenticationError("INVALID_ISSUER", "Invalid token issuer"))
				return
			}

			// Validate audience if configured
			if jwtConfig.Audience != "" && claims.Audience != nil {
				found := false
				for _, aud := range claims.Audience {
					if aud == jwtConfig.Audience {
						found = true
						break
					}
				}
				if !found {
					logger.WithRequestID(requestID).Warnw("Invalid token audience",
						"expected", jwtConfig.Audience,
						"actual", claims.Audience)
					respondWithGameError(w, errors.NewAuthenticationError("INVALID_AUDIENCE", "Invalid token audience"))
					return
				}
			}

			// Validate user ID
			if claims.UserID == "" {
				logger.WithRequestID(requestID).Warnw("Missing user_id in token")
				respondWithGameError(w, errors.NewAuthenticationError("MISSING_USER_ID", "Missing user ID in token"))
				return
			}

			// Add user information to context
			ctx := context.WithValue(r.Context(), "user_id", claims.UserID)
			if claims.UserType != "" {
				ctx = context.WithValue(ctx, "user_type", claims.UserType)
			}
			ctx = context.WithValue(ctx, "token_issued_at", claims.IssuedAt)
			ctx = context.WithValue(ctx, "token_expires_at", claims.ExpiresAt)

			r = r.WithContext(ctx)

			logger.WithRequestID(requestID).Debugw("JWT authentication successful",
				"user_id", claims.UserID,
				"user_type", claims.UserType,
				"token_expires_at", claims.ExpiresAt)

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

// min returns the minimum of two integers
func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

// CreateJWTToken creates a new JWT token with the given claims
func CreateJWTToken(userID, userType string, config *JWTConfig) (string, error) {
	now := time.Now().UTC()

	claims := JWTClaims{
		UserID:   userID,
		UserType: userType,
		RegisteredClaims: jwt.RegisteredClaims{
			Issuer:    config.Issuer,
			Audience:  jwt.ClaimStrings{config.Audience},
			Subject:   userID,
			ExpiresAt: jwt.NewNumericDate(now.Add(config.Expiration)),
			IssuedAt:  jwt.NewNumericDate(now),
			NotBefore: jwt.NewNumericDate(now),
			ID:        fmt.Sprintf("%s-%d", userID, now.Unix()),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString([]byte(config.Secret))
	if err != nil {
		return "", fmt.Errorf("failed to sign JWT token: %w", err)
	}

	return tokenString, nil
}

// CreateRefreshToken creates a new refresh token
func CreateRefreshToken(userID string, config *JWTConfig) (string, error) {
	now := time.Now().UTC()

	claims := JWTClaims{
		UserID:   userID,
		UserType: "refresh",
		RegisteredClaims: jwt.RegisteredClaims{
			Issuer:    config.Issuer,
			Audience:  jwt.ClaimStrings{config.Audience},
			Subject:   userID,
			ExpiresAt: jwt.NewNumericDate(now.Add(config.RefreshExpiration)),
			IssuedAt:  jwt.NewNumericDate(now),
			NotBefore: jwt.NewNumericDate(now),
			ID:        fmt.Sprintf("refresh-%s-%d", userID, now.Unix()),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString([]byte(config.Secret))
	if err != nil {
		return "", fmt.Errorf("failed to sign refresh token: %w", err)
	}

	return tokenString, nil
}

// ValidateRefreshToken validates a refresh token and returns the user ID
func ValidateRefreshToken(tokenString string, config *JWTConfig) (string, error) {
	token, err := jwt.ParseWithClaims(tokenString, &JWTClaims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return []byte(config.Secret), nil
	})

	if err != nil {
		return "", fmt.Errorf("failed to parse refresh token: %w", err)
	}

	if !token.Valid {
		return "", fmt.Errorf("invalid refresh token")
	}

	claims, ok := token.Claims.(*JWTClaims)
	if !ok {
		return "", fmt.Errorf("invalid refresh token claims")
	}

	if claims.UserType != "refresh" {
		return "", fmt.Errorf("token is not a refresh token")
	}

	return claims.UserID, nil
}
