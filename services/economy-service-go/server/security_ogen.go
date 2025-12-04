// Issue: #150 - Security Handler for ogen (JWT validation)
package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	api "github.com/gc-lover/necpgame-monorepo/services/economy-service-go/pkg/api"
)

// SecurityHandler implements ogen security handler
type SecurityHandler struct {
	jwtValidator *JwtValidator
}

// NewSecurityHandler creates new security handler
func NewSecurityHandler(jwtValidator *JwtValidator) *SecurityHandler {
	return &SecurityHandler{
		jwtValidator: jwtValidator,
	}
}

// HandleBearerAuth handles Bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// Extract token from BearerAuth struct
	token := t.GetToken()
	if token == "" {
		return ctx, fmt.Errorf("missing token")
	}

	// Use existing JWT validator (it expects "Bearer <token>" format)
	claims, err := s.jwtValidator.Verify(ctx, "Bearer "+token)
	if err != nil {
		return ctx, fmt.Errorf("invalid token: %w", err)
	}

	// Extract user_id from claims
	userID := claims.Subject
	if userID == "" {
		userID = claims.RegisteredClaims.Subject
	}

	// Parse UUID
	userUUID, err := uuid.Parse(userID)
	if err != nil {
		return ctx, fmt.Errorf("invalid user id: %w", err)
	}

	// Add to context (handlers use it)
	ctx = context.WithValue(ctx, "user_id", userID)
	ctx = context.WithValue(ctx, "user_uuid", userUUID)
	ctx = context.WithValue(ctx, "username", claims.PreferredUsername)
	ctx = context.WithValue(ctx, "claims", claims)

	return ctx, nil
}

