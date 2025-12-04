// Issue: #1597
// SecurityHandler implements ogen security
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/quest-state-dialogue-service-go/pkg/api"
)

type SecurityHandler struct{}

// HandleBearerAuth implements ogen security
func (h *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	// For now, just return context
	return ctx, nil
}

