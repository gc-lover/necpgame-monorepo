package server

import (
	"net/http"
	"strings"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
)

// AuthMiddleware handles JWT authentication
type AuthMiddleware struct {
	logger         *zap.Logger
	jwtSecret      []byte
	skipAuthPaths  map[string]bool
}

// NewAuthMiddleware creates a new authentication middleware
func NewAuthMiddleware(logger *zap.Logger, jwtSecret string) *AuthMiddleware {
	skipPaths := map[string]bool{
		"/health": true,
	}

	return &AuthMiddleware{
		logger:        logger,
		jwtSecret:     []byte(jwtSecret),
		skipAuthPaths: skipPaths,
	}
}

// Middleware implements the authentication middleware
func (m *AuthMiddleware) Middleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Skip authentication for certain paths
		if m.skipAuthPaths[r.URL.Path] {
			next.ServeHTTP(w, r)
			return
		}

		// Extract token from Authorization header
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			m.logger.Warn("Missing authorization header")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Check Bearer token format
		if !strings.HasPrefix(authHeader, "Bearer ") {
			m.logger.Warn("Invalid authorization header format")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		tokenString := strings.TrimPrefix(authHeader, "Bearer ")

		// Parse and validate JWT token
		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			// Validate signing method
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				return nil, jwt.ErrSignatureInvalid
			}
			return m.jwtSecret, nil
		})

		if err != nil {
			m.logger.Warn("Invalid JWT token", zap.Error(err))
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		if !token.Valid {
			m.logger.Warn("Token validation failed")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Extract claims
		claims, ok := token.Claims.(jwt.MapClaims)
		if !ok {
			m.logger.Warn("Invalid token claims")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Extract user information
		userIDStr, ok := claims["user_id"].(string)
		if !ok {
			m.logger.Warn("Missing user_id in token")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		userID, err := uuid.Parse(userIDStr)
		if err != nil {
			m.logger.Warn("Invalid user_id format", zap.Error(err))
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Extract user type (default to customer if not specified)
		userType := models.AuthorTypeCustomer
		if typeStr, ok := claims["user_type"].(string); ok {
			switch typeStr {
			case "agent":
				userType = models.AuthorTypeAgent
			case "system":
				userType = models.AuthorTypeSystem
			case "customer":
				userType = models.AuthorTypeCustomer
			default:
				m.logger.Warn("Unknown user type in token", zap.String("user_type", typeStr))
			}
		}

		// Add user information to context
		ctx := models.SetUserInContext(r.Context(), userID, userType)
		r = r.WithContext(ctx)

		// Log authentication success
		m.logger.Info("User authenticated",
			zap.String("user_id", userID.String()),
			zap.String("user_type", string(userType)),
			zap.String("path", r.URL.Path))

		next.ServeHTTP(w, r)
	})
}