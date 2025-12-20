// Package server Issue: #150 - Security Handler (JWT validation)
package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

// SecurityHandler implements ogen security handler
type SecurityHandler struct{}

// NewSecurityHandler creates new security handler

// HandleBearerAuth handles Bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, t api.BearerAuth) (context.Context, error) {
	// Extract token from BearerAuth struct
	token := t.GetToken()
	if token == "" {
		return ctx, fmt.Errorf("missing token")
	}

	// TODO: Validate JWT token properly (use jwt-go library)
	// For now, mock validation
	playerID, err := s.validateToken()
	if err != nil {
		return ctx, fmt.Errorf("invalid token: %w", err)
	}

	// Add player_id to context (handlers use it)
	ctx = context.WithValue(ctx, "player_id", playerID)

	return ctx, nil
}

// validateToken validates JWT token (mock for now)
func (s *SecurityHandler) validateToken() (uuid.UUID, error) {
	// TODO: Implement real JWT validation
	// - Verify signature
	// - Check expiration
	// - Extract claims

	// Mock: just return a random UUID for testing
	return uuid.New(), nil
}
