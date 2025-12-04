// Issue: #1599 - ogen migration
package server

import (
	"context"

	"github.com/necpgame/gameplay-progression-core-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	return ctx, nil
}

