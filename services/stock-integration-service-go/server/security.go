// Issue: #1601
package server

import (
	"context"
	"strings"

	"github.com/ogen-go/ogen/ogenerrors"
	api "github.com/gc-lover/necpgame-monorepo/services/stock-integration-service-go/pkg/api"
)

type SecurityHandler struct {
	authEnabled bool
}

func NewSecurityHandler(authEnabled bool) *SecurityHandler {
	return &SecurityHandler{
		authEnabled: authEnabled,
	}
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// Skip validation if auth is disabled (for development/testing)
	if !s.authEnabled {
		return ctx, nil
	}

	// Check if token is provided
	tokenString := strings.TrimSpace(t.Token)
	if tokenString == "" {
		return ctx, ogenerrors.ErrSecurityRequirementIsNotSatisfied
	}

	// Remove "Bearer " prefix if present
	tokenString = strings.TrimPrefix(tokenString, "Bearer ")
	tokenString = strings.TrimSpace(tokenString)

	if tokenString == "" {
		return ctx, ogenerrors.ErrSecurityRequirementIsNotSatisfied
	}

	// TODO: Implement full JWT validation with JwtValidator
	// For now, basic token presence check is sufficient
	// Future: Add JwtValidator similar to other services

	// Store token in context for potential use in handlers
	ctx = context.WithValue(ctx, "token", tokenString)

	return ctx, nil
}

