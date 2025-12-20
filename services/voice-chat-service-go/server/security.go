// Package server Issue: ogen migration
package server

import (
	"context"
	"strings"

	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/pkg/api"
	"github.com/go-faster/errors"
	"github.com/ogen-go/ogen/ogenerrors"
)

type SecurityHandler struct {
	jwtValidator *JwtValidator
	authEnabled  bool
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, t api.BearerAuth) (context.Context, error) {
	if !s.authEnabled || s.jwtValidator == nil {
		return ctx, nil
	}

	token := strings.TrimPrefix(t.Token, "Bearer ")
	token = strings.TrimSpace(token)

	claims, err := s.jwtValidator.Verify(ctx, "Bearer "+token)
	if err != nil {
		return ctx, errors.Wrap(ogenerrors.ErrSecurityRequirementIsNotSatisfied, "invalid or expired token")
	}

	ctx = context.WithValue(ctx, "claims", claims)
	ctx = context.WithValue(ctx, "user_id", claims.Subject)
	ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

	return ctx, nil
}
