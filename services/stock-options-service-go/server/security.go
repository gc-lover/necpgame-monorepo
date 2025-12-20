// Package server Issue: #1600
package server

import (
	"context"

	"github.com/necpgame/stock-options-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ api.OperationName, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	return ctx, nil
}
