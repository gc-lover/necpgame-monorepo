// Package server Issue: #1598
// SecurityHandler implements ogen security
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-history-service-go/pkg/api"
)

type SecurityHandler struct{}

// HandleBearerAuth implements ogen security
func (h *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	// For now, just return context
	return ctx, nil
}
