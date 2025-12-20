// Package server Issue: #1600
package server

import (
	"context"

	"github.com/necpgame/character-engram-security-service-go/pkg/api"
)

type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement bearer token validation
	return ctx, nil
}
