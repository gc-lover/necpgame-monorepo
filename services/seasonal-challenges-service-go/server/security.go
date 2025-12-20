// Package server Issue: ogen migration
package server

import (
	"context"

	"github.com/necpgame/seasonal-challenges-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement bearer token validation
	return ctx, nil
}
