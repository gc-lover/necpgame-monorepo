// Package server Issue: #1597 - ogen security handlers
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/quest-skill-checks-conditions-service-go/pkg/api"
)

type SecurityHandler struct{}

// HandleBearerAuth implements ogen security
func (h *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	// For now, accept all requests
	return ctx, nil
}
