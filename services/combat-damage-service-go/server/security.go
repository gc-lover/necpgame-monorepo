// Issue: JWT Implementation for Combat Damage Service
package server

import (
	"context"
	"fmt"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// SecurityHandler implements ogen.SecurityHandler with JWT validation
type SecurityHandler struct {
	config *Config
	logger *logrus.Logger
}

// NewSecurityHandler creates new security handler with JWT validation
func NewSecurityHandler(config *Config, logger *logrus.Logger) *SecurityHandler {
	return &SecurityHandler{
		config: config,
		logger: logger,
	}
}

// HandleBearerAuth handles JWT bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	tokenString := t.GetToken()
	if tokenString == "" {
		s.logger.WithField("operation", operationName).Warn("Empty JWT token")
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
		s.logger.WithField("operation", operationName).WithError(err).Warn("JWT validation failed")
		return ctx, fmt.Errorf("invalid JWT token: %w", err)
	}

	if !token.Valid {
		s.logger.WithField("operation", operationName).Warn("Invalid JWT token")
		return ctx, fmt.Errorf("invalid JWT token")
	}

	// Extract claims
	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		s.logger.WithField("operation", operationName).Warn("Invalid JWT claims")
		return ctx, fmt.Errorf("invalid JWT claims")
	}

	// Validate token type (must be access token)
	tokenType, ok := claims["type"].(string)
	if !ok || tokenType != "access" {
		s.logger.WithField("operation", operationName).WithField("token_type", tokenType).Warn("Invalid token type")
		return ctx, fmt.Errorf("invalid token type: expected access token")
	}

	// Extract user ID
	userIDStr, ok := claims["user_id"].(string)
	if !ok {
		s.logger.WithField("operation", operationName).Warn("Missing user_id in JWT claims")
		return ctx, fmt.Errorf("missing user_id in JWT claims")
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.logger.WithField("operation", operationName).WithField("user_id", userIDStr).WithError(err).Warn("Invalid user_id format in JWT")
		return ctx, fmt.Errorf("invalid user_id format: %w", err)
	}

	// Add user ID to context
	ctx = context.WithValue(ctx, "user_id", userID.String())
	ctx = context.WithValue(ctx, "user_uuid", userID)

	s.logger.WithField("operation", operationName).WithField("user_id", userID.String()).Debug("JWT validation successful")
	return ctx, nil
}
