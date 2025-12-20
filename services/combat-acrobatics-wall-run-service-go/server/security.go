// Package server Issue: #1510
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/combat-acrobatics-wall-run-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement bearer token validation
	return ctx, nil
}
