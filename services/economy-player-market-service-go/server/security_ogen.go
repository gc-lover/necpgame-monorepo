// Package server Issue: #1594 - Security handler for ogen
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
)

type SecurityHandler struct{}

func NewSecurityHandler() *SecurityHandler {
	return &SecurityHandler{}
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, t api.BearerAuth) (context.Context, error) {
	// TODO: JWT validation
	token := t.GetToken()
	_ = token

	ctx = context.WithValue(ctx, "user_id", "user-from-token")
	return ctx, nil
}
