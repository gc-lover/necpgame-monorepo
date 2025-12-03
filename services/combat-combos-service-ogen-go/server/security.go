// Issue: #1578
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-ogen-go/pkg/api"
)

// SecurityHandler handles ogen security
type SecurityHandler struct{}

// HandleBearerAuth implements Bearer auth (stub)
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement real auth
	return ctx, nil
}

