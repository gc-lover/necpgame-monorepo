// Package server Issue: ogen migration
package server

import (
	"context"
	"strings"

	"github.com/necpgame/clan-war-service-go/pkg/api"
	"github.com/ogen-go/ogen/ogenerrors"
)

// SecurityHandler handles ogen security
type SecurityHandler struct {
	jwtValidator *JwtValidator
	authEnabled  bool
}

// NewSecurityHandler creates new security handler
func NewSecurityHandler(jwtValidator *JwtValidator, authEnabled bool) *SecurityHandler {
	return &SecurityHandler{
		jwtValidator: jwtValidator,
		authEnabled:  authEnabled,
	}
}

// HandleBearerAuth implements Bearer auth for ogen
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, t api.BearerAuth) (context.Context, error) {
	if !s.authEnabled {
		return ctx, nil
	}

	tokenString := t.Token
	if tokenString == "" {
		return ctx, ogenerrors.ErrSecurityRequirementIsNotSatisfied
	}

	// Remove "Bearer " prefix if present
	tokenString = strings.TrimPrefix(tokenString, "Bearer ")
	tokenString = strings.TrimSpace(tokenString)

	claims, err := s.jwtValidator.Verify(ctx, tokenString)
	if err != nil {
		return ctx, ogenerrors.ErrSecurityRequirementIsNotSatisfied
	}

	// Add user info to context
	ctx = context.WithValue(ctx, "claims", claims)
	userID := claims.Subject
	if userID == "" {
		userID = claims.RegisteredClaims.Subject
	}
	ctx = context.WithValue(ctx, "user_id", userID)
	ctx = context.WithValue(ctx, "username", claims.PreferredUsername)
	return ctx, nil
}
