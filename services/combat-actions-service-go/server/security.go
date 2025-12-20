// Package server Issue: #1595
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/combat-actions-service-go/pkg/api"
)

// SecurityHandler implements ogen security
type SecurityHandler struct{}

// HandleBearerAuth handles JWT bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	// For now, accept all requests
	return ctx, nil
}
