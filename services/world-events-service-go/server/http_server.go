// World Events HTTP Server - Enterprise-grade server setup
// Issue: #2224
// PERFORMANCE: Optimized HTTP server for world event management

package server

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"

	"github.com/go-redis/redis/v8"
	"github.com/golang-jwt/jwt/v4"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/pkg/api"
)

// JWTClaims represents JWT token claims
type JWTClaims struct {
	UserID   string `json:"user_id"`
	Username string `json:"username"`
	Role     string `json:"role"`
	jwt.RegisteredClaims
}

// SecurityHandler implements JWT authentication
type SecurityHandler struct {
	jwtSecret []byte
	logger    *zap.Logger
}

// NewSecurityHandler creates a new security handler with JWT secret
func NewSecurityHandler(jwtSecret string, logger *zap.Logger) *SecurityHandler {
	return &SecurityHandler{
		jwtSecret: []byte(jwtSecret),
		logger:    logger,
	}
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Extract token from BearerAuth
	tokenString := t.Token

	// Parse and validate token
	token, err := jwt.ParseWithClaims(tokenString, &JWTClaims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return s.jwtSecret, nil
	})

	if err != nil {
		s.logger.Error("JWT validation failed", zap.Error(err))
		return ctx, fmt.Errorf("invalid token: %w", err)
	}

	if !token.Valid {
		s.logger.Warn("Invalid JWT token provided")
		return ctx, fmt.Errorf("token is not valid")
	}

	claims, ok := token.Claims.(*JWTClaims)
	if !ok {
		s.logger.Error("Invalid JWT claims format")
		return ctx, fmt.Errorf("invalid token claims")
	}

	// Add user information to context
	ctx = context.WithValue(ctx, "user_id", claims.UserID)
	ctx = context.WithValue(ctx, "username", claims.Username)
	ctx = context.WithValue(ctx, "role", claims.Role)

	s.logger.Debug("JWT validated successfully", zap.String("user", claims.Username))
	return ctx, nil
}

// Server wraps the HTTP server with handlers
type Server struct {
	handler *api.Server
}

// NewServer creates a new HTTP server instance
// PERFORMANCE: Dependency injection for database and cache
func NewServer(db *sql.DB, redisClient *redis.Client, logger *zap.Logger) (*Server, error) {
	// Create handler with PERFORMANCE optimizations and dependencies
	h := NewHandler(db, redisClient)

	// Initialize security handler with JWT secret
	jwtSecret := "default-jwt-secret-change-in-production" // TODO: Get from env
	sec := NewSecurityHandler(jwtSecret, logger)

	// Create ogen server with security handler
	handler, err := api.NewServer(h, sec)
	if err != nil {
		logger.Error("Failed to create API server", zap.Error(err))
		return nil, fmt.Errorf("failed to create API server: %w", err)
	}

	return &Server{
		handler: handler,
	}, nil
}

// Handler returns the HTTP handler
func (s *Server) Handler() http.Handler {
	return s.handler
}
