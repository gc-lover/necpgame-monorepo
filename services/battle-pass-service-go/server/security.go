// Issue: JWT Implementation for Battle Pass Service
package server

import (
	"context"
	"fmt"
	"log"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-service-go/pkg/api"
	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
)

// SecurityHandler implements ogen.SecurityHandler with JWT validation
type SecurityHandler struct {
	config *Config
	logger *log.Logger
}

// NewSecurityHandler creates new security handler with JWT validation
func NewSecurityHandler(config *Config, logger *log.Logger) *SecurityHandler {
	return &SecurityHandler{
		config: config,
		logger: logger,
	}
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	tokenString := t.GetToken()
	if tokenString == "" {
		s.logger.Printf("[JWT] %s: Empty JWT token", operationName)
		return ctx, fmt.Errorf("missing JWT token")
	}

	// Parse and validate JWT token
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		// Validate signing method
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return []byte(s.config.JWTSecret), nil
	})

	if err != nil {
		s.logger.Printf("[JWT] %s: JWT validation failed: %v", operationName, err)
		return ctx, fmt.Errorf("invalid JWT token: %w", err)
	}

	if !token.Valid {
		s.logger.Printf("[JWT] %s: Invalid JWT token", operationName)
		return ctx, fmt.Errorf("invalid JWT token")
	}

	// Extract claims
	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		s.logger.Printf("[JWT] %s: Invalid JWT claims", operationName)
		return ctx, fmt.Errorf("invalid JWT claims")
	}

	// Validate token type (must be access token)
	tokenType, ok := claims["type"].(string)
	if !ok || tokenType != "access" {
		s.logger.Printf("[JWT] %s: Invalid token type: %s", operationName, tokenType)
		return ctx, fmt.Errorf("invalid token type: expected access token")
	}

	// Extract user ID
	userIDStr, ok := claims["user_id"].(string)
	if !ok {
		s.logger.Printf("[JWT] %s: Missing user_id in JWT claims", operationName)
		return ctx, fmt.Errorf("missing user_id in JWT claims")
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.logger.Printf("[JWT] %s: Invalid user_id format: %s, error: %v", operationName, userIDStr, err)
		return ctx, fmt.Errorf("invalid user_id format: %w", err)
	}

	// Add user ID to context
	ctx = context.WithValue(ctx, "user_id", userID.String())
	ctx = context.WithValue(ctx, "user_uuid", userID)

	s.logger.Printf("[JWT] %s: JWT validation successful for user %s", operationName, userID.String())
	return ctx, nil
}
