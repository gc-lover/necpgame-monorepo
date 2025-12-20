// Package server Issue: #1597
// SecurityHandler implements ogen security
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/quest-rewards-events-service-go/pkg/api"
)

type SecurityHandler struct{}

// HandleBearerAuth implements ogen security
func (h *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	// For now, just return context
	return ctx, nil
}
