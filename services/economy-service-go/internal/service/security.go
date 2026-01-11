package service

import (
	"context"
	"fmt"

	"go.uber.org/zap"

	"necpgame/services/economy-service-go/config"
	api "necpgame/services/economy-service-go/pkg/api"
)

// SecurityHandler implements the generated SecurityHandler interface for economy service
type SecurityHandler struct {
	jwtService *JWTService
	logger     *zap.Logger
}

// NewSecurityHandler creates a new security handler
func NewSecurityHandler(cfg *config.Config, logger *zap.Logger) *SecurityHandler {
	return &SecurityHandler{
		jwtService: NewJWTService(cfg),
		logger:     logger,
	}
}

// HandleBearerAuth implements JWT Bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t string) (context.Context, error) {
	if t == "" {
		return ctx, fmt.Errorf("missing bearer token")
	}

	claims, err := s.jwtService.ValidateAccessToken(t)
	if err != nil {
		s.logger.Warn("Invalid JWT token", zap.Error(err))
		return ctx, fmt.Errorf("invalid token: %w", err)
	}

	// Add user information to context
	ctx = context.WithValue(ctx, "user_id", claims.UserID)
	ctx = context.WithValue(ctx, "username", claims.Username)

	s.logger.Debug("JWT token validated",
		zap.String("user_id", claims.UserID),
		zap.String("username", claims.Username),
		zap.String("operation", string(operationName)))

	return ctx, nil
}