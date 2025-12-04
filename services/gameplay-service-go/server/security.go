// Issue: #1599
package server

import (
	"context"
	"errors"

	api "github.com/necpgame/gameplay-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	if t.Token == "" {
		return ctx, errors.New("bearer token required")
	}
	return ctx, nil
}

