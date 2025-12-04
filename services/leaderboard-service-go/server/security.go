// Issue: ogen migration
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/leaderboard-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement bearer token validation
	return ctx, nil
}

