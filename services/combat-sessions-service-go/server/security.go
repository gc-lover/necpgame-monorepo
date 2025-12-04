// Issue: #1595
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sessions-service-go/pkg/api"
)

// SecurityHandler implements ogen security
type SecurityHandler struct{}

// HandleBearerAuth handles JWT bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	return ctx, nil
}

