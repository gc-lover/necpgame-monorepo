// Package server Issue: #1442
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
)

// SecurityHandler implements ogen security handler
type SecurityHandler struct{}

// HandleBearerAuth implements BearerAuth security (placeholder)
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ api.OperationName, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation if needed
	return ctx, nil
}
