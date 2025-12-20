// Package server Issue: ogen migration
package server

import (
	"context"

	"github.com/necpgame/progression-paragon-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ api.OperationName, _ api.BearerAuth) (context.Context, error) {
	return ctx, nil
}
