// Issue: #1599
// Issue: #151 - JWT validation
package server

import (
	"context"
	"errors"

	api "github.com/gc-lover/necpgame-monorepo/services/mail-service-go/pkg/api"
)

type SecurityHandler struct {
	jwtValidator *JwtValidator
}

func NewSecurityHandler(jwtValidator *JwtValidator) *SecurityHandler {
	return &SecurityHandler{
		jwtValidator: jwtValidator,
	}
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	if t.Token == "" {
		return ctx, errors.New("bearer token required")
	}

	if s.jwtValidator == nil {
		// If validator is not configured, skip validation (for development)
		return ctx, nil
	}

	claims, err := s.jwtValidator.Verify(ctx, t.Token)
	if err != nil {
		return ctx, err
	}

	// Extract player ID from claims
	playerID, err := ExtractPlayerIDFromClaims(claims)
	if err != nil {
		return ctx, err
	}

	// Store player ID in context for handlers
	ctx = context.WithValue(ctx, "player_id", playerID)
	ctx = context.WithValue(ctx, "user_uuid", playerID)
	ctx = context.WithValue(ctx, "user_id", playerID.String())

	return ctx, nil
}







