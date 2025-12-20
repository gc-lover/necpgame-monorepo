// Package server Issue: #2203 - Authentication and authorization
package server

import (
	"context"
	"fmt"
	"net/http"
	"strings"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// JwtValidator handles JWT token validation
type JwtValidator struct {
	issuer      string
	jwksURL     string
	logger      *logrus.Logger
	jwksCache   map[string]interface{}
	cacheExpiry time.Time
}

// NewJwtValidator creates new JWT validator
func NewJwtValidator(issuer, jwksURL string, logger *logrus.Logger) *JwtValidator {
	return &JwtValidator{
		issuer:    issuer,
		jwksURL:   jwksURL,
		logger:    logger,
		jwksCache: make(map[string]interface{}),
	}
}

// ValidateToken validates JWT token
func (v *JwtValidator) ValidateToken(tokenString string) (*Claims, error) {
	token, err := jwt.ParseWithClaims(tokenString, &Claims{}, func(token *jwt.Token) (interface{}, error) {
		// Validate signing method
		if _, ok := token.Method.(*jwt.SigningMethodRSA); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}

		// Get public key from JWKS
		return v.getPublicKey()
	})

	if err != nil {
		return nil, fmt.Errorf("failed to parse token: %w", err)
	}

	if claims, ok := token.Claims.(*Claims); ok && token.Valid {
		return claims, nil
	}

	return nil, fmt.Errorf("invalid token")
}

// Claims represents JWT claims
type Claims struct {
	UserID      uuid.UUID  `json:"sub"`
	Username    string     `json:"preferred_username"`
	Email       string     `json:"email"`
	Roles       []string   `json:"roles"`
	CharacterID *uuid.UUID `json:"character_id,omitempty"`
	GuildID     *uuid.UUID `json:"guild_id,omitempty"`
	jwt.RegisteredClaims
}

// getPublicKey retrieves public key from JWKS (simplified implementation)
func (v *JwtValidator) getPublicKey() (interface{}, error) {
	// TODO: Implement proper JWKS fetching and caching
	// For now, return a mock key
	return []byte("mock-public-key"), nil
}

// ExtractPlayerID extracts player ID from request context
func (v *JwtValidator) ExtractPlayerID(ctx context.Context) (uuid.UUID, error) {
	claims := ctx.Value("claims")
	if claims == nil {
		return uuid.Nil, fmt.Errorf("no claims in context")
	}

	if c, ok := claims.(*Claims); ok {
		return c.UserID, nil
	}

	return uuid.Nil, fmt.Errorf("invalid claims type")
}

// NewAuthMiddleware AuthMiddleware validates JWT tokens
func NewAuthMiddleware(validator *JwtValidator, authEnabled bool, logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			if !authEnabled {
				next.ServeHTTP(w, r)
				return
			}

			authHeader := r.Header.Get("Authorization")
			if authHeader == "" {
				http.Error(w, "Authorization header required", http.StatusUnauthorized)
				return
			}

			tokenParts := strings.Split(authHeader, " ")
			if len(tokenParts) != 2 || tokenParts[0] != "Bearer" {
				http.Error(w, "Invalid authorization header format", http.StatusUnauthorized)
				return
			}

			claims, err := validator.ValidateToken(tokenParts[1])
			if err != nil {
				logger.WithError(err).Warn("Token validation failed")
				http.Error(w, "Invalid token", http.StatusUnauthorized)
				return
			}

			// Add claims to request context
			ctx := context.WithValue(r.Context(), "claims", claims)
			r = r.WithContext(ctx)

			next.ServeHTTP(w, r)
		})
	}
}

// NewLoggingMiddleware LoggingMiddleware logs HTTP requests
func NewLoggingMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Create response writer wrapper to capture status code
			wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

			next.ServeHTTP(wrapped, r)

			logger.WithFields(logrus.Fields{
				"method":      r.Method,
				"path":        r.URL.Path,
				"status":      wrapped.statusCode,
				"duration_ms": time.Since(start).Milliseconds(),
				"user_agent":  r.UserAgent(),
				"remote_addr": r.RemoteAddr,
			}).Info("HTTP request")
		})
	}
}

// NewTimeoutMiddleware TimeoutMiddleware adds request timeout
func NewTimeoutMiddleware(timeout time.Duration) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			ctx, cancel := context.WithTimeout(r.Context(), timeout)
			defer cancel()

			r = r.WithContext(ctx)
			next.ServeHTTP(w, r)
		})
	}
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
