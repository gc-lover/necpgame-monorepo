// Package server Issue: #1598
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-moderation-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ api.OperationName, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	if t.Token == "" {
		return ctx, errors.New("bearer token required")
	}
	return ctx, nil
}
