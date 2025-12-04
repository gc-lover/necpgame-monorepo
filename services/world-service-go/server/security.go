// Issue: #1596 - ogen security handlers
package server

import (
	"context"

	"github.com/necpgame/world-service-go/pkg/api"
)

// HandleBearerAuth implements ogen security
func (h *Handlers) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	// For now, accept all requests
	return ctx, nil
}

